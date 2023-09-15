package com.rcs2.mcqsys.dto;

import java.util.List;

import com.rcs2.mcqsys.model.Subject;

import lombok.Data;

@Data
public class SubjectWiseModelResultsDto {
	
	private Subject	subjectId;
	private long paperCount;
	private List<ModuleResultDto> moduleResultList;
}





