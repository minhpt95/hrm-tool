package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.timesheet.TimesheetDto;
import com.vatek.hrmtool.readable.form.create.CreateTimesheetForm;

public interface TimesheetService {
    TimesheetDto createTimesheet(CreateTimesheetForm form);
}
