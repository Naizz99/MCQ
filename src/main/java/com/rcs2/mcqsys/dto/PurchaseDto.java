package com.rcs2.mcqsys.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PurchaseDto {

	private long userId;
	
	private String userName;
	
	private String type;
	
	private long id;
	
	private String paperName;
	
	private String purchaseDate;
	
	private String expireDate;

	private String status;
}
