package com.vatek.hrmtool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse implements Serializable {
	
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	private String errorType;
	private String message;
	private String errorCode;
}
