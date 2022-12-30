package com.vatek.hrmtool.readable.form.create;

import com.vatek.hrmtool.enumeration.TimesheetType;
import com.vatek.hrmtool.validator.anotation.DateFormatConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTimesheetForm {
    private String title;
    private String description;
    @Min(1)
    private Long projectId;
    @Min(1)
    @Max(8)
    private Integer workingHours;
    private TimesheetType timesheetType;
    @DateFormatConstraint
    private String workingDay;
}
