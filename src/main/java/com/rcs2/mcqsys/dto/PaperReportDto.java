package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class PaperReportDto {

	private long paperId;

    private String subject;

	private String name;

    private String grade;
	
	private int time;

    private String Publisher;

	private int numberOfQuestion;

	private String active;
	
	private int attemptCount;
}
