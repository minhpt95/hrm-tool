package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.DayOffEntity;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import com.vatek.hrmtool.enumeration.TypeRequest;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.mapping.RequestMapping;
import com.vatek.hrmtool.readable.form.create.CreateRequestForm;
import com.vatek.hrmtool.readable.form.update.UpdateApprovalStatusForm;
import com.vatek.hrmtool.respository.DayOffEntityRepository;
import com.vatek.hrmtool.respository.RequestRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.RequestService;
import com.vatek.hrmtool.util.CommonUtil;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RequestServiceImpl implements RequestService {
    private final DayOffEntityRepository dayOffEntityRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapping requestMapping;
    @Override
    @Transactional(rollbackOn = Exception.class)
    public RequestDto createRequest(CreateRequestForm createRequestForm) {

        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userEntity = userRepository.findUserEntityById(currentUser.getId());
        var requestEntity = new RequestEntity();
        requestEntity.setRequestTitle(createRequestForm.getRequestTitle());
        requestEntity.setRequestReason(createRequestForm.getRequestReason());
        requestEntity.setTypeRequest(createRequestForm.getTypeRequest());
        requestEntity.setCreatedBy(currentUser.getId());
        requestEntity.setCreatedTime(DateUtil.getInstantNow());

        switch (createRequestForm.getTypeRequest()){
            case REQUEST_DEVICE -> {
                requestEntity = requestRepository.save(requestEntity);
                return requestMapping.toDto(requestEntity);
            }
            case DAY_OFF -> {
                var from = DateUtil.convertStringDateToInstant(createRequestForm.getFromDate());
                var to = DateUtil.convertStringDateToInstant(createRequestForm.getToDate());

                if(!from.isBefore(to)){
                    throw new ProductException(
                            ErrorResponse
                            .builder()
                                    .code(ErrorConstant.Code.FROM_DATE_TO_DATE_VALIDATE)
                                    .type(ErrorConstant.Type.FROM_DATE_TO_DATE_VALIDATE)
                                    .message(ErrorConstant.Message.FROM_DATE_TO_DATE_VALIDATE)
                                    .build()
                    );
                }


                Specification<DayOffEntity> dayOffEntitySpecification = (root,query,criteriaBuilder) -> {
                    var predicates = new ArrayList<Predicate>();

                    predicates.add(criteriaBuilder.equal(root.get("dayoffEntityId").get("userId"),currentUser.getId()));
                    predicates.add(criteriaBuilder.notEqual(root.get("status"),ApprovalStatus.REJECTED));
                    predicates.add(criteriaBuilder.between(root.get("dayoffEntityId").get("dateOff"),from,to));

                    switch (createRequestForm.getTypeDayoff()){
                        case MORNING , AFTERNOON -> {
                            predicates.add(
                                    criteriaBuilder.or(
                                            criteriaBuilder.equal(root.get("dayoffEntityId").get("typeDayOff"),createRequestForm.getTypeDayoff()),
                                            criteriaBuilder.equal(root.get("dayoffEntityId").get("typeDayOff"), TypeDayOff.FULL)
                                    )
                            );
                        }
                    }

                    Predicate[] p = predicates.toArray(new Predicate[0]);

                    return criteriaBuilder.and(p);
                };

                var countOverlapping = dayOffEntityRepository.count(dayOffEntitySpecification);

                if(countOverlapping > 0){
                    throw new ProductException(
                            ErrorResponse
                                    .builder()
                                    .code(ErrorConstant.Code.OVERLAPPING_DATE)
                                    .type(ErrorConstant.Type.OVERLAPPING_DATE)
                                    .message(ErrorConstant.Message.OVERLAPPING_DATE)
                                    .build()
                    );
                }

                var fromCounter = from;
                List<DayOffEntity> dayOffEntities = new ArrayList<>();
                while (!fromCounter.isAfter(to)){
                    var dayOffEntity = new DayOffEntity();
                    var dayOffEntityId = new DayOffEntity.DayOffEntityId();
                    dayOffEntityId.setDateOff(fromCounter);
                    dayOffEntityId.setUserId(currentUser.getId());
                    dayOffEntityId.setTypeDayOff(createRequestForm.getTypeDayoff());
                    dayOffEntity.setDayoffEntityId(dayOffEntityId);
                    dayOffEntity.setStatus(ApprovalStatus.PENDING);
                    dayOffEntity.setRequestEntity(requestEntity);
                    dayOffEntities.add(dayOffEntity);
                    fromCounter = fromCounter.plus(1, ChronoUnit.DAYS);
                }
                requestEntity.setDayOffEntityList(dayOffEntities);
                requestEntity.setRequester(userEntity);
                RequestEntity requestEntityList = requestRepository.save(requestEntity);

                return requestMapping.toDto(requestEntityList);
            }

            default -> throw new ProductException(
                    ErrorResponse.builder()
                            .code(ErrorConstant.Code.NOT_FOUND)
                            .message(ErrorConstant.Message.NOT_FOUND)
                            .type(ErrorConstant.Type.NOT_FOUND)
                    .build()
            );
        }
    }

    @Override
    public ListResponseDto<RequestDto> getAllRequestsByStatus(Pageable pageable, ApprovalStatus requestStatus){

        Specification<RequestEntity> specification = (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(criteriaBuilder.equal(root.get("typeRequest"),TypeRequest.REQUEST_DEVICE));

            Predicate[] p = new Predicate[predicates.size()];

            return criteriaBuilder.and(predicates.toArray(p));
        };

        var requestEntities = requestRepository.findAll(
                specification,
                CommonUtil.buildPageable(pageable.getPageNumber(),pageable.getPageSize())
        );

        Page<RequestDto> requestDtos = requestEntities.map(requestMapping::toDto);

        return new ListResponseDto<>(requestDtos, pageable.getPageSize(), pageable.getPageNumber());
    }

    @Override
    public RequestDto approvalRequest(UpdateApprovalStatusForm form) {
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RequestEntity requestEntity = requestRepository.findById(form.getId()).orElse(null);

        if(requestEntity == null){
            throw new ProductException(
                    ErrorResponse.builder()
                            .message(String.format(ErrorConstant.Message.NOT_FOUND,"Request with id : " + form.getId()))
                            .code(ErrorConstant.Code.NOT_FOUND)
                            .type(ErrorConstant.Type.NOT_FOUND)
                            .build()
            );
        }

        requestEntity.setModifiedBy(currentUser.getId());
        requestEntity.setModifiedTime(Instant.now());

        requestEntity = requestRepository.save(requestEntity);

        return requestMapping.toDto(requestEntity);
    }

    @Override
    public ListResponseDto<RequestDto> getAllDeviceRequestsByStatus(Pageable pageable, ApprovalStatus requestStatus){
        Specification<RequestEntity> specification = (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(criteriaBuilder.equal(root.get("status"),requestStatus));
            predicates.add(criteriaBuilder.equal(root.get("typeRequest"),TypeRequest.REQUEST_DEVICE));

            Predicate[] p = new Predicate[predicates.size()];

            return criteriaBuilder.and(predicates.toArray(p));
        };

        var requestEntities = requestRepository.findAll(
                specification,
                CommonUtil.buildPageable(pageable.getPageNumber(),pageable.getPageSize())
        );

        Page<RequestDto> requestDtos = requestEntities.map(requestMapping::toDto);

        return new ListResponseDto<>(requestDtos, pageable.getPageSize(), pageable.getPageNumber());
    }

    @Override
    public ListResponseDto<RequestDto> getRequestsByManagerByStatus(Pageable pageable, ApprovalStatus requestStatus){
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<RequestEntity> specification = (root, query, criteriaBuilder) -> {

            var predicates = new ArrayList<Predicate>();

            Join<RequestEntity, UserEntity> requesterEntityJoin = root.join("requester");
            Join<UserEntity,ProjectEntity> workingProjectEntityJoin = requesterEntityJoin.join("workingProject");
            Join<ProjectEntity,UserEntity> projectManagerEntityJoin = workingProjectEntityJoin.join("managerUser");

            predicates.add(criteriaBuilder.equal(projectManagerEntityJoin.get("id"),currentUser.getId()));
            predicates.add(criteriaBuilder.equal(root.get("status"),requestStatus));
            predicates.add(criteriaBuilder.equal(root.get("typeRequest"), TypeRequest.DAY_OFF));

            query.distinct(true);

            Predicate[] p = new Predicate[predicates.size()];

            return criteriaBuilder.and(predicates.toArray(p));
        };

        var requestEntities = requestRepository.findAll(
                specification,
                CommonUtil.buildPageable(pageable.getPageNumber(), pageable.getPageSize())
        );

        Page<RequestDto> requestDtos = requestEntities.map(requestMapping::toDto);

        return new ListResponseDto<>(requestDtos,pageable.getPageSize(), pageable.getPageNumber());
    }
}
