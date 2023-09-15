package com.rcs2.mcqsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user_role")
public class UserRole {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long urId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userId;
	
	@ManyToOne
	@JoinColumn(name = "roleId")
	private Role roleId;
	
}
