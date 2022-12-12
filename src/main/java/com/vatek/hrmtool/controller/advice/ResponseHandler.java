package com.vatek.hrmtool.controller.advice;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ErrorBindingDto;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.exception.TokenRefreshException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


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
        return ErrorResponse
                .builder()
                .errorCode(ErrorConstant.Code.PERMISSION_DENIED)
                .errorType(ErrorConstant.Type.PERMISSION_DENIED)
                .message(ex.getMessage())
                .build();
    }


    @ExceptionHandler(value = DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleDisableException(DisabledException ex, WebRequest request) {
        return ErrorResponse
                .builder()
                .errorCode(ErrorConstant.Code.USER_INACTIVE)
                .errorType(ErrorConstant.Type.USER_INACTIVE)
                .message(ErrorConstant.Message.USER_INACTIVE)
                .build();
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return ErrorResponse
                .builder()
                .errorCode(ErrorConstant.Code.TOKEN_REFRESH_EXCEPTION)
                .errorType(ErrorConstant.Type.TOKEN_REFRESH_EXCEPTION)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<List<?>> handleBindingErrors(BindException ex) {
        ResponseDto<List<?>> errorResponse = new ResponseDto<>();
        errorResponse.setErrorCode(ErrorConstant.Code.INTERNAL_SERVER_ERROR);

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
        errorResponse.setErrorCode(ErrorConstant.Code.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindingErrors(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorConstant.Code.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }
}
