package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class ClassRoomMembers {
	
	private long classroomId;
	
	private long userId;

	private String email;
	
	private String name;
	
}
