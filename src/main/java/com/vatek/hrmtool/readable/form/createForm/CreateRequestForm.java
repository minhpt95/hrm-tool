package com.vatek.hrmtool.readable.form.createForm;

import com.vatek.hrmtool.enumeration.TypeRequest;
import com.vatek.hrmtool.validator.anotation.DateFormatConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.SqlResultSetMapping;

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
}
