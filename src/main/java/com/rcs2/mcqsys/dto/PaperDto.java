package com.rcs2.mcqsys.dto;

import com.rcs2.mcqsys.model.PaperTemplate;

import lombok.Data;

@Data
public class PaperDto {
	
    private String userId;
    
    private String paperId;
    
    private String subjectId;
	
	private String name;
	
    private String grade;
	
	private String time;
	
    private String Publisher;

	private String numberOfQuestion;
	
	private String answersPerQuestion;
	
	private String active;
	
	private String status;
	
	private String user;
	
	private PaperTemplate template;
	
	private boolean availableForBuy;
	
}

