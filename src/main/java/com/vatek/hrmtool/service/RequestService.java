package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.readable.form.createForm.CreateRequestForm;
import org.springframework.data.domain.Pageable;

public interface RequestService {
    RequestDto createRequest(CreateRequestForm createRequestForm);

    ListResponseDto<RequestDto> getAllRequestsByStatus(Pageable pageable,RequestStatus requestStatus);

    ListResponseDto<RequestDto> getAllDeviceRequestsByStatus(Pageable pageable, RequestStatus requestStatus);

    ListResponseDto<RequestDto> getRequestsByManagerByStatus(Pageable pageable, RequestStatus requestStatus);
}
