package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.enumeration.TypeRequest;
import com.vatek.hrmtool.readable.form.createForm.CreateRequestForm;
import com.vatek.hrmtool.respository.RequestRepository;
import com.vatek.hrmtool.service.RequestService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/request")
@RestController
@AllArgsConstructor
@Log4j2
public class RequestController {
    private RequestService requestService;

    @PostMapping("/create")
    public ResponseDto<RequestDto> createRequest(@RequestBody CreateRequestForm requestForm){
        var responseDto = new ResponseDto<RequestDto>();
        responseDto.setContent(requestService.createRequest(requestForm));
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        return responseDto;
    }

    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @PutMapping("/approval-request")
    public ResponseDto<RequestDto> confirmRequest (TypeRequest typeRequest){
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @PutMapping("/revert-approval")
    public ResponseDto<RequestDto> revertApproval (){
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @GetMapping("/get-requests-by-manager")
    public ListResponseDto<RequestDto> getRequestsByManager(Pageable pageable){
        return requestService.getRequestsByManagerByStatus(pageable, RequestStatus.PENDING);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/get-all-requests")
    public ListResponseDto<RequestDto> getAllRequests(Pageable pageable){
        return requestService.getAllRequestsByStatus(pageable, RequestStatus.PENDING);
    }

    @PreAuthorize("hasAnyRole('ROLE_IT_ADMIN')")
    @GetMapping("/get-all-device-requests")
    public ListResponseDto<RequestDto> getAllDeviceRequests(Pageable pageable){
        return requestService.getAllDeviceRequestsByStatus(pageable,RequestStatus.PENDING);
    }
}
