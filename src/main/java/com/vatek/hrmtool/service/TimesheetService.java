package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.timesheet.TimesheetDto;
import com.vatek.hrmtool.readable.form.approval.ApprovalForm;
import com.vatek.hrmtool.readable.form.create.CreateTimesheetForm;
import com.vatek.hrmtool.readable.form.update.UpdateTimesheetForm;

public interface TimesheetService {
    TimesheetDto createTimesheet(CreateTimesheetForm form);

    TimesheetDto updateTimesheet(UpdateTimesheetForm form);

    TimesheetDto decisionTimesheet(ApprovalForm form);
}
