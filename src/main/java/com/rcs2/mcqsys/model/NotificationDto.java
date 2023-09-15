package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class NotificationDto {

	private Long ntfId;
	private User userId;
	private LocalDate receiveDate;
	private LocalTime receiveTime;
	private String topic;
	private String subNotification;
	private String notification;
	private boolean readStatus;
}
