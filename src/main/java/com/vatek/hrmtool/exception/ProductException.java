package com.vatek.hrmtool.exception;

public class ProductException extends RuntimeException {
    private ErrorResponse errorResponse;

    public ProductException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}