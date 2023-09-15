package com.rcs2.mcqsys.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
		
	private String name; 
	private String firstName; 
	private String lastName; 
	private String bio; 
	private String gender; 
	 
//	@ManyToOne
//	@JoinColumn(name = "role")
//	private Role role;
	
	private String email; 
	private String mobile;
	private String username;
	
	private boolean logged; 
	private boolean active; 
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
	@Enumerated(EnumType.STRING)
	private Provider provider;
	
	private boolean forcePassword;
	
	private boolean sso;
	private String ssoEmail;
	
}
