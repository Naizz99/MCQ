package com.rcs2.mcqsys.model;

import java.time.LocalTime;
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
@Table(name = "classroom_sessions")
public class ClassRoomSessions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long csId;
	
	@ManyToOne
	@JoinColumn(name = "classroomId")
    private ClassRoom classroomId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User userId;
	
	private String loginEmail;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private boolean active;
}
