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
@Table(name = "classroom_messages")
public class ClassRoomMessages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cmId;
	
	@ManyToOne
	@JoinColumn(name = "classroomId")
    private ClassRoom classroomId;
	
	private String sentFrom;
	
	private String sentTo;
	
	private String name;
	
	private LocalDate sentDate;
	private LocalTime sentTime;
	private String message;
	private boolean readStatus;
	
}
