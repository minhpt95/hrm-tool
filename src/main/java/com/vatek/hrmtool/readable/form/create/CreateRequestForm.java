package com.vatek.hrmtool.readable.form.create;

import com.vatek.hrmtool.enumeration.TypeDayOff;
import com.vatek.hrmtool.enumeration.TypeRequest;
import com.vatek.hrmtool.validator.anotation.DateFormatConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestForm {
    private String requestTitle;

    private String requestReason;

    @DateFormatConstraint
    private String fromDate;

    @DateFormatConstraint
    private String toDate;

    private TypeRequest typeRequest;

    private TypeDayOff typeDayoff;
}
