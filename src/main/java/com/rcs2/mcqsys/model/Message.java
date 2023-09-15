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
@Table(name = "message")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	
	@ManyToOne
	@JoinColumn(name = "sentFrom")
    private User sentFrom;
	
	@ManyToOne
	@JoinColumn(name = "sentTo")
    private User sentTo;
		
	private LocalDate sentDate;
	private LocalTime sentTime;
	private String message;
	private boolean readStatus;
}
