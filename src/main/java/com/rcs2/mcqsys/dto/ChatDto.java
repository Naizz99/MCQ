package com.rcs2.mcqsys.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.rcs2.mcqsys.model.User;

import lombok.Data;

@Data
public class ChatDto {
	private String email;
	
	private User user;
	
	private String lastMessage;
	
	private LocalDate date;
	
	private LocalTime time;
	
	private boolean readStatus;
}
