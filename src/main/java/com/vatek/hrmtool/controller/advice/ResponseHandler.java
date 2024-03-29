package com.vatek.hrmtool.controller.advice;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ErrorBindingDto;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.HrmToolException;
import com.vatek.hrmtool.exception.TokenRefreshException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class ResponseHandler {

    @ExceptionHandler(HrmToolException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleProductException(HrmToolException ex) {
        return ex.getErrorResponse();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        return ErrorResponse
                .builder()
                .code(ErrorConstant.Code.PERMISSION_DENIED)
                .type(ErrorConstant.Type.PERMISSION_DENIED)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        return ErrorResponse
                .builder()
                .code(ErrorConstant.Code.AUTHENTICATION_ERROR)
                .type(ErrorConstant.Type.AUTHENTICATION_ERROR)
                .message(ex.getMessage())
                .build();
    }


    @ExceptionHandler(value = DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleDisableException() {
        return ErrorResponse
                .builder()
                .code(ErrorConstant.Code.USER_INACTIVE)
                .type(ErrorConstant.Type.USER_INACTIVE)
                .message(ErrorConstant.Message.USER_INACTIVE)
                .build();
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleTokenRefreshException(TokenRefreshException ex) {
        return ErrorResponse
                .builder()
                .code(ErrorConstant.Code.TOKEN_REFRESH_EXCEPTION)
                .type(ErrorConstant.Type.TOKEN_REFRESH_EXCEPTION)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<List<ErrorBindingDto>> handleBindingErrors(BindException ex) {
        var errorResponse = new ResponseDto<List<ErrorBindingDto>>();
        errorResponse.setCode(ErrorConstant.Code.MISSING_FIELD);
        errorResponse.setType(ErrorConstant.Type.MISSING_FIELD);

        var errors = ex.getBindingResult().getFieldErrors();

        var errorBindingDtoList = errors.stream().map(x -> {
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
    public ErrorResponse handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ErrorConstant.Code.INTERNAL_SERVER_ERROR);
        errorResponse.setType(ErrorConstant.Type.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleAnotherException(Exception ex) {
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setCode(ErrorConstant.Code.INTERNAL_SERVER_ERROR);
//        errorResponse.setType(ErrorConstant.Type.INTERNAL_SERVER_ERROR);
//        errorResponse.setMessage(ex.getMessage());
//        return errorResponse;
//    }
}
