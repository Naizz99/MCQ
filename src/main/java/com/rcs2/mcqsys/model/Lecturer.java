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
@Table(name = "lecturer")
public class Lecturer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lecturerId;
	
	@ManyToOne 
	@JoinColumn(name = "userId")
	private User userId;
	
	private String instituteName;
	private String nicNumber;
	private String educationQualification;
	private String description;
}
