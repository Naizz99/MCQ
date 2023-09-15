package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class UserPaperDto {

	private long userId;
	
	private String userName;
	
	private long paperId;
	
	private String grade;
	
	private String publisher;
	
	private String subject;

	private String PaperName;

	private String active;
	
    private int totalCreateCount;

    private int onActiveCount;
    
    private int onDeactiveCount;

	private int attendedCount;

	private double averageScore;
}
