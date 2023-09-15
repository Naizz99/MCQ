package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class PublisherRegisterDto {
	private String cmpName;
	private String cmpAddress;
	private String cmpEmail;
	private String cmpMobile;
	private String cmpDescription;
	private String cmpPassword;
}
