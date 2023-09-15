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
@Table(name = "student_subject")
public class StudentSubject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ssId;
	
	@ManyToOne
	@JoinColumn(name = "studentId")
	private Student studentId;
	
	@ManyToOne
	@JoinColumn(name = "subjectId")
	private Subject subjectId;
	
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}
