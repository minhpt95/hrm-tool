package com.vatek.hrmtool.dto.timesheet;

import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TimesheetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimesheetDto {
    private String title;
    private String description;
    private Integer workingHours;
    private TimesheetType timesheetType;
    private Instant workingDay;
    private ApprovalStatus requestStatus;
    private ProjectDto projectEntity;
    private UserDto userEntity;
}
