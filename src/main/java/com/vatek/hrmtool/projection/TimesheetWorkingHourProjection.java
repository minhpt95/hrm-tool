package com.vatek.hrmtool.projection;

import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TimesheetType;

import java.time.Instant;

public interface TimesheetWorkingHourProjection {
    Integer getWorkingHours();
    TimesheetType getTimesheetType();
    Instant getWorkingDay();
    ApprovalStatus getStatus();
    UserEntity getUserEntity();
}
