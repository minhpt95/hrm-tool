package com.vatek.hrmtool.exception;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {
	
	/**
	 * 
	 */
	private String type;
	private String message;
	private String code;
}
