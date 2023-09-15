package com.rcs2.mcqsys.repository;

import com.rcs2.mcqsys.model.EmailDetails;

public interface EmailService {

	 void sendMail(EmailDetails details);
	
	 void sendMailWithAttachment(EmailDetails details);
}
