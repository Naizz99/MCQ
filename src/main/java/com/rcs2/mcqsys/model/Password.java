package com.rcs2.mcqsys.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "password")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Password {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long upId;
	
	@OneToOne 
	@JoinColumn(name = "userId")
	private User userId;
	
	private String password;
	
	private LocalDate lastUpdated;
	private boolean active; 
}
