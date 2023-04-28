package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.timesheet.TimesheetDto;
import com.vatek.hrmtool.readable.form.approval.ApprovalForm;
import com.vatek.hrmtool.readable.form.create.CreateTimesheetForm;
import com.vatek.hrmtool.readable.form.update.UpdateTimesheetForm;
import com.vatek.hrmtool.service.TimesheetService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/api/timesheet")
public class TimesheetController {
    private TimesheetService timesheetService;

    @PostMapping("/create")
    public ResponseDto<TimesheetDto> createTimesheet(@RequestBody CreateTimesheetForm form){
        var responseDto = new ResponseDto<TimesheetDto>();

        responseDto.setContent(timesheetService.createTimesheet(form));
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);

        return responseDto;
    }

    @PreAuthorize("ROLE_PM")
    @PutMapping("/update")
    public ResponseDto<TimesheetDto> updateTimesheet(@RequestBody UpdateTimesheetForm form){
        var responseDto = new ResponseDto<TimesheetDto>();

        responseDto.setContent(timesheetService.updateTimesheet(form));
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);

        return responseDto;
    }

    @PreAuthorize("ROLE_PM")
    @PutMapping("/decision")
    public ResponseDto<TimesheetDto> decisionTimesheet(ApprovalForm approvalForm){
        var responseDto = new ResponseDto<TimesheetDto>();

        responseDto.setContent(timesheetService.decisionTimesheet(approvalForm));
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);

        return responseDto;
    }
}
