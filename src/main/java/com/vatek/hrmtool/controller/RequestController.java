package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.request.RequestDto;
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
    private final RequestRepository requestRepository;
    private RequestService requestService;

    @PostMapping("/create")
    public ResponseDto<RequestDto> createRequest(@RequestBody CreateRequestForm requestForm){
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @PutMapping("/updateStatus")
    public ResponseDto<RequestDto> updateRequestType(TypeRequest typeRequest){
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @GetMapping("/getAllRequestByManager")
    public ResponseDto<RequestDto> getProjectByManager(Pageable pageable){
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/getAllRequest")
    public ResponseDto<RequestDto> getAllProject(Pageable pageable){
        return null;
    }
}
