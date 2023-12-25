package com.vatek.hrmtool.exception;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrmToolException extends RuntimeException {
    private ErrorResponse errorResponse;
}