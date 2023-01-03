package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.readable.form.create.CreateRequestForm;
import com.vatek.hrmtool.readable.form.update.UpdateApprovalStatusForm;
import org.springframework.data.domain.Pageable;

public interface RequestService {
    RequestDto createRequest(CreateRequestForm createRequestForm);
    RequestDto approvalRequest(UpdateApprovalStatusForm updateApprovalStatusForm);
    ListResponseDto<RequestDto> getAllRequestsByStatus(Pageable pageable, ApprovalStatus requestStatus);
    ListResponseDto<RequestDto> getAllDeviceRequestsByStatus(Pageable pageable, ApprovalStatus requestStatus);
    ListResponseDto<RequestDto> getRequestsByManagerByStatus(Pageable pageable, ApprovalStatus requestStatus);
}
