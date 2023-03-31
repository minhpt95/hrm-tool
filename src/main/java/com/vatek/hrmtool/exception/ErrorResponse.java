package com.vatek.hrmtool.exception;

import lombok.*;

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
