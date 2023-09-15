package com.rcs2.mcqsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.repository.EmailService;

@RestController
public class EmailController {

 @Autowired 
 private EmailService emailService;

 @GetMapping("/EmailForm")
public ModelAndView viewAddPaper() {

	ModelAndView mav = new ModelAndView("email-form");
	
	EmailDetails newEmail = new EmailDetails();
	
	mav.addObject("newEmail", newEmail);
		    
	return mav;
}
 
 @GetMapping("/secure/saveMail")
 public String saveMail()
 {
	 
	 /*
     String status = emailService.sendSimpleMail(details);
     return status;
     */
	 return null;
 }
 
 @GetMapping("/secure/sendMail")
 public void sendMail(@ModelAttribute EmailDetails details)
 {
	 	
     emailService.sendMail(details);
 }

 @GetMapping("/sendMailWithAttachment")
 public void sendMailWithAttachment(@RequestBody EmailDetails details)
 {
     emailService.sendMailWithAttachment(details);
 }
}