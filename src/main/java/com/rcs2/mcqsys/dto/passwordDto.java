package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class passwordDto {
	private String userId;
	
	private String correctPassword;
	
	private String otp;
	
	private String newPassword;
	
}

