package com.vatek.hrmtool.readable.form.update;

import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.validator.anotation.EnumPatternConstraint;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateApprovalStatusForm {
    @NotNull
    private Long id;
    @EnumPatternConstraint(regexp = "APPROVED|REJECTED")
    private ApprovalStatus approvalStatus;
}
