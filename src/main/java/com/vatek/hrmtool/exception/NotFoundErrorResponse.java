package com.vatek.hrmtool.exception;

import com.vatek.hrmtool.constant.ErrorConstant;
import lombok.Builder;
public class NotFoundErrorResponse extends ErrorResponse {

    private String notFoundItem;

    public NotFoundErrorResponse(String notFoundItem) {
        super(
                ErrorConstant.Type.NOT_FOUND,
                String.format(ErrorConstant.Message.NOT_FOUND,notFoundItem),
                ErrorConstant.Code.NOT_FOUND
        );
    }
}
