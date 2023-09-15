package com.rcs2.mcqsys.dto;


import lombok.Data;

@Data
public class EmailDto {
	
	private String name;
    private String email;
    private String subject;
	private String message;
}
