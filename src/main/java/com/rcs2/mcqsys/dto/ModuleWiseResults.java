package com.rcs2.mcqsys.dto;

import com.rcs2.mcqsys.model.Module;

import lombok.Data;

@Data
public class ModuleWiseResults {
	private Module moduleId;
	private long paperId;
	private int allQuestionCount;
	private int answeredQuestionCount;
	private int correctAnsweredCount;
	private double result;
}