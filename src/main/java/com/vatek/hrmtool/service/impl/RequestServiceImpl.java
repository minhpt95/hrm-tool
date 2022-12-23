package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.DayOffEntity;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import com.vatek.hrmtool.enumeration.TypeRequest;
import com.vatek.hrmtool.mapping.RequestMapping;
import com.vatek.hrmtool.readable.form.createForm.CreateRequestForm;
import com.vatek.hrmtool.respository.RequestRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.RequestService;
import com.vatek.hrmtool.util.CommonUtil;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
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
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class RequestServiceImpl implements RequestService {
    private RequestRepository requestRepository;

    private UserRepository userRepository;

    private RequestMapping requestMapping;

    @Override
    @Transactional
    public RequestDto createRequest(CreateRequestForm createRequestForm) {

        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = userRepository.findUserEntityById(currentUser.getId());


        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestTitle(createRequestForm.getRequestTitle());
        requestEntity.setRequestReason(createRequestForm.getRequestReason());
        requestEntity.setStatus(RequestStatus.PENDING);
        requestEntity.setTypeRequest(createRequestForm.getTypeRequest());

        Instant from = DateUtil.convertStringDateToInstant(createRequestForm.getFromDate());
        Instant to = DateUtil.convertStringDateToInstant(createRequestForm.getToDate());

        List<DayOffEntity> dayOffEntities = new ArrayList<>();
        while (!from.isAfter(to)){
            DayOffEntity dayOffEntity = new DayOffEntity();
            if(createRequestForm.getTypeDayoff() != TypeDayOff.FULL){
                DayOffEntity.DateOffId dateOffId = new DayOffEntity.DateOffId();
                dateOffId.setDateOff(from);
                dateOffId.setUserId(currentUser.getId());
                dateOffId.setTypeDayoff(createRequestForm.getTypeDayoff());

                dayOffEntity.setDateOffId(dateOffId);
                dayOffEntity.setRequest(requestEntity);
                dayOffEntity.setCreatedBy(currentUser.getId());
                dayOffEntity.setCreatedTime(Instant.now());
                dayOffEntities.add(dayOffEntity);
            }else{
                DayOffEntity.DateOffId dateOffIdMorning = new DayOffEntity.DateOffId();
                dateOffIdMorning.setDateOff(from);
                dateOffIdMorning.setUserId(currentUser.getId());
                dateOffIdMorning.setTypeDayoff(TypeDayOff.MORNING);

                dayOffEntity.setDateOffId(dateOffIdMorning);
                dayOffEntity.setRequest(requestEntity);
                dayOffEntity.setCreatedBy(currentUser.getId());
                dayOffEntity.setCreatedTime(Instant.now());
                dayOffEntities.add(dayOffEntity);

                DayOffEntity.DateOffId dateOffIdAfternoon = new DayOffEntity.DateOffId();
                dateOffIdAfternoon.setDateOff(from);
                dateOffIdAfternoon.setUserId(currentUser.getId());
                dateOffIdAfternoon.setTypeDayoff(TypeDayOff.AFTERNOON);

                dayOffEntity.setDateOffId(dateOffIdAfternoon);
                dayOffEntity.setRequest(requestEntity);
                dayOffEntity.setCreatedBy(currentUser.getId());
                dayOffEntity.setCreatedTime(Instant.now());
                dayOffEntities.add(dayOffEntity);
            }
            from = from.plus(1, ChronoUnit.DAYS);
        }

        requestEntity.setDayOffEntities(dayOffEntities);
        requestEntity.setRequester(userEntity);
        requestEntity.setCreatedBy(currentUser.getId());
        requestEntity.setCreatedTime(Instant.now());

        requestEntity = requestRepository.save(requestEntity);
        return requestMapping.toDto(requestEntity);
    }

    @Override
    public ListResponseDto<RequestDto> getAllRequestsByStatus(Pageable pageable,RequestStatus requestStatus){
        return null;
    }
    @Override
    public ListResponseDto<RequestDto> getAllDeviceRequestsByStatus(Pageable pageable,RequestStatus requestStatus){
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
    public ListResponseDto<RequestDto> getRequestsByManagerByStatus(Pageable pageable,RequestStatus requestStatus){
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<RequestEntity> specification = (root, query, criteriaBuilder) -> {

            var predicates = new ArrayList<Predicate>();

            Join<RequestEntity, UserEntity> requesterEntityJoin = root.join("requester");
            Join<UserEntity,ProjectEntity> workingProjectEntityJoin = requesterEntityJoin.join("workingProject");
            Join<ProjectEntity,UserEntity> projectManagerEntityJoin = workingProjectEntityJoin.join("managerUser");

            predicates.add(criteriaBuilder.equal(projectManagerEntityJoin.get("id"),currentUser.getId()));
            predicates.add(criteriaBuilder.equal(root.get("status"),requestStatus));
            predicates.add(criteriaBuilder.notEqual(root.get("typeRequest"), TypeRequest.REQUEST_DEVICE));

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
