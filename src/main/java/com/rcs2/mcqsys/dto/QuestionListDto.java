package com.rcs2.mcqsys.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionListDto {
	private String paperID;
	
	private List<String> questionList;
}
