package com.rcs2.mcqsys.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class StudentPaperReviewDto {
	
	private String paper;
	private LocalDate date;
	private LocalTime time;
	private int questionCount;
	private int answeredCount;
	private int correctCount;
	private double total;
		
}

