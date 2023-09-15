package com.rcs2.mcqsys.dto;

import java.util.List;

import com.rcs2.mcqsys.model.Role;

import lombok.Data;

@Data
public class UserDto {
	
	private Long userId;
	private String name;
	private String gender; 
	private String email; 
	private String mobile;
	private List<String> role;
	private String roleId;
	private String active;

}
