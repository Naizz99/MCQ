package com.rcs2.mcqsys.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class ClassRoomStudentDto {
	
	private long userId;
	
	private String email;
	
	private boolean status;
	
	private int totalAttendPapers;
	
	private double totalMarks;
	
	private LocalTime totalAttendTime;
	
}
