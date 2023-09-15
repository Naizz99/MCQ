package com.rcs2.mcqsys.dto;

import java.util.List;

import com.rcs2.mcqsys.model.PaperAnswer;

import lombok.Data;

@Data
public class QuestionWithCorrectAnswerDto {
	private long pqId;
		
	private long moduleId;
	
	private long paId;
	
	private List<PaperAnswer> answerList;
}
