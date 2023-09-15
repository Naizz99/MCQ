package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "active_sessions")
public class ActiveSessions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long asId;
	
	@ManyToOne
	@JoinColumn(name = "paperID")
    private Paper paperID;
	
	@ManyToOne
	@JoinColumn(name = "studentId")
    private Student studentId;
	
	private LocalTime startTime;
	private LocalTime endTime;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}

