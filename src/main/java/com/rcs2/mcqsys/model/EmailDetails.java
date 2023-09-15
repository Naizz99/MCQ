package com.rcs2.mcqsys.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDetails {

	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
	
	public EmailDetails(String recipient,String subject,String msgBody){
		this.recipient = recipient;
		this.msgBody = msgBody;
		this.subject = subject;
	}
	
}

