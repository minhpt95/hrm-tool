package com.vatek.hrmtool.exception;

import com.vatek.hrmtool.constant.ErrorConstant;
public class LoginInvalidErrorResponse extends ErrorResponse {
    public LoginInvalidErrorResponse() {
        super(
                ErrorConstant.Type.LOGIN_INVALID,
                ErrorConstant.Message.LOGIN_INVALID,
                ErrorConstant.Code.LOGIN_INVALID
        );
    }
}
