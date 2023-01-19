package com.vatek.hrmtool.readable.form.update;

import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.validator.anotation.EnumPatternConstraint;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateRequestStatusForm {
    @NotNull
    private Long id;

    private Instant dayOff;
    @EnumPatternConstraint(regexp = "APPROVED|REJECTED")
    private ApprovalStatus approvalStatus;
}
