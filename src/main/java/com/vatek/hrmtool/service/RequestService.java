package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.readable.form.createForm.CreateRequestForm;
import org.springframework.data.domain.Pageable;

public interface RequestService {
    RequestDto createRequest(CreateRequestForm createRequestForm);

    ListResponseDto<RequestDto> getAllRequests(Pageable pageable);

    ListResponseDto<RequestDto> getAllDeviceRequests(Pageable pageable);

    ListResponseDto<RequestDto> getRequestsByManager(Pageable pageable);
}
