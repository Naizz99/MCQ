package com.rcs2.mcqsys.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rcs2.mcqsys.model.Login;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.repository.NotificationRepository;

@Service
@Transactional
public class NotificationService {
	 @Autowired
	 private NotificationRepository repo;
	  
	 @Autowired
	 private SimpMessagingTemplate webSoketTemplate;
	 
	 @Autowired
	 private NotificationService notificationService;
	 
	 public List<Notification> listAll() {
	     return repo.findAll();
	 }
	 
	 public List<Notification> listAllUnread(Long userId) {
	     return repo.listAllUnread(userId);
	 }
	  
	 public void save(Notification notification,HttpSession session) {
	     repo.save(notification);
	     
	     int unreadNotificationCount = notificationService.unreadNotificationCountByReceiver(notification.getUserId().getUserId());
		 //Login.unreadNotificationCount = unreadNotificationCount;
		 session.setAttribute("unreadNotificationCount", notificationService.unreadNotificationCountByReceiver(notification.getUserId().getUserId()));
		 
		 webSoketTemplate.convertAndSend("/topic/updateNotifications",unreadNotificationCount);	
	 }
	  
	 public Notification get(long id) {
	     return repo.findById(id).get();
	 }
	  
	 public void delete(long id) {
	     repo.deleteById(id);
	 }

	 public List<Notification> listAll(Long userId) {
	     return repo.listAll(userId);
	 }

	public int unreadNotificationCountByReceiver(Long userId) {
		return repo.unreadNotificationCountByReceiver(userId);
	}
}
