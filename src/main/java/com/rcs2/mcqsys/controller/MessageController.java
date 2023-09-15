package com.rcs2.mcqsys.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.config.MCQAuthenticationSuccessHandler;
import com.rcs2.mcqsys.dto.ChatDto;
import com.rcs2.mcqsys.dto.ClassRoomMembers;
import com.rcs2.mcqsys.dto.MessageDto;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoomMessages;
import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.model.Login;
import com.rcs2.mcqsys.model.Message;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.NotificationDto;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.CartService;
import com.rcs2.mcqsys.service.ClassRoomMessagesService;
import com.rcs2.mcqsys.service.ClassRoomService;
import com.rcs2.mcqsys.service.ClassRoomStudentService;
import com.rcs2.mcqsys.service.MessageService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class MessageController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClassRoomMessagesService classRoomMessagesService;
	
	@Autowired
	private ClassRoomService classRoomService;
	
	@Autowired
	private ClassRoomStudentService classRoomStudentService;
	
	@Autowired
	private SimpMessagingTemplate webSoketTemplate;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private NotificationService notificationService;
	
	@GetMapping("/listMessages")
	public ModelAndView listMessages(HttpSession session) {	
		User loggedUser = (User)session.getAttribute("user");
		ModelAndView mav = new ModelAndView("user/messages");
		List<ChatDto> chatList = new ArrayList();
		List<Message> listMessages = messageService.allSenders(loggedUser.getUserId());
		for(Message c : listMessages) {
			ChatDto chat = new ChatDto();
			if(c.getSentFrom().equals(loggedUser.getUserId())) {
				chat.setUser(c.getSentTo());
			}else {
				chat.setUser(c.getSentFrom());
			}
			chat.setReadStatus(c.isReadStatus());
			chat.setDate(c.getSentDate());
			chat.setTime(c.getSentTime());
			chat.setLastMessage(messageService.getLastMessage(loggedUser.getUserId(),chat.getUser().getUserId()));
			chatList.add(chat);
		}
		mav.addObject("chatList", chatList);
		mav.addObject("user", loggedUser);
		int unreadMsgCount = messageService.unreadMessageCountByReceiver(loggedUser.getUserId());
		mav.addObject("unreadMsgCount", unreadMsgCount);
		List<User> users = userService.findAllByRole("STUDENT");
		mav.addObject("users", users);
		return mav;
	}
	
	//Classroom messages
	@GetMapping("/showClassRoomMessages")
	public ModelAndView showClassRoomMessages(@RequestParam long classroomId,@RequestParam String email) {		
		
		ModelAndView mav = new ModelAndView("classroom/classroom-messages");
				
		List<ChatDto> chatList = new ArrayList();
		List<ClassRoomMessages> listMessages = classRoomMessagesService.allSenders(email,classroomId);
		for(ClassRoomMessages c : listMessages) {
			ChatDto chat = new ChatDto();
			if(c.getSentFrom().equals(email)) {
				chat.setEmail(c.getSentTo());
			}else {
				chat.setEmail(c.getSentFrom());
			}
			chat.setReadStatus(c.isReadStatus());
			chat.setDate(c.getSentDate());
			chat.setTime(c.getSentTime());
			chat.setLastMessage(classRoomMessagesService.getLastMessage(classroomId,email,chat.getEmail()));
			chatList.add(chat);
		}
		mav.addObject("chatList", chatList);
		
		mav.addObject("email", email);
		
		List<ClassRoomMembers> members = new ArrayList();
		List<ClassRoomStudent> listStudents = classRoomStudentService.listAllByClassRoom(classroomId);
		for(ClassRoomStudent cStd : listStudents) {
			ClassRoomMembers mb = new ClassRoomMembers();
			mb.setClassroomId(cStd.getClassRoomId().getClassroomId());
			mb.setUserId(cStd.getStudentId().getUserId().getUserId());
			mb.setEmail(cStd.getStudentId().getUserId().getEmail());
			mb.setName(cStd.getStudentId().getUserId().getName());
			
			members.add(mb);
		}
		ClassRoomMembers mb = new ClassRoomMembers();
		mb.setClassroomId(classroomId);
		mb.setEmail(classRoomService.get(classroomId).getLecturerId().getUserId().getEmail());
		mb.setName(classRoomService.get(classroomId).getLecturerId().getUserId().getName());
		members.add(mb);
		mav.addObject("members", members);
		
		int unreadMsgCount = classRoomMessagesService.unreadMessageCountByReceiver(email);
		mav.addObject("unreadMsgCount", unreadMsgCount);
  	    
		ClassRoom classroom = classRoomService.get(classroomId);
		mav.addObject("classroom", classroom);
		
		return mav;
	}
	
	@GetMapping("/getMessages")
	public ResponseEntity<List<MessageDto>> getMessages(@RequestParam String sentFrom , @RequestParam String sentTo) {		
		
		List<MessageDto> listMessages = new ArrayList();
		
		List<ClassRoomMessages> messages = classRoomMessagesService.listAll(sentFrom,sentTo);
		for(ClassRoomMessages cm : messages) {
			MessageDto message = new MessageDto();
			
			message.setCmId(cm.getCmId());
			message.setClassroomId(cm.getClassroomId().getClassroomId());
			message.setUserName(cm.getName());
			message.setSentDate(cm.getSentDate());
			message.setSentTime(cm.getSentTime());
			message.setMessage(cm.getMessage());
			message.setReadStatus(cm.isReadStatus());
			
			if(cm.getSentTo().equals(sentFrom)) {
				message.setType("incoming");
				cm.setReadStatus(true);
				classRoomMessagesService.save(cm);
			}else {
				message.setType("outgoing");
			}
			listMessages.add(message);
		}
		
		return new ResponseEntity<>(listMessages, HttpStatus.OK);
	}
	
	@PostMapping("/secure/sendMessages")
	public ResponseEntity<ClassRoomMessages> sendMessages(@RequestParam String message,@RequestParam String sentFrom,@RequestParam String sentTo,@RequestParam long classroomId) {		
		
		ClassRoomMessages msg = new ClassRoomMessages();
		msg.setClassroomId(classRoomService.get(classroomId));
		msg.setReadStatus(false);
		msg.setMessage(message);
		msg.setSentDate(LocalDate.now());
		msg.setSentTime(LocalTime.now());
		msg.setSentFrom(sentFrom);
		msg.setSentTo(sentTo);
		msg.setName(sentFrom);
		
		classRoomMessagesService.save(msg);
	
		webSoketTemplate.convertAndSend("/topic/newMessage",msg);
		
		ResponseEntity<ClassRoomMessages> x = new ResponseEntity<>(msg, HttpStatus.OK);
		return x ;
	}
	
	//Notification Management
	
	@GetMapping("/listNotifications")
	public ModelAndView listNotifications(HttpSession session) {	
		User loggedUser = (User)session.getAttribute("user");
		ModelAndView mav = new ModelAndView("user/notifications");

		List<Notification> listNotifications = notificationService.listAll(loggedUser.getUserId());
		mav.addObject("listNotifications", listNotifications);
		
		List<NotificationDto> notificationList = new ArrayList<>();
		for(Notification n:listNotifications) {
			NotificationDto nn = new NotificationDto();
			nn.setNtfId(n.getNtfId());
			nn.setTopic(n.getTopic());
			nn.setNotification(n.getNotification());
			nn.setSubNotification(n.getNotification().substring(0,25) + "....read more");
			nn.setReadStatus(n.isReadStatus());
			nn.setReceiveDate(n.getReceiveDate());
			nn.setReceiveTime(n.getReceiveTime());
			nn.setUserId(n.getUserId());
			notificationList.add(nn);
		}
		mav.addObject("notificationList", notificationList);
		
		mav.addObject("user", loggedUser);
		
		return mav;
	}
	
	@GetMapping("/readNotification")
	public ResponseEntity<String> readNotification(@RequestParam Long ntfId,HttpSession session) {		
		User loggedUser = (User)session.getAttribute("user");
		
		Notification ntf = notificationService.get(ntfId);
		ntf.setReadStatus(true);
		notificationService.save(ntf,session);

		int unreadNotificationCount = notificationService.unreadNotificationCountByReceiver(loggedUser.getUserId());
		//Login.unreadNotificationCount = unreadNotificationCount;
		session.setAttribute("unreadNotificationCount", unreadNotificationCount);
		
		webSoketTemplate.convertAndSend("/topic/updateNotifications",unreadNotificationCount);
		
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x ;
	}
	
	@GetMapping("/getNotificationCount")
	public ResponseEntity<Integer> getNotificationCount(HttpSession session) {		
		User loggedUser = (User)session.getAttribute("user");
		
		int unreadNotificationCount = notificationService.unreadNotificationCountByReceiver(loggedUser.getUserId());
		//Login.unreadNotificationCount = unreadNotificationCount;
		session.setAttribute("unreadNotificationCount", unreadNotificationCount);	
		
		ResponseEntity<Integer> x = new ResponseEntity<>(unreadNotificationCount, HttpStatus.OK);
		return x ;
	}
	
	@GetMapping("/getMessageCount")
	public ResponseEntity<Integer> getMessageCount(HttpSession session) {		
		User loggedUser = (User)session.getAttribute("user");
		
		int unreadMsgCount = messageService.unreadMessageCountByReceiver(loggedUser.getUserId());
		//Login.unreadMsgCount = unreadMsgCount;
		session.setAttribute("unreadMsgCount", unreadMsgCount);
		
		ResponseEntity<Integer> x = new ResponseEntity<>(unreadMsgCount, HttpStatus.OK);
		return x ;
	}
	
}



















