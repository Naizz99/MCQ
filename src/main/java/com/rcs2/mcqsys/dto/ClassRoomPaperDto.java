package com.rcs2.mcqsys.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClassRoomPaperDto {
	
    private String paperName;
		
	private boolean active;
	
	private LocalDate expireDate;
	
	private int attendCount;
}
