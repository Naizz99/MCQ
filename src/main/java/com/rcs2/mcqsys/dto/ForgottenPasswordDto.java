package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class ForgottenPasswordDto {
	private String email; 
	private long userId; 
	private String username; 
	private String isActive; 
	private String otp;
	
}
