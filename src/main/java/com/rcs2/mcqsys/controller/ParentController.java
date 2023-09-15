package com.rcs2.mcqsys.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.rcs2.mcqsys.dto.ParentStudentDto;
import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.model.Login;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.StudentParent;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.repository.EmailService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.StudentParentService;
import com.rcs2.mcqsys.service.StudentService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class ParentController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private StudentService studentService;
		
	@Autowired 
	private StudentParentService studentParentService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired 
	private EmailService emailService;
	
	@Autowired 
	private NotificationService notificationService;
	
	EmailDetails email;
	Notification notification;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	@GetMapping("/getStudentsByEmail")
	public ResponseEntity<List<ParentStudentDto>> getStudentsByEmail(@RequestParam String input,@RequestParam Long parentId,HttpSession session) {
		
		List<ParentStudentDto> studentList = new ArrayList<ParentStudentDto>();
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		int countWithEmail = userService.getCountByEmail(input);
		int countWithMobile = userService.getCountByMobile(input);
						
		if((countWithEmail > 0) && (countWithMobile <= 0)) {
			
			List<User> students = userService.getStudentListByEmail(input);
			for(User u:students) {
				ParentStudentDto psDto = new ParentStudentDto();
				psDto.setUserId(u.getUserId());
				psDto.setStudentId(studentService.getStudentByUserId(u.getUserId()).getStudentId());
				psDto.setName(u.getName());
				psDto.setEmail(u.getEmail());
				psDto.setParentEmail(userService.getById(parentId).getEmail());
				psDto.setMobile(u.getMobile());
				psDto.setGrade(studentService.getStudentByUserId(u.getUserId()).getGrade().getGradeName());
				if(studentParentService.getParentCountByStudent(u.getUserId()) > 0) {
					psDto.setLinked(true);
				}else {
					psDto.setLinked(false);
				}
				studentList.add(psDto);
			}
						
			/*
			 if(loggedRoles.contains(roleService.getByRole("STUDENT"))) {
				ParentStudentDto psDto = new ParentStudentDto();
				psDto.setUserId(user.getUserId());
				psDto.setName(user.getName());
				psDto.setEmail(user.getEmail());
				psDto.setParentEmail(userService.getById(parentId).getEmail());
				psDto.setMobile(user.getMobile());
				psDto.setGrade(studentService.getStudentByUserId(user.getUserId()).getGrade().getGradeName());
				if(studentParentService.getCount(parentId, user.getUserId()) > 0) {
					psDto.setLinked(true);
				}else {
					psDto.setLinked(false);
				}
				studentList.add(psDto);
			}else if(loggedRoles.contains(roleService.getByRole("PARENT"))) {
				for(StudentParent sp : studentParentService.listAllByParent(parentId)) {
					ParentStudentDto psDto = new ParentStudentDto();
					psDto.setUserId(sp.getStudentId().getUserId());
					psDto.setName(sp.getStudentId().getName());
					psDto.setEmail(sp.getStudentId().getEmail());
					psDto.setParentEmail(userService.getById(parentId).getEmail());
					psDto.setMobile(sp.getStudentId().getMobile());
					psDto.setGrade(studentService.getStudentByUserId(user.getUserId()).getGrade().getGradeName());
					psDto.setLinked(true);
					
					studentList.add(psDto);
				}
			}
			 */
			
		}else if((countWithEmail <= 0) && (countWithMobile > 0)){
			List<User> users = userService.getStudentsByMobile(input);
			
			for(User u : users) {
				ParentStudentDto psDto = new ParentStudentDto();
				psDto.setUserId(u.getUserId());
				psDto.setStudentId(studentService.getStudentByUserId(u.getUserId()).getStudentId());
				psDto.setName(u.getName());
				psDto.setEmail(u.getEmail());
				psDto.setParentEmail(userService.getById(parentId).getEmail());
				psDto.setMobile(u.getMobile());
				psDto.setGrade(studentService.getStudentByUserId(u.getUserId()).getGrade().getGradeName());
				if(studentParentService.getCount(parentId, u.getUserId()) > 0) {
					psDto.setLinked(true);
				}else {
					psDto.setLinked(false);
				}
				studentList.add(psDto);
				
				/*
				 * if(loggedRoles.contains(roleService.getByRole("STUDENT"))) {
					ParentStudentDto psDto = new ParentStudentDto();
					psDto.setUserId(user.getUserId());
					psDto.setName(user.getName());
					psDto.setEmail(user.getEmail());
					psDto.setParentEmail(userService.getById(parentId).getEmail());
					psDto.setMobile(user.getMobile());
					psDto.setGrade(studentService.getStudentByUserId(user.getUserId()).getGrade().getGradeName());
					if(studentParentService.getCount(parentId, user.getUserId()) > 0) {
						psDto.setLinked(true);
					}else {
						psDto.setLinked(false);
					}
					
					studentList.add(psDto);
				}else if(loggedRoles.contains(roleService.getByRole("PARENT"))) {
					for(StudentParent sp : studentParentService.listAllByParent(parentId)) {
						ParentStudentDto psDto = new ParentStudentDto();
						psDto.setUserId(sp.getStudentId().getUserId());
						psDto.setName(sp.getStudentId().getName());
						psDto.setEmail(sp.getStudentId().getEmail());
						psDto.setParentEmail(userService.getById(parentId).getEmail());
						psDto.setMobile(sp.getStudentId().getMobile());
						psDto.setGrade(studentService.getStudentByUserId(sp.getStudentId().getUserId()).getGrade().getGradeName());
						psDto.setLinked(true);
						
						studentList.add(psDto);
					}
				}
				 */
			}
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(studentList,HttpStatus.OK);
	}
	
	@GetMapping("/searchStudent")
	public ResponseEntity<Student> searchStudent(@RequestParam long studentId) {
		return new ResponseEntity<>(studentService.get(studentId),HttpStatus.OK) ;		
	}
	
	@GetMapping("/confirmStudent")
	public ResponseEntity<String> confirmStudent(@RequestParam String studentEmail,@RequestParam String parentEmail,HttpSession session) {
		
		if(!studentEmail.equals("")) {
			User user = userService.getByEmail(studentEmail);
			String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
			
			email = new EmailDetails(
					user.getEmail(),
					("QuizzMart - One Time Password"),
					("Dear Student, \n\n Use this otp to connect with parent. \n\n  " + otp));
			emailService.sendMail(email);
			
			notification = new Notification(
					user,
					("QuizzMart - One Time Password"),
					("Use this otp to connect with parent. " + otp));
			notificationService.save(notification,session);
			
			return new ResponseEntity<>(otp, HttpStatus.OK);
		}else if((studentEmail.equals("")) && !(parentEmail.equals(null))) {
			User parentUser = userService.getByEmail(parentEmail);
			String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
			
			email = new EmailDetails(
					parentUser.getEmail(),
					("QuizzMart - One Time Password"),
					("Dear Student, \n\n Use this otp to connect with parent. \n\n  " + otp));
			emailService.sendMail(email);
			
			notification = new Notification(
					parentUser,
					("QuizzMart - One Time Password"),
					("Use this otp to connect with parent. " + otp));
			notificationService.save(notification,session);
					
			return new ResponseEntity<>(otp, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/linkStudent")
	public ResponseEntity<String> linkStudent(@RequestParam Long parentId , @RequestParam Long studentId , @RequestParam String email) {
		
		int count = studentParentService.getCount(parentId,studentId);
		int userCount = userService.getCountByEmail(email);
		
		User user = new User();
		if(userCount > 0) {
			user = userService.getByEmail(email);
		}else {
			user = userService.getById(parentId);
		}
		
		if(count <= 0) {
			User parent = userService.getById(parentId);
			User student = userService.getById(studentId);
			
			StudentParent std = new StudentParent();
			std.setParentId(parent);
			std.setStudentId(student);
			std.setActive(true);
			std.setCreateBy(user.getUsername());
			std.setCreateDate(LocalDate.now());
			std.setUpdateBy(user.getName());
			std.setUpdateDate(LocalDate.now());
			studentParentService.save(std);
			
			return new ResponseEntity<>("Success",HttpStatus.OK) ;
		}else {
			return new ResponseEntity<>("Dupplicate",HttpStatus.OK) ;
		}
	}
	
	@GetMapping("/unLinkStudent")
	public ResponseEntity<String> unLinkStudent(@RequestParam Long parentId , @RequestParam Long studentId) {
		
		StudentParent sp = studentParentService.getByStudentAndParent(parentId,studentId);
		
		studentParentService.delete(sp.getStudentParentId());
		
		return new ResponseEntity<>("Success",HttpStatus.OK) ;
	}
}




