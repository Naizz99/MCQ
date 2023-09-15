package com.rcs2.mcqsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "student")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long studentId;

	@ManyToOne
	@JoinColumn(name = "grade")
    private Grade grade;

	@OneToOne 
	@JoinColumn(name = "userId")
	private User userId;
	
	private double gpa;
	private int attemptsAllowed;
}

