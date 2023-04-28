package com.vatek.hrmtool.readable.form.update;

import com.vatek.hrmtool.enumeration.TimesheetType;
import com.vatek.hrmtool.readable.form.create.CreateTimesheetForm;
import com.vatek.hrmtool.validator.anotation.DateFormatConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTimesheetForm extends CreateTimesheetForm {
    @NotNull
    private Long id;
}
