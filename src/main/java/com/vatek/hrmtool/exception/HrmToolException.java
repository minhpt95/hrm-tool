package com.vatek.hrmtool.exception;

public class HrmToolException extends RuntimeException {
    private ErrorResponse errorResponse;

    public HrmToolException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}