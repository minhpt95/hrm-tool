package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.readable.form.createForm.CreateRequestForm;
import com.vatek.hrmtool.respository.RequestRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.RequestService;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@Log4j2
public class RequestServiceImpl implements RequestService {
    private RequestRepository requestRepository;

    @Override
    public RequestDto createRequest(CreateRequestForm createRequestForm) {

        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestTitle(createRequestForm.getRequestTitle());
        requestEntity.setRequestReason(createRequestForm.getRequestReason());
        requestEntity.setStatus(RequestStatus.PENDING);
        requestEntity.setTypeRequest(createRequestForm.getTypeRequest());
        requestEntity.setFromDate(DateUtil.convertStringDateToInstant(createRequestForm.getFromDate()));
        requestEntity.setToDate(DateUtil.convertStringDateToInstant(createRequestForm.getFromDate()));
        requestEntity.setCreatedBy(currentUser.getId());
        requestEntity.setCreatedTime(Instant.now());

        requestRepository.save(requestEntity);
        return null;
    }
}
