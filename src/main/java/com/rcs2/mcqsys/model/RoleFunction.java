package com.rcs2.mcqsys.model;

import java.time.LocalDate;

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
@Table(name = "role_function")
public class RoleFunction{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uflId;
	
	@ManyToOne
	@JoinColumn(name = "functionId")
	private Functions functionId;
	
	@ManyToOne
	@JoinColumn(name = "role")
	private Role role;
	
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
}
