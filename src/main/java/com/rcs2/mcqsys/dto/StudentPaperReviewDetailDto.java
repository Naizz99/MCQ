package com.rcs2.mcqsys.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class StudentPaperReviewDetailDto {
	
	private long paperID;
	private long questionId;
	private long moduleId;
	private long correctAnswer;
	private long givenAnswer;
	private long studentId;
	private long reviewId;
	private LocalDate date;
	private Integer result;
	private boolean status;
	private boolean active;
		
}

