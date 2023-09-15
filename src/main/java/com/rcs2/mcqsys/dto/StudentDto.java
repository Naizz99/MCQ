package com.rcs2.mcqsys.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudentDto {
	
	private String studentId;
	private String userId;
	private String grade;
	private String gpa;
	private String averageMark;
	private int attendQues;
	private String active;
	private String createBy;
	private String createDate;
	private String updateBy;
	private String updateDate;
	private String name;
	private String gender;
	private String email;
	private String mobile;
	private String totalAttempts;
	private List<StudentPaperReviewDto> papers;
	private List<SubjectWiseModelResultsDto> moduleResults;
}
