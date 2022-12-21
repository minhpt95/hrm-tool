package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.enumeration.RequestStatus;
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
import java.time.Instant;
import java.util.ArrayList;

@Service
@AllArgsConstructor
@Log4j2
public class RequestServiceImpl implements RequestService {
    private RequestRepository requestRepository;

    private UserRepository userRepository;

    private RequestMapping requestMapping;

    @Override
    public RequestDto createRequest(CreateRequestForm createRequestForm) {

        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = userRepository.findUserEntityById(currentUser.getId());


        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestTitle(createRequestForm.getRequestTitle());
        requestEntity.setRequestReason(createRequestForm.getRequestReason());
        requestEntity.setStatus(RequestStatus.PENDING);
        requestEntity.setTypeRequest(createRequestForm.getTypeRequest());
        requestEntity.setFromDate(DateUtil.convertStringDateToInstant(createRequestForm.getFromDate()));
        requestEntity.setToDate(DateUtil.convertStringDateToInstant(createRequestForm.getFromDate()));
        requestEntity.setRequester(userEntity);
        requestEntity.setCreatedBy(currentUser.getId());
        requestEntity.setCreatedTime(Instant.now());

        requestEntity = requestRepository.save(requestEntity);
        return requestMapping.toDto(requestEntity);
    }

    @Override
    public ListResponseDto<RequestDto> getAllRequests(Pageable pageable){
        return null;
    }

    @Override
    public ListResponseDto<RequestDto> getAllDeviceRequests(Pageable pageable){
        return null;
    }

    @Override
    public ListResponseDto<RequestDto> getRequestsByManager(Pageable pageable){
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<RequestEntity> specification = (root, query, criteriaBuilder) -> {

            var predicates = new ArrayList<Predicate>();

            Join<RequestEntity, UserEntity> requesterEntityJoin = root.join("requester");
            Join<UserEntity,ProjectEntity> workingProjectEntityJoin = requesterEntityJoin.join("workingProject");
            Join<ProjectEntity,UserEntity> projectManagerEntityJoin = workingProjectEntityJoin.join("managerUser");

            predicates.add(criteriaBuilder.equal(projectManagerEntityJoin.get("id"),currentUser.getId()));
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
