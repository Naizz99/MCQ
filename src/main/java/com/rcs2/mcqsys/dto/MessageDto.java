package com.rcs2.mcqsys.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class MessageDto {
	
	private long cmId;
    private long classroomId;
    private String userName;
	private String type;
	private LocalDate sentDate;
	private LocalTime sentTime;
	private String message;
	private boolean readStatus;
}
