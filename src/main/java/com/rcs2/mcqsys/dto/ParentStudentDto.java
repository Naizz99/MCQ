package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class ParentStudentDto {
	private Long userId;
	private Long studentId;
	private Long parentId;
	private String mobile;
	private String email;
	private String parentEmail;
	private String name;
	private boolean isLinked;
	private String grade;
}
