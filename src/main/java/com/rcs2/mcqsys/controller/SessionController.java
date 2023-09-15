package com.rcs2.mcqsys.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rcs2.mcqsys.model.ActiveSessions;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.ActiveSessionsService;

public class SessionController {
	
	@Autowired
	private ActiveSessionsService activeSessionService;
	
	@RequestMapping("/listActiveSessions")
	public String listActiveSessions(Model model,HttpSession session) {
		User user = (User)session.getAttribute("user");
		 model.addAttribute("user", user);
		
		List<ActiveSessions> listSessions = activeSessionService.listAll();
	    model.addAttribute("listSessions", listSessions);
	    
	    return "services/session-list";
	}
}
