package com.rcs2.mcqsys.dto;

import java.util.List;

import lombok.Data;

@Data
public class MarkedQuestionsdto {
	private String paperID;
	
	private List<QuestionWithCorrectAnswerDto> questionList;
}
