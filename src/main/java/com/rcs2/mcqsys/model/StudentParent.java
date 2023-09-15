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
@Table(name = "student_parent")
public class StudentParent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long studentParentId;
	
	@ManyToOne
	@JoinColumn(name = "studentId")
    private User studentId;
	
	@ManyToOne
	@JoinColumn(name = "parentId")
    private User parentId;
	
	private String nic;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}
