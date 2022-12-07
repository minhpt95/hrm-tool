package com.vatek.hrmtool.controller.advice;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ErrorBindingDto;
import com.vatek.hrmtool.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.exception.TokenRefreshException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class ResponseHandler {

    @ExceptionHandler(ProductException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleProductException(ProductException ex, WebRequest request) {
        return ex.getErrorResponse();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorConstant.Code.SUCCESS);
        errorResponse.setMessage("You do not have permission to access");
        return errorResponse;
    }


    @ExceptionHandler(value = DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleDisableException(DisabledException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorConstant.Code.USER_INACTIVE);
        errorResponse.setErrorType(ErrorConstant.Type.USER_INACTIVE);
        errorResponse.setMessage(ErrorConstant.Message.USER_INACTIVE);
        return errorResponse;
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorConstant.Code.SUCCESS);
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<List<?>> handleBindingErrors(BindException ex) {
        ResponseDto<List<?>> errorResponse = new ResponseDto<>();
        errorResponse.setErrorCode(ErrorConstant.Code.SUCCESS);

        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        List<ErrorBindingDto> errorBindingDtoList = errors.stream().map(x -> {
            ErrorBindingDto errorBindingDto = new ErrorBindingDto();
            errorBindingDto.setFieldError(x.getField());
            errorBindingDto.setErrorMessage(x.getDefaultMessage());
            return errorBindingDto;
        }).collect(Collectors.toList());

        errorResponse.setContent(errorBindingDtoList);

        return errorResponse;
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindingErrors(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorConstant.Code.SUCCESS);
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }
}
