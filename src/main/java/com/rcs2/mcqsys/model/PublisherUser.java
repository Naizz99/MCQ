package com.rcs2.mcqsys.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import groovy.transform.ToString;
import lombok.Data;

@Data
@Entity
@Table(name = "publisher_user")
@ToString
public class PublisherUser{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pbUserId;
	
	@ManyToOne 
	@JoinColumn(name = "poId")
	private Publisher poId;
	
	@ManyToOne 
	@JoinColumn(name = "userId")
	private User userId;
	
	@ManyToOne 
	@JoinColumn(name = "role")
	private Role role;
	
}
