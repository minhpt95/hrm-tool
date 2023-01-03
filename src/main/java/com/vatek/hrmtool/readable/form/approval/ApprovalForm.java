package com.vatek.hrmtool.readable.form.approval;

import com.vatek.hrmtool.enumeration.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalForm {
    private Long id;
    private ApprovalStatus approvalStatus;
}
