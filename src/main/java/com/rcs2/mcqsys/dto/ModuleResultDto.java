package com.rcs2.mcqsys.dto;

import com.rcs2.mcqsys.model.Module;

import lombok.Data;

@Data
public class ModuleResultDto{
	private Module module;
	private int allQuestionCount;
	private int attendQuestionCount;
	private int correctQuestionCount;
	private String result;
}
