package com.rcs2.mcqsys.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.model.PasswordPolicy;
import com.rcs2.mcqsys.model.Profile;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.UserRole;
import com.rcs2.mcqsys.repository.EmailService;
import com.rcs2.mcqsys.service.ClassRoomPackageService;
import com.rcs2.mcqsys.service.LecturerService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.PaperAnswerService;
import com.rcs2.mcqsys.service.PaperBundleService;
import com.rcs2.mcqsys.service.PaperPackageService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.PasswordPolicyService;
import com.rcs2.mcqsys.service.PasswordService;
import com.rcs2.mcqsys.service.ProfileService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.UserRoleService;
import com.rcs2.mcqsys.service.UserService;
 
@Controller
public class LecturerController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LecturerService lecturerService;
	
	@Autowired
	private PaperService paperService;
	
	@Autowired
	private PaperBundleService paperBundleService;
	
	@Autowired
	private PaperQuestionService paperQuestionService;
	
	@Autowired
	private PaperAnswerService paperAnswerService;
	
	@Autowired
	private PaperPackageService paperPackageService;
	
	@Autowired
	private ClassRoomPackageService classRoomPackageService;

	@Autowired 
	private PasswordService passwordService;
	
	@Autowired 
	private PasswordPolicyService passwordPolicyService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;
		
	@Autowired
	private ProfileService profileService;
	
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
	
	@RequestMapping("/listLecturers")
	public String listPapers(Model model,HttpSession session) {
		User user = (User)session.getAttribute("user");
		 model.addAttribute("user", user);
		
		List<Lecturer> listLecturers = lecturerService.listAll();
	    model.addAttribute("listLecturers", listLecturers);
	     
	    return "user/lecturer-list";
	}
	
	@GetMapping("/addLecturer")
	public ModelAndView viewAddStudent(HttpSession session) {
		ModelAndView mav = new ModelAndView("user/lecturer-form");
				
		User loggedUser = (User)session.getAttribute("user");
		mav.addObject("user", loggedUser);
		
		Lecturer lecturer = new Lecturer();
				
		User lecturerUser = new User();
		lecturerUser.setCreateBy(loggedUser.getUsername());
		lecturerUser.setCreateDate(LocalDate.now());
		lecturerUser.setUpdateBy(loggedUser.getUsername());
		lecturerUser.setUpdateDate(LocalDate.now());
		lecturerUser.setForcePassword(true);
		lecturerUser.setSso(false);
		lecturerUser.setSsoEmail(null);
		lecturer.setUserId(lecturerUser);
		mav.addObject("lecturer", lecturer);
		
		//Get all policies
  		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
  		mav.addObject("listPolicies", listPolicies);
  		
		return mav;
	}
	
	@GetMapping("/showLecturerUpdate")
	public ModelAndView showLecturerUpdate(@RequestParam Long lecturerId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("user/lecturer-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		Lecturer lecturer = lecturerService.get(lecturerId);
		
		lecturer.getUserId().setUpdateBy(user.getName());
		lecturer.getUserId().setUpdateDate(LocalDate.now());
		
		mav.addObject("lecturer", lecturer);
		
		//Get all policies
  		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
  		mav.addObject("listPolicies", listPolicies);
  		
		return mav;
	}
	
	@PostMapping("/secure/saveLecturer")
	public ResponseEntity<String> saveLecturer(@ModelAttribute Lecturer lecturer,HttpSession session) {
		boolean isDupplicate = false;
				
		List<Lecturer> listLecturer = lecturerService.listAll();
		for (Lecturer selectedLecturer : listLecturer) 
		{
			if((selectedLecturer.getUserId().getUserId()).equals(lecturer.getUserId().getUserId())) {
				
				isDupplicate = true;
				break;
			}
		}
		if(!isDupplicate) {
			lecturer.getUserId().setUpdateDate(LocalDate.now());	
			userService.save(lecturer.getUserId());
			
			UserRole uRole = new UserRole();
			uRole.setUserId(lecturer.getUserId());
			uRole.setRoleId(roleService.getByRole("LECTURER"));
			userRoleService.save(uRole);
			
			Profile profile = new Profile();
			profile.setUser(lecturer.getUserId());
			profile.setCoverImage(null);
			profile.setProfilePic(null);
			profileService.save(profile);
			
			Password password = new Password();
			password.setUserId(lecturer.getUserId());
			password.setPassword(passwordEncoder.encode("1qaz@WSX"));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			lecturerService.save(lecturer);	
			
			email = new EmailDetails(
					lecturer.getUserId().getEmail(),
					("Lecturer Registration - " + lecturer.getUserId().getName()),
					("Dear Sir/Madam, \n\n You has been registered to the QuizzMart successfully. Please use following details to login to system using http://localhost:8081/login. \n\n Username : " + lecturer.getUserId().getName() + " \n Email : " + lecturer.getUserId().getEmail() + " \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					lecturer.getUserId(),
					("Lecturer Registration - " + lecturer.getUserId().getName()),
					("Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
			notificationService.save(notification,session);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {	
			if((userService.getCountByUserId(lecturer.getUserId().getUserId()) > 0) && (((lecturerService.getLecturerCountById(lecturer.getLecturerId())) > 0))) {
				
				lecturer.getUserId().setUpdateDate(LocalDate.now());
				userService.save(lecturer.getUserId());
				
				Lecturer lec = lecturerService.get(lecturer.getLecturerId());
				lec.setNicNumber(lecturer.getNicNumber());
				lec.setInstituteName(lecturer.getInstituteName());
				lec.setEducationQualification(lecturer.getEducationQualification());
				lec.setDescription(lecturer.getDescription());
				lec.setUserId(lecturer.getUserId());
				lecturerService.save(lec);	
				
				return new ResponseEntity<>("success", HttpStatus.OK);
			}else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}			
		}
	}
	
	@GetMapping("/deleteLecturer")
	public ResponseEntity<String> deleteLecturer(@RequestParam Long lecturerId,@RequestParam Long userId) {
				
		passwordService.delete(passwordService.getPasswordByUser(userId).getUpId());
		
		lecturerService.delete(lecturerId);
		
		for(UserRole ur : userRoleService.listByUser(userId)) {
			userRoleService.delete(ur.getUrId());
		}
		profileService.delete(profileService.getByUserId(userId).getProfileId());
		userService.deleteById(userId);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	
	@GetMapping("/lecturerDeactive")
	public String lecturerDeactive(@RequestParam Long userId) {
		
		User user = userService.getById(userId);
		user.setActive(false);
		
		userService.save(user);
		
		return "redirect:/listLecturers";
	}
	
	@GetMapping("/lecturerActive")
	public String lecturertActive(@RequestParam Long userId) {
		
		User user = userService.getById(userId);
		user.setActive(true);
		
		userService.save(user);
		
		return "redirect:/listLecturers";
	}
}
