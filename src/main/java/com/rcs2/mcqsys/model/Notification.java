
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
@Table(name = "notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ntfId;
				
	@ManyToOne
	@JoinColumn(name = "userId")
    private User userId;
	
	private LocalDate receiveDate;
	private LocalTime receiveTime;
	private String topic;
	private String notification;
	private boolean readStatus;
	
	public Notification() {
		
	}
	
	public Notification(User userId,String topic,String notification) {
		this.userId = userId;
		this.topic = topic;
		this.notification = notification;
		this.receiveDate = LocalDate.now();
		this.receiveTime = LocalTime.now();
		this.readStatus = false;
	}
}
