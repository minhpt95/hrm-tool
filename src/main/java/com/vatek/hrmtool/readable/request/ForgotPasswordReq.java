package com.vatek.hrmtool.readable.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ForgotPasswordReq {
    @NotEmpty
    private String email;
}
