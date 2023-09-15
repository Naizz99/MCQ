package com.rcs2.mcqsys.service;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rcs2.mcqsys.exception.GlobalExceptionHandler;
import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.repository.EmailService;
 
@Service
public class EmailServiceImpl implements EmailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @Autowired private JavaMailSender javaMailSender;
 
    @Value("${spring.mail.username}") private String sender;
 
    public void sendMail(EmailDetails details)
    {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            LOGGER.info("Mail Sent Successfully...");
        }

        catch (Exception e) {
        	
        	LOGGER.error("Error while Sending Mail");
        }
    }
 

    public void sendMailWithAttachment(EmailDetails details){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
 
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                details.getSubject());
            
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
 
            mimeMessageHelper.addAttachment(
                file.getFilename(), file);

            javaMailSender.send(mimeMessage);
            LOGGER.info("Mail Sent Successfully...");
        }

        catch (MessagingException e) {
        	LOGGER.error("Error while Sending Mail");
        }
    }
}
