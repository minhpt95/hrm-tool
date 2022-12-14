package com.vatek.hrmtool.exception;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse implements Serializable {
	
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	private String type;
	private String message;
	private String code;
}
