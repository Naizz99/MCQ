package com.rcs2.mcqsys.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.SystemOutLogger;
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
import com.rcs2.mcqsys.dto.ForgottenPasswordDto;
import com.rcs2.mcqsys.dto.LecturerRegisterDto;
import com.rcs2.mcqsys.dto.ProfileDto;
import com.rcs2.mcqsys.dto.StudentRegisterDto;
import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.Login;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.model.PasswordPolicy;
import com.rcs2.mcqsys.model.Profile;
import com.rcs2.mcqsys.model.Provider;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.model.StudentParent;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.UserRole;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.RoleFunction;
import com.rcs2.mcqsys.repository.EmailService;
import com.rcs2.mcqsys.service.ClassRoomStudentService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.LecturerService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.PasswordPolicyService;
import com.rcs2.mcqsys.service.PasswordService;
import com.rcs2.mcqsys.service.ProfileService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleFunctionService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.StudentPaperReviewDetailService;
import com.rcs2.mcqsys.service.StudentPaperReviewService;
import com.rcs2.mcqsys.service.StudentParentService;
import com.rcs2.mcqsys.service.StudentService;
import com.rcs2.mcqsys.service.UserRoleService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class UserController {		
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private StudentParentService studentParentService;
	
	@Autowired
	private ClassRoomStudentService classRoomStudentService;
	
	@Autowired
	private LecturerService lecturerService;
	
	@Autowired
    private PublisherService publisherService;
	
	@Autowired
    private PublisherUserService publisherUserService;
	
	@Autowired 
	private StudentPaperReviewService studentPaperReviewService;
	
	@Autowired 
	private StudentPaperReviewDetailService studentPaperReviewDetailService;
	
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
	private RoleFunctionService roleFunctionService;
	
	@Autowired 
	private EmailService emailService;
	
	@Autowired 
	private NotificationService notificationService;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	Password password = new Password();
	UserRole uRole = new UserRole();
	EmailDetails email;
	Notification notification;
	
	@RequestMapping("/registration")
	public ModelAndView registration(Model model) {
		ModelAndView mav = new ModelAndView("registration");
		
		User user = new User();
		mav.addObject("user", user);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);
		
		//Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
	    
	    return mav;
	}
	
	@RequestMapping("/registerPublisher")
	public ModelAndView registerPublisher(Model model) {
		ModelAndView mav = new ModelAndView("register-publisher");
		
		User user = new User();
		mav.addObject("user", user);
		
	    return mav;
	}
	
	@PostMapping("/secure/registerStudent")
	public ResponseEntity<Object> registerStudent(@ModelAttribute("student") StudentRegisterDto student,HttpSession session) {
		
		//Save Student
		User stdUser = new User();
		stdUser.setActive(true);
		stdUser.setEmail(student.getStdEmail());
		stdUser.setGender(student.getStdGender());
		stdUser.setMobile(student.getStdMobile());
		stdUser.setName(student.getStdName());
		stdUser.setUsername(student.getStdUsername());
		stdUser.setCreateBy(student.getStdUsername());
		stdUser.setCreateDate(LocalDate.now());
		stdUser.setUpdateBy(student.getStdUsername());
		stdUser.setUpdateDate(LocalDate.now());
		stdUser.setForcePassword(false);
		stdUser.setSso(false);
		stdUser.setSsoEmail(null);
		userService.save(stdUser);
				
		password = new Password();
		password.setUserId(stdUser);
		password.setPassword(passwordEncoder.encode(student.getStdPassword()));
		password.setActive(true);
		password.setLastUpdated(LocalDate.now());
		passwordService.save(password);
				
		uRole = new UserRole();
		uRole.setUserId(stdUser);
		uRole.setRoleId(roleService.getByRole("STUDENT"));
		userRoleService.save(uRole);
				
		Profile profile = new Profile();
		profile.setUser(stdUser);
		profile.setCoverImage(null);
		profile.setProfilePic(null);
		profileService.save(profile);	
		
		Student std = new Student();
		std.setUserId(stdUser);
		std.setGpa(Double.parseDouble(student.getStdGPA()));
		std.setGrade(gradeService.get(Long.parseLong(student.getStdGrade())));
		if(std.getAttemptsAllowed() == 0) {
			std.setAttemptsAllowed(3);
		}
		studentService.save(std);
			
		email = new EmailDetails(
				student.getStdEmail(),
				("Student Registration - " + student.getStdName()),
				("Dear Student, \n\n Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
		emailService.sendMail(email);
		
		notification = new Notification(
				stdUser,
				("Student Registration - " + student.getStdName()),
				("Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
		notificationService.save(notification,session);
				
		//SaveParent
		User parent = new User();
		if(student.getPrtEmail() != "") {
			if((userService.getParentCountByEmail(student.getPrtEmail()) > 0)) {
				parent = userService.getParentByEmail(student.getPrtEmail());
			}else{
				parent.setActive(true);
				parent.setEmail(student.getPrtEmail());
				parent.setGender(student.getPrtGender());
				parent.setMobile(student.getPrtMobile());
				parent.setName(student.getPrtName());
				parent.setUsername(student.getPrtEmail());
				parent.setCreateBy(student.getStdUsername());
				parent.setCreateDate(LocalDate.now());
				parent.setUpdateBy(student.getStdUsername());
				parent.setUpdateDate(LocalDate.now());
				parent.setForcePassword(true);
				parent.setSso(false);
				parent.setSsoEmail(null);
				userService.save(parent);
				
				uRole = new UserRole();
				uRole.setUserId(parent);
				uRole.setRoleId(roleService.getByRole("PARENT"));
				userRoleService.save(uRole);
							
				password = new Password();
				password.setUserId(parent);
				password.setPassword(passwordEncoder.encode("1qaz@WSX"));
				password.setActive(true);
				password.setLastUpdated(LocalDate.now());
				passwordService.save(password);
				
				profile = new Profile();
				profile.setUser(parent);
				profile.setCoverImage(null);
				profile.setProfilePic(null);
				profileService.save(profile);
			}
			
			StudentParent stdParent = new StudentParent();
			stdParent.setParentId(parent);
			stdParent.setStudentId(stdUser);
			stdParent.setActive(true);
			stdParent.setCreateBy(student.getStdUsername());
			stdParent.setCreateDate(LocalDate.now());
			stdParent.setUpdateBy(student.getStdUsername());
			stdParent.setUpdateDate(LocalDate.now());
			studentParentService.save(stdParent);
			
			email = new EmailDetails(
					student.getPrtEmail(),
					("Student Registration - " + student.getStdName()),
					("Dear Parent, \n\n Your child has been registered to the QuizzMart. Please use following details to login to system and analysis your children using http://localhost:8081/login. \n\n Username : " + student.getPrtEmail() + " \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					parent,
					("Student Registration - " + student.getStdName()),
					("Your child has been registered to the QuizzMart. Please use following details to login to system and analysis your children using http://localhost:8081/login. \n\n Username : " + student.getPrtEmail() + " \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
			notificationService.save(notification,session);
		}
		return new ResponseEntity<>(student, HttpStatus.OK);
		
	}
	
	@PostMapping("/secure/registerLecturer")
	public ResponseEntity<Object> registerLecturer(@ModelAttribute("lecturer") LecturerRegisterDto lecturer,HttpSession session){
				
		User lecUser = new User();
		lecUser.setActive(true);
		lecUser.setEmail(lecturer.getLecEmail());
		lecUser.setGender(lecturer.getLecGender());
		lecUser.setMobile(lecturer.getLecMobile());
		lecUser.setName(lecturer.getLecName());
		lecUser.setUsername(lecturer.getLecUsername());
		lecUser.setCreateBy(lecturer.getLecUsername());
		lecUser.setCreateDate(LocalDate.now());
		lecUser.setUpdateBy(lecturer.getLecUsername());
		lecUser.setUpdateDate(LocalDate.now());
		lecUser.setForcePassword(false);
		lecUser.setSso(false);
		lecUser.setSsoEmail(null);
		userService.save(lecUser);
				
		uRole = new UserRole();
		uRole.setUserId(lecUser);
		uRole.setRoleId(roleService.getByRole("LECTURER"));
		userRoleService.save(uRole);
				
		Password password = new Password();
		password.setUserId(lecUser);
		password.setPassword(passwordEncoder.encode(lecturer.getLecPassword()));
		password.setActive(true);
		password.setLastUpdated(LocalDate.now());
		passwordService.save(password);
				
		Profile profile = new Profile();
		profile.setUser(lecUser);
		profile.setCoverImage(null);
		profile.setProfilePic(null);
		profileService.save(profile);
				
		Lecturer lec = new Lecturer();
		lec.setDescription(lecturer.getDescription());
		lec.setEducationQualification(lecturer.getEducationQualifycation());
		lec.setInstituteName(lecturer.getInstituteName());
		lec.setNicNumber(lecturer.getLecNic());
		lec.setUserId(lecUser);
		lecturerService.save(lec);

		email = new EmailDetails(
				lecturer.getLecEmail(),
				("Lecturer Registration - " + lecturer.getLecName()),
				("Dear Lecturer, \n\n Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
		emailService.sendMail(email);
				
		notification = new Notification(
				lecUser,
				("Lecturer Registration - " + lecturer.getLecName()),
				("Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
		notificationService.save(notification,session);
				
		return new ResponseEntity<>(lecturer, HttpStatus.OK);
			
	}
	
	@PostMapping("/secure/updateUser")
	public ResponseEntity<Object> updateUser(@ModelAttribute User user,HttpSession session) {
		User loggeduser = (User)session.getAttribute("user");
		
		if(user.isSso()) {
			//Password password = passwordService.getPasswordByUser(user.getUserId());
			//password.setPassword(null);
			//passwordService.save(password);
			
			user.setEmail(user.getSsoEmail());
			user.setProvider(Provider.GOOGLE);
		}else {
			user.setSsoEmail(null);
			user.setProvider(null);
		}
		
		user.setUpdateBy(loggeduser.getUsername());
		user.setUpdateDate(LocalDate.now());
		
		userService.save(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PostMapping("/secure/updateProfile")
	public ResponseEntity<Object> updateprofile(@ModelAttribute ProfileDto profileDto,HttpSession session) {
		
		Profile profile = profileService.get(profileDto.getProfileId());

		if(profileDto.getType().equals("new")) {
			profile.setBodyBgColor(profileDto.getBodyBgColor());
			profile.setBodyFontFamily(profileDto.getBodyFontFamily());
			profile.setSideBgColor(profileDto.getSideBgColor());
			profile.setSideMenuTextColor(profileDto.getSideMenuTextColor());
			profile.setSideSubMenuTextColor(profileDto.getSideSubMenuTextColor());
			profile.setFontColor(profileDto.getFontColor());
			profile.setPageTitle(profileDto.getPageTitle());
			profile.setCardBgColor(profileDto.getCardBgColor());
			profile.setCardSideColor1(profileDto.getCardSideColor1());
			profile.setCardSelectedColor(profileDto.getCardSelectedColor());
		}else if(profileDto.getType().equals("default")) {
			profile.setBodyBgColor("#f6f9ff");
			profile.setBodyFontFamily("Nunito, sans-serif");
			profile.setPageTitle("#012970");
			profile.setFontColor("#000");
			profile.setSideBgColor("#ffffff");
			profile.setSideMenuTextColor("#012970");
			profile.setSideSubMenuTextColor("#012970");
			profile.setCardBgColor("#ffffff");
			profile.setCardSideColor1("#e1f0fa");
			profile.setCardSelectedColor("#e1f0fc");
		}		

		profileService.save(profile);

		session.removeAttribute("profile");
		session.setAttribute("profile", profile);
		
		return new ResponseEntity<>(profile, HttpStatus.OK);
	}
		
	@PostMapping("/secure/updateStudent")
	public ResponseEntity<Object> updateStudent(@ModelAttribute Student student,HttpSession session) {
		boolean isDupplicate = false;
		User loggeduser = (User)session.getAttribute("user");
		
		student.getUserId().setUpdateBy(loggeduser.getUsername());
		student.getUserId().setUpdateDate(LocalDate.now());
		
		userService.save(student.getUserId());
		studentService.save(student);
		
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	
	@GetMapping("/secure/searchParent")
	public ResponseEntity<Object> SearchParent(@RequestParam long userId) {
		return new ResponseEntity<>(userService.getById(userId), HttpStatus.OK);
	}
	
	//Admin Management
	
	@RequestMapping("/listAdmins")
	public ModelAndView listAdmins(HttpSession session) { 
	    List<User> listAdmins = userService.findAllByRole("ADMIN");
	    
	    ModelAndView mav = new ModelAndView("user/admin-list");
	    
	    User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
	    mav.addObject("listAdmins", listAdmins);
	    return mav;
	}
	
	@GetMapping("/showAdminUpdate")
	public ModelAndView viewAdminUpdate(@RequestParam Long adminId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("user/admin-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		User admin = userService.getById(adminId);
		admin.setUpdateBy(user.getName());
		admin.setUpdateDate(LocalDate.now());
		mav.addObject("admin", admin);
		
		Password password = passwordService.getPasswordByUser(adminId);
		mav.addObject("password", password);
		
		//Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
		
		return mav;
	}
	
	@GetMapping("/addAdmin")
	public ModelAndView viewAddAdmin(HttpSession session) {
		ModelAndView mav = new ModelAndView("user/admin-form");
		
		User admin = new User();
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		admin.setCreateBy(user.getUsername());
		admin.setCreateDate(LocalDate.now());
		admin.setUpdateBy(user.getUsername());
		admin.setUpdateDate(LocalDate.now());
		admin.setActive(false);
		admin.setForcePassword(true);
		admin.setSso(false);
		admin.setSsoEmail(null);
		mav.addObject("admin", admin);
		
		//Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
		
		return mav;
	}
	
	@PostMapping("/secure/saveAdmin")
	public ResponseEntity<String> saveAdmin(@ModelAttribute User admin,HttpSession session) {
		boolean isDupplicate = false;
				
		List<User> listAdmin = userService.findAllByRole("ADMIN");
		for (User selectedAdmin : listAdmin) 
		{
			if((selectedAdmin.getUserId()).equals(admin.getUserId())) {
				isDupplicate = true;
				break;
			}
		}
		
		if(!isDupplicate) {
			
			admin.setUpdateDate(LocalDate.now());
			userService.save(admin);
			
			uRole = new UserRole();
			uRole.setUserId(admin);
			uRole.setRoleId(roleService.getByRole("ADMIN"));
			userRoleService.save(uRole);
			
			Password password = new Password();
			password.setUserId(admin);
			password.setPassword(passwordEncoder.encode("1qaz@WSX"));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			Profile profile = new Profile();
			profile.setUser(admin);
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
			
			email = new EmailDetails(
					admin.getEmail(),
					("Admin Registration - " + admin.getName()),
					("Dear Sir/Madam, \n\n You has been registered to the QuizzMart successfully. Please use following details to login to system using http://localhost:8081/login. \n\n Username : " + admin.getUsername() + " \n Email : " + admin.getEmail() + " \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					admin,
					("Admin Registration - " + admin.getName()),
					("Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
			notificationService.save(notification,session);
					
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {
			if(userService.getCountByUserId(admin.getUserId()) > 0) {
				admin.setUpdateDate(LocalDate.now());
				userService.save(admin);
				return new ResponseEntity<>("success", HttpStatus.OK);
			}else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
		}
	}
		
	@GetMapping("/deleteAdmin")
	public ResponseEntity<String> deleteAdmin(@RequestParam Long adminId) {
		passwordService.delete(passwordService.getPasswordByUser(adminId).getUpId());
		for(UserRole ur : userRoleService.listByUser(adminId)) {
			userRoleService.delete(ur.getUrId());
		}
		profileService.delete(profileService.getByUserId(adminId).getProfileId());
		userService.deleteById(adminId);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/adminDeactive")
	public String adminDeactive(@RequestParam Long adminId) {
		User admin = userService.getById(adminId);
		admin.setActive(false);
		userService.save(admin);
		return "redirect:/listAdmins";
	}
	
	@GetMapping("/adminActive")
	public String adminActive(@RequestParam Long adminId) {
		User admin = userService.getById(adminId);
		admin.setActive(true);
		userService.save(admin);
		return "redirect:/listAdmins";
	}
	
	//Editor Management
	
	@RequestMapping("/listSystemEditors")
	public ModelAndView listSystemUsers(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("user/editor-list");
		List<PublisherUser> listUsers;
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User loggedUser = (User)session.getAttribute("user");
		mav.addObject("user", loggedUser);
		
    	if(loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
	    	listUsers = publisherUserService.listEditorsByPublisher((publisherUserService.getByUserId(loggedUser.getUserId()).getPoId()));
		}else {
			listUsers = publisherUserService.getAllEditors();
		}
	    
	    mav.addObject("listUsers", listUsers);
	    return mav;
	}
	
	@GetMapping("/showEditorUpdate")
	public ModelAndView showEditorUpdate(@RequestParam Long editorId,HttpSession session) {
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		
		ModelAndView mav = new ModelAndView("user/editor-form");
		
		Publisher publisher = new Publisher();
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			publisher = publisherUserService.getByUserId(user.getUserId()).getPoId();
		}
		mav.addObject("publisher", publisher);
		
		mav.addObject("user", user);
		
		PublisherUser editor = publisherUserService.getByUserId(editorId);
		editor.getUserId().setUpdateBy(user.getName());
		editor.getUserId().setUpdateDate(LocalDate.now());
		mav.addObject("editor", editor);
		
		//Get all paper publishers 
		List<Publisher> listPublishers = publisherService.listAllActive();
	    mav.addObject("publisherList", listPublishers);   
	    
	    //Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
		
		return mav;
		
	}
		
	@GetMapping("/addEditor")
	public ModelAndView addEditor(HttpSession session) {
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		ModelAndView mav = new ModelAndView("user/editor-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		PublisherUser editor = new PublisherUser();
		editor.setUserId(new User());
		editor.getUserId().setUpdateBy(user.getName());
		editor.getUserId().setUpdateDate(LocalDate.now());
		editor.setRole(roleService.getByRole("EDITOR"));
		
		Publisher publisher = new Publisher();
			
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			publisher = publisherUserService.getByUserId(user.getUserId()).getPoId();
			editor.setPoId(publisher);
		}
		mav.addObject("publisher", publisher);
		
		editor.getUserId().setCreateBy(user.getUsername());
		editor.getUserId().setCreateDate(LocalDate.now());
		editor.getUserId().setUpdateBy(user.getUsername());
		editor.getUserId().setUpdateDate(LocalDate.now());
		editor.getUserId().setForcePassword(true);
		editor.getUserId().setSso(false); 
		editor.getUserId().setSsoEmail(null);
		
		mav.addObject("editor", editor);
		
		//Get all paper publishers 
		List<Publisher> listPublishers = publisherService.listAllActive();
	    mav.addObject("publisherList", listPublishers);
	    
	    //Get all policies
  		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
  		mav.addObject("listPolicies", listPolicies);
		return mav;
	}
	
	@PostMapping("/secure/saveEditor")
	public ResponseEntity<String> saveEditor(@ModelAttribute PublisherUser editor,HttpSession session) {
		
		boolean isDupplicate = false;
				
		List<PublisherUser> listEditors = publisherUserService.getAllEditors();
		for (PublisherUser selectedEditor : listEditors) 
		{
			if((selectedEditor.getUserId().getUserId()).equals(editor.getUserId().getUserId())) {
				isDupplicate = true;
				break;
			}
		}
		
		if(!isDupplicate) {
			
			editor.getUserId().setUpdateDate(LocalDate.now());
			userService.save(editor.getUserId());
			
			uRole = new UserRole();
			uRole.setUserId(editor.getUserId());
			uRole.setRoleId(roleService.getByRole("EDITOR"));
			userRoleService.save(uRole);
			
			Password password = new Password();
			password.setUserId(editor.getUserId());
			password.setPassword(passwordEncoder.encode("1qaz@WSX"));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			Profile profile = new Profile();
			profile.setUser(editor.getUserId());
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
			
			publisherUserService.save(editor);
			
			email = new EmailDetails(
					editor.getUserId().getEmail() ,
					("Editor Registration - " + editor.getUserId().getName()),
					("Dear Sir/Madam, \n\n You has been registered to the QuizzMart successfully. Please use following details to login to system using http://localhost:8081/login. \n\n Username : " + editor.getUserId().getUsername() + " \n Email : "+ editor.getUserId().getEmail() +" \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					editor.getUserId(),
					("Editor Registration - " + editor.getUserId().getName()),
					("Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
			notificationService.save(notification,session);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {	
			if(userService.getCountByUserId(editor.getUserId().getUserId()) > 0) {
				editor.getUserId().setUpdateDate(LocalDate.now());
				userService.save(editor.getUserId());
				publisherUserService.save(editor);
				
				return new ResponseEntity<>("success", HttpStatus.OK);
			}else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	}
	
	@GetMapping("/deleteEditor")
	public ResponseEntity<String> deleteEditor(@RequestParam Long userId) {
		
		//publisherUserService.delete(publisherUserService.getByUserId(userId).getPbUserId());
		for(PublisherUser pbUser : publisherUserService.listAllByUser(userId)) {
			publisherUserService.delete(pbUser.getPbUserId());
		}
		
		passwordService.delete(passwordService.getPasswordByUser(userId).getUpId());
		
		for(UserRole ur : userRoleService.listByUser(userId)) {
			userRoleService.delete(ur.getUrId());
		}
		profileService.delete(profileService.getByUserId(userId).getProfileId());
		userService.deleteById(userId);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/systemEditorDeactive")
	public String userDeactive(@RequestParam Long userId) {
		User user = userService.getById(userId);
		user.setActive(false);
		userService.save(user);
		return "redirect:/listSystemEditors";
	}
	
	@GetMapping("/systemEditorActive")
	public String userActive(@RequestParam Long userId) {
		User user = userService.getById(userId);
		user.setActive(true);
		userService.save(user);
		return "redirect:/listSystemEditors";
	}

	//Student Management
	@RequestMapping("/listStudents")
	public ModelAndView listStudents(Model model,HttpSession session) {
		ModelAndView mav = new ModelAndView("user/student-list");
		
		User loggedUser = (User)session.getAttribute("user");
		mav.addObject("user", loggedUser);
		
		List<Student> listStudents = studentService.listAll();

		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");

		if (loggedRoles.contains(roleService.getByRole("PARENT"))) {
			listStudents = studentService.getStudentsByParent(loggedUser.getUserId());
		}
		
		if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			listStudents = studentService.getStudentsByLecturer(lecturerService.getLecturerByUserId(loggedUser.getUserId()));
		}
		
		if(loggedRoles.contains(roleService.getByRole("SUPERADMIN")) || loggedRoles.contains(roleService.getByRole("ADMIN")) || loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			listStudents = studentService.listAll();
		}
		
	    mav.addObject("listStudents", listStudents);
	    
	    return mav;
	}
		
	@GetMapping("/showStudentUpdate")
	public ModelAndView viewStudentUpdate(@RequestParam Long studentId,HttpSession session) {
		ModelAndView mav = new ModelAndView("user/student-form");
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");

		if (loggedRoles.contains(roleService.getByRole("PARENT"))) {
			mav = new ModelAndView("parent/student-form");
		}
		
		if (loggedRoles.contains(roleService.getByRole("STUDENT"))) {
			mav = new ModelAndView("student/student-form");
		}
		
		if(loggedRoles.contains(roleService.getByRole("SUPERADMIN")) || loggedRoles.contains(roleService.getByRole("ADMIN")) || loggedRoles.contains(roleService.getByRole("AUTHOR")) || loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			mav = new ModelAndView("user/student-form");
		}
		
		mav.addObject("user", user);

		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);

		//Get all parents
		List<User> listParents = userService.findAllByRole("PARENT");
		mav.addObject("listParents", listParents);
		
		Student student = studentService.getStudentByUserId(studentId);

		(student.getUserId()).setUpdateBy(user.getName());

		student.getUserId().setUpdateDate(LocalDate.now());

		mav.addObject("student", student);
		
		for(StudentPaperReview sr : studentPaperReviewService.listByStudent(student.getStudentId())) {
			if(studentPaperReviewDetailService.getCountByReview(sr.getSprId()) <= 0) {
				studentPaperReviewService.delete(sr.getSprId());
			}
		}
		//Get all Reviews
		List<StudentPaperReview> listReviews = studentPaperReviewService.listByStudent(student.getStudentId());
		
		if(listReviews.size() > 3) {
			while(listReviews.size() > 3) {
				listReviews.remove(listReviews.size()-1);
			}
		}
		mav.addObject("listReviews", listReviews);

		int parentCount = studentParentService.getParentCountByStudent(student.getUserId().getUserId());

		if(parentCount > 0) {
			mav.addObject("parent", studentParentService.getParentByStudent(student.getUserId().getUserId()).getParentId());
		}else {
			mav.addObject("parent", new User());
		}

		//Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
		
		//Get Classrooms
		List<ClassRoomStudent> listClassRooms = classRoomStudentService.listByUser(studentService.getStudentByUserId(studentId).getStudentId());
		mav.addObject("listClassRooms", listClassRooms);
				
		return mav;
	}
	
	@GetMapping("/addStudent")
	public ModelAndView viewAddStudent(HttpSession session) {
		ModelAndView mav = new ModelAndView("user/student-form");
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		if (loggedRoles.contains(roleService.getByRole("PARENT"))) {
			mav = new ModelAndView("parent/student-form");
		}
		
		if(loggedRoles.contains(roleService.getByRole("SUPERADMIN")) || loggedRoles.contains(roleService.getByRole("ADMIN")) || loggedRoles.contains(roleService.getByRole("AUTHOR")) || loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			mav = new ModelAndView("user/student-form");
		}
		
		Student student = new Student();
		User studentUser = new User();
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);
				
		//Get all parents
		List<User> listParents = userService.findAllByRole("PARENT");
		mav.addObject("listParents", listParents);
		
		studentUser.setCreateBy(user.getUsername());
		studentUser.setCreateDate(LocalDate.now());
		studentUser.setUpdateBy(user.getUsername());
		studentUser.setUpdateDate(LocalDate.now());
		studentUser.setForcePassword(true);
		studentUser.setSso(false);
		studentUser.setSsoEmail(null);
		
		student.setUserId(studentUser);
		
		mav.addObject("student", student);
		
		int parentCount = studentParentService.getParentCountByStudent(studentUser.getUserId());
		
		if(parentCount > 0) {
			mav.addObject("parent", studentParentService.getParentByStudent(studentUser.getUserId()).getParentId());
		}else {
			mav.addObject("parent", new User());
		}
		
		//Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
		
		return mav;
	}
	
	@PostMapping("/secure/saveStudent")
	public ResponseEntity<Object> saveStudent(@ModelAttribute("student") StudentRegisterDto student,HttpSession session) {
		boolean isDupplicate = false;
		boolean haveParent = false;
		
		List<User> listStudent = userService.findAll();
		for (User selectedStudent : listStudent) 
		{
			if((selectedStudent.getUserId().toString()).equals(student.getUserId())) {
				isDupplicate = true;
				break;
			}
		}

		if(!isDupplicate) {
			User stdUser = new User();
			stdUser.setActive(true);
			stdUser.setEmail(student.getStdEmail());
			stdUser.setGender(student.getStdGender());
			stdUser.setMobile(student.getStdMobile());
			stdUser.setName(student.getStdName());
			stdUser.setUsername(student.getStdUsername());
			stdUser.setCreateBy(student.getCreateUser());
			stdUser.setCreateDate(LocalDate.now());
			stdUser.setUpdateBy(student.getCreateUser());
			stdUser.setUpdateDate(LocalDate.now());
			stdUser.setForcePassword(true);
			stdUser.setSso(false);
			stdUser.setSsoEmail(null);
			userService.save(stdUser);
			
			uRole = new UserRole();
			uRole.setUserId(stdUser);
			uRole.setRoleId(roleService.getByRole("STUDENT"));
			userRoleService.save(uRole);
			
			Password password = new Password();
			password.setUserId(stdUser);
			password.setPassword(passwordEncoder.encode(student.getStdPassword()));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);

			Profile profile = new Profile();
			profile.setUser(stdUser);
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
			
			//Save Student
			Student std = new Student();
			std.setUserId(stdUser);
			std.setGpa(Double.parseDouble(student.getStdGPA()));
			std.setGrade(gradeService.get(Long.parseLong(student.getStdGrade())));
			if(std.getAttemptsAllowed() == 0) {
				std.setAttemptsAllowed(3);
			}
			studentService.save(std);
			
			email = new EmailDetails(
					student.getStdEmail(),
					("Student Registration - " + student.getStdName()),
					("Dear Student, \n\n Your have been registered to the QuizzMart. Please use following details to login to system. \n\n Username : " + student.getStdUsername() + " \n Email : " + student.getPrtEmail() + " \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					stdUser,
					("Student Registration - " + student.getStdName()),
					("Congratulations!! \n \n You have been successfully registered to the QuizzMart"));
			notificationService.save(notification,session);
			
			//SaveParent
			User parent = new User();
			if((userService.getParentCountByEmail(student.getPrtEmail()) > 0)) {
				parent = userService.getParentByEmail(student.getPrtEmail());
				haveParent = true;
			}else if(student.getPrtEmail() != "") {
				parent.setActive(true);
				parent.setEmail(student.getPrtEmail());
				parent.setGender(student.getPrtGender());
				parent.setMobile(student.getPrtMobile());
				parent.setName(student.getPrtName());
				parent.setUsername(student.getPrtEmail());
				parent.setCreateBy(student.getCreateUser());
				parent.setCreateDate(LocalDate.now());
				parent.setUpdateBy(student.getCreateUser());
				parent.setUpdateDate(LocalDate.now());
				parent.setForcePassword(true);
				parent.setSso(false);
				parent.setSsoEmail(null);
				userService.save(parent);
				
				uRole = new UserRole();
				uRole.setUserId(parent);
				uRole.setRoleId(roleService.getByRole("PARENT"));
				userRoleService.save(uRole);
				
				password = new Password();
				password.setUserId(parent);
				password.setPassword(passwordEncoder.encode("1qaz@WSX"));
				password.setActive(true);
				password.setLastUpdated(LocalDate.now());
				passwordService.save(password);
				
				profile = new Profile();
				profile.setUser(parent);
				profile.setProfilePic(null);
				profile.setCoverImage(null);
				profileService.save(profile);
				
				haveParent = true;
			}else {
				haveParent = false;
			}
			
			if(haveParent) {
				StudentParent stdParent = new StudentParent();
				stdParent.setParentId(parent);
				stdParent.setStudentId(stdUser);
				stdParent.setActive(true);
				stdParent.setCreateBy(student.getCreateUser());
				stdParent.setCreateDate(LocalDate.now());
				stdParent.setUpdateBy(student.getCreateUser());
				stdParent.setUpdateDate(LocalDate.now());
				studentParentService.save(stdParent);
			
				email = new EmailDetails(
						student.getPrtEmail(),
						("Student Registration - " + student.getStdName()),
						("Dear Parent, \n\n Your child has been registered to the QuizzMart. Please use following details to login to system and analysis your children using http://localhost:8081/login. \n\n Username : " + student.getPrtEmail() + " \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
				emailService.sendMail(email);
				
				notification = new Notification(
						stdUser,
						("Student Registration - " + student.getStdName()),
						("Congratulations!! \n \n Your child has been successfully registered to the QuizzMart"));
				notificationService.save(notification,session);
				
			}
			return new ResponseEntity<>(student, HttpStatus.OK);
		}else if((userService.getCountByUserId(Long.parseLong(student.getUserId()))) > 0){
						
			User stdUser = userService.getById(Long.parseLong(student.getUserId()));
			stdUser.setUsername(student.getStdUsername());
			stdUser.setEmail(student.getStdEmail());
			stdUser.setGender(student.getStdGender());
			stdUser.setMobile(student.getStdMobile());
			stdUser.setName(student.getStdName());
			stdUser.setUpdateBy(student.getCreateUser());
			stdUser.setUpdateDate(LocalDate.now());
			userService.save(stdUser);
						
			//Save Student
			Student std = studentService.getStudentByUserId(stdUser.getUserId());
			std.setGpa(Double.parseDouble(student.getStdGPA()));
			std.setGrade(gradeService.get(Long.parseLong(student.getStdGrade())));
			studentService.save(std);
			
			//SaveParent
			User parent = new User();
			if((userService.getParentCountByEmail(student.getPrtEmail()) > 0)) {
				parent = userService.getParentByEmail(student.getPrtEmail());
				haveParent = true;
			}else if(student.getPrtEmail() != "") {
				parent.setActive(true);
				parent.setEmail(student.getPrtEmail());
				parent.setGender(student.getPrtGender());
				parent.setMobile(student.getPrtMobile());
				parent.setName(student.getPrtName());
				parent.setUsername(student.getPrtEmail());
				parent.setCreateBy(student.getCreateUser());
				parent.setCreateDate(LocalDate.now());
				parent.setUpdateBy(student.getCreateUser());
				parent.setUpdateDate(LocalDate.now());
				parent.setForcePassword(true);
				parent.setSso(false);
				parent.setSsoEmail(null);
				userService.save(parent);
				
				uRole = new UserRole();
				uRole.setUserId(parent);
				uRole.setRoleId(roleService.getByRole("PARENT"));
				userRoleService.save(uRole);
				
				password = new Password();
				password.setUserId(parent);
				password.setPassword(passwordEncoder.encode("1qaz@WSX"));
				password.setActive(true);
				password.setLastUpdated(LocalDate.now());
				passwordService.save(password);
				
				Profile profile = new Profile();
				profile.setUser(parent);
				profile.setProfilePic(null);
				profile.setCoverImage(null);
				profileService.save(profile);
				
				haveParent = true;
			}else {
				haveParent = false;
			}
			if(haveParent && (studentParentService.getParentCountByStudent(stdUser.getUserId()) <= 0)) {
				StudentParent stdParent = new StudentParent();
				stdParent.setParentId(parent);
				stdParent.setStudentId(stdUser);
				stdParent.setActive(true);
				stdParent.setCreateBy(student.getCreateUser());
				stdParent.setCreateDate(LocalDate.now());
				stdParent.setUpdateBy(student.getCreateUser());
				stdParent.setUpdateDate(LocalDate.now());
				studentParentService.save(stdParent);
			}else if(haveParent && (studentParentService.getParentCountByStudent(stdUser.getUserId()) > 0)) {
				StudentParent stdParent = studentParentService.getByStudentUserId(stdUser.getUserId());
				if(stdParent.getParentId().getUserId() != parent.getUserId()) {
					studentParentService.delete(stdParent.getStudentParentId());
					
					stdParent = new StudentParent();
					stdParent.setParentId(parent);
					stdParent.setStudentId(stdUser);
					stdParent.setActive(true);
					stdParent.setCreateBy(student.getCreateUser());
					stdParent.setCreateDate(LocalDate.now());
					stdParent.setUpdateBy(student.getCreateUser());
					stdParent.setUpdateDate(LocalDate.now());
					studentParentService.save(stdParent);
				}
			}
			return new ResponseEntity<>(student, HttpStatus.OK);
			
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("/deleteStudent")
	public ResponseEntity<String> deleteStudent(@RequestParam Long studentId,@RequestParam Long userId) {
		
		List<StudentParent> records = studentParentService.listAllByStudent(userId);
		for (StudentParent selectedRecord : records) 
		{
			studentParentService.delete(selectedRecord.getStudentParentId());
		}
		
		for(UserRole ur : userRoleService.listByUser(userId)) {
			userRoleService.delete(ur.getUrId());
		}
		
		passwordService.delete(passwordService.getPasswordByUser(userId).getUpId());
		studentService.delete(studentId);
		profileService.delete(profileService.getByUserId(userId).getProfileId());
		userService.deleteById(userId);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/studentDeactive")
	public String studentDeactive(@RequestParam Long studentId,@RequestParam Long userId) {
		
		Student student = studentService.getStudent(studentId);
		
		User user = userService.getById(userId);
		
		user.setActive(false);
		
		userService.save(user);
		studentService.save(student);
		
		return "redirect:/listStudents";
	}
	
	@GetMapping("/studentActive")
	public String studentActive(@RequestParam Long studentId,@RequestParam Long userId) {
		
		Student student = studentService.getStudent(studentId);
		
		User user = userService.getById(userId);
		
		user.setActive(true);
		
		userService.save(user);
		studentService.save(student);
		
		return "redirect:/listStudents";
	}
	
	//Student Management
	
	@RequestMapping("/listParents")
	public ModelAndView listParents(Model model,HttpSession session) {
		
		List<User> listParents = userService.findAllByRole("PARENT");
		
	    ModelAndView mav = new ModelAndView("user/parent-list");
	    
	    mav.addObject("listParents", listParents);
	    
	    User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
	    
	    return mav;
	}
		
	
	@GetMapping("/showParentUpdate")
	public ModelAndView viewParentUpdate(@RequestParam Long userId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("user/parent-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		User parentUser = userService.getById(userId);
		mav.addObject("parent", parentUser);
		
		parentUser.setUpdateBy(user.getName());
		parentUser.setUpdateDate(LocalDate.now());
		
		//Get all Children
  		List<StudentParent> liststudents = studentParentService.getStudentsByParent(parentUser.getUserId());
  		mav.addObject("studentList", liststudents);
  		
  		//Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
		
		return mav;
	}
	
	@GetMapping("/addParent")
	public ModelAndView viewAddParent(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("user/parent-form");
		
		User parent = new User();
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
				
		parent.setCreateBy(user.getUsername());
		parent.setCreateDate(LocalDate.now());
		parent.setUpdateBy(user.getUsername());
		parent.setUpdateDate(LocalDate.now());
		parent.setForcePassword(true);
		parent.setSso(false);
		parent.setSsoEmail(null);
		mav.addObject("parent", parent);
		
		//Get all policies
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		mav.addObject("listPolicies", listPolicies);
		
		return mav;
	}
	
	@PostMapping("/secure/saveParent")
	public ResponseEntity<String> saveParent(@ModelAttribute User user) {
		boolean isDupplicate = false;
				
		List<User> listParent = userService.findAll();
		for (User selectedParent : listParent) 
		{
			if(selectedParent.getUsername().equals(user.getUsername())) {
				isDupplicate = true;
				break;
			}
		}
		
		if(!isDupplicate) {
			user.setCreateDate(LocalDate.now());
			
			userService.save(user);
			
			uRole = new UserRole();
			uRole.setUserId(user);
			uRole.setRoleId(roleService.getByRole("PARENT"));
			userRoleService.save(uRole);
			
			password = new Password();
			password.setUserId(user);
			password.setPassword(passwordEncoder.encode("1qaz@WSX"));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			Profile profile = new Profile();
			profile.setUser(user);
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
		}else {
			user.setUpdateDate(LocalDate.now());
			userService.save(user);
		}		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/deleteParent")
	public ResponseEntity<String> deleteParent(@RequestParam Long parentId) {
		
		List<StudentParent> records = studentParentService.listAllByParent(parentId);
		for (StudentParent selectedRecord : records) 
		{
			studentParentService.delete(selectedRecord.getStudentParentId());
		}
		
		for(UserRole ur : userRoleService.listByUser(parentId)) {
			userRoleService.delete(ur.getUrId());
		}
		
		passwordService.delete(passwordService.getPasswordByUser(parentId).getUpId());
		profileService.delete(profileService.getByUserId(parentId).getProfileId());
		userService.deleteById(parentId);
			
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/parentDeactive")
	public String parentDeactive(@RequestParam Long userId) {
		
		User user = userService.getById(userId);
		user.setActive(false);
		
		userService.save(user);
		
		return "redirect:/listParents";
	}
	
	@GetMapping("/parentActive")
	public String parentActive(@RequestParam Long userId) {
		
		User user = userService.getById(userId);
		user.setActive(true);
		
		userService.save(user);
		
		return "redirect:/listParents";
	}
	
	//Author Management
	
	@Autowired
	private UserService authorService;
	
	@RequestMapping("/listAuthors")
	public ModelAndView listAuthors(HttpSession session) {
		ModelAndView mav;
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		
		mav = new ModelAndView("user/author-list");
	    if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {

    		//List<PublisherUser> listUsers = publisherUserService.listAllByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId().getPoId());
    		List<PublisherUser> listUsers = publisherUserService.listAuthorsByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId());
	    	 
	 	    mav.addObject("listUsers", listUsers);
	 	    mav.addObject("poId", publisherUserService.getByUserId(user.getUserId()).getPoId());
		}else {
			 List<PublisherUser> listUsers = publisherUserService.getAllAuthor();
			 
		     mav.addObject("listUsers", listUsers);
		}
		mav.addObject("user", user);
		
	    return mav;
	}
	
	@RequestMapping("/listAuthorsByPublisher")
	public ModelAndView listAuthorsByPublisher(@RequestParam Publisher poId,HttpSession session) {
	    List<PublisherUser> listAuthors = publisherUserService.listAllByPublisher(poId.getPoId());
	    
	    ModelAndView mav = new ModelAndView("user/author-list");
	    mav.addObject("listAuthors", listAuthors);
	    mav.addObject("poId", poId);
	    
	    User user = (User)session.getAttribute("user");
	    mav.addObject("user", user);
	    
	    return mav;
	}
	
	@GetMapping("/showAuthorUpdate")
	public ModelAndView viewAuthorUpdate(@RequestParam Long authorId,HttpSession session) {
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		
		ModelAndView mav = new ModelAndView("user/author-form");
		
		Publisher publisher = new Publisher();
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			publisher = publisherUserService.getByUserId(user.getUserId()).getPoId();
		}
		mav.addObject("publisher", publisher);
		
		//User author = authorService.getAuthorById(authorId);
		PublisherUser author = publisherUserService.getByUserId(authorId);
		author.getUserId().setUpdateBy(user.getName());
		author.getUserId().setUpdateDate(LocalDate.now());
		
		mav.addObject("author", author);
		
		mav.addObject("user", user);
		
		//Get all paper publishers 
		List<Publisher> listPublishers = publisherService.listAllActive();
	    mav.addObject("publisherList", listPublishers); 
	    
	    //Get all policies
  		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
  		mav.addObject("listPolicies", listPolicies);
  		
		return mav;
	}
	
	@GetMapping("/addAuthor")
	public ModelAndView viewAddAuthor(HttpSession session) {
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		
		PublisherUser author = new PublisherUser();
		author.setUserId(new User());
		author.getUserId().setUpdateBy(user.getName());
		author.getUserId().setUpdateDate(LocalDate.now());
		author.setRole(roleService.getByRole("AUTHOR"));
		
		ModelAndView mav = new ModelAndView("user/author-form");
		Publisher publisher = new Publisher();
			
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			publisher = publisherUserService.getByUserId(user.getUserId()).getPoId();
			author.setPoId(publisher);
		}
		mav.addObject("publisher", publisher);
		
		mav.addObject("user", user);
		author.getUserId().setCreateBy(user.getUsername());
		author.getUserId().setCreateDate(LocalDate.now());
		author.getUserId().setUpdateBy(user.getName());
		author.getUserId().setUpdateDate(LocalDate.now());
		author.getUserId().setForcePassword(true);
		author.getUserId().setSso(false);
		author.getUserId().setSsoEmail(null);
		
		mav.addObject("author", author);
		
		//Get all paper publishers 
		List<Publisher> listPublishers = publisherService.listAllActive();
	    mav.addObject("publisherList", listPublishers);
	    
	    //Get all policies
  		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
  		mav.addObject("listPolicies", listPolicies);
  		
		return mav;
	}
	
	@PostMapping("/secure/saveAuthor")
	public ResponseEntity<String> saveAuthor(@ModelAttribute PublisherUser author,HttpSession session) {
		boolean isDupplicate = false;
			
		List<PublisherUser> listAuthor = publisherUserService.getAllAuthor();
		
		for (PublisherUser selectedAuthor : listAuthor) 
		{
			if(selectedAuthor.getUserId().getUserId().equals(author.getUserId().getUserId())) {
				isDupplicate = true;
				break;
			}
		}
		if(!isDupplicate) {
			
			author.getUserId().setUpdateDate(LocalDate.now());
			userService.save(author.getUserId());
			
			publisherUserService.save(author);
			
			uRole = new UserRole();
			uRole.setUserId(author.getUserId());
			uRole.setRoleId(roleService.getByRole("AUTHOR"));
			userRoleService.save(uRole);
			
			password = new Password();
			password.setUserId(author.getUserId());
			password.setPassword(passwordEncoder.encode("1qaz@WSX"));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			Profile profile = new Profile();
			profile.setUser(author.getUserId());
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
			
			email = new EmailDetails(
					author.getUserId().getEmail(),
					("Author Registration - " + author.getUserId().getName()),
					("Dear Sir/Madam, \n\n You has been registered to the QuizzMart successfully. Please use following details to login to system using http://localhost:8081/login. \n\n Username : " + author.getUserId().getName() + " \n Email : " + author.getUserId().getEmail() + " \n Password : 1qaz@WSX \n Please change the password after first time login to the system."));
			emailService.sendMail(email);
			
			notification = new Notification(
					author.getUserId(),
					("Author Registration - " + author.getUserId().getName()),
					("Congratulations!! \n \n Your child has been successfully registered to the QuizzMart"));
			notificationService.save(notification,session);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {		
			if(userService.getCountByUserId(author.getUserId().getUserId()) > 0) {
				author.getUserId().setUpdateDate(LocalDate.now());
				userService.save(author.getUserId());
				publisherUserService.save(publisherUserService.get(author.getPbUserId()));
				return new ResponseEntity<>("success", HttpStatus.OK);
			}else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	}
	
	@GetMapping("/deleteAuthor")
	public ResponseEntity<String> deleteAuthor(@RequestParam Long authorId) {
		
		publisherUserService.delete(publisherUserService.getByUserId(authorId).getPbUserId());
		
		for(UserRole ur : userRoleService.listByUser(authorId)) {
			userRoleService.delete(ur.getUrId());
		}
		
		passwordService.delete(passwordService.getPasswordByUser(authorId).getUpId());
		profileService.delete(profileService.getByUserId(authorId).getProfileId());
		authorService.deleteById(authorId);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/authorDeactive")
	public String authorDeactive(@RequestParam Long authorId) {
		User author = userService.getById(authorId);
		author.setActive(false);
		userService.save(author);
		return "redirect:/listAuthors";
	}
	
	@GetMapping("/authorActive")
	public String authorActive(@RequestParam Long authorId) {
		User author = userService.getById(authorId);
		author.setActive(true);
		userService.save(author);
		return "redirect:/listAuthors";
	}
	
	@GetMapping("/authorDeactiveByPublisher")
	public String authorDeactiveByPublisher(@RequestParam Long authorId) {
		User author = userService.getById(authorId);
		author.setActive(false);
		userService.save(author);
		return "redirect:/listAuthorsByPublisher?poId=" + publisherUserService.getByUserId(author.getUserId()).getPoId().getPoId() ;
	}
	
	@GetMapping("/authorActiveByPublisher")
	public String authorActiveByPublisher(@RequestParam Long authorId) {
		User author = userService.getById(authorId);
		author.setActive(true);
		userService.save(author);
		return "redirect:/listAuthorsByPublisher?poId=" + publisherUserService.getByUserId(author.getUserId()).getPoId().getPoId();
	}
	
	@GetMapping("/sendOtp")
	public ResponseEntity<Object> sendOtp(@RequestParam String username,HttpSession session) {
		
		//User user = userService.getUserByEmail(emailAddress);
		User user = userService.getByUserName(username);
		
		if(!user.equals(null)) {
			
			String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
			
	        ForgottenPasswordDto forgottenPassword= new ForgottenPasswordDto();
			forgottenPassword.setEmail(user.getEmail());
			forgottenPassword.setUserId(user.getUserId());
			forgottenPassword.setUsername(user.getUsername());
			forgottenPassword.setIsActive(Boolean.toString(user.isActive()));
			forgottenPassword.setOtp(otp);
			
			email = new EmailDetails(
					user.getEmail(),
					("QuizzMart - One Time Password"),
					("Dear User, \n\n Your OTP is : " + otp));
			emailService.sendMail(email);
			
			notification = new Notification(
					user,
					("QuizzMart - One Time Password"),
					("Dear User, \n\n Your OTP is : " + otp));
			notificationService.save(notification,session);
						
			return new ResponseEntity<>(forgottenPassword, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestParam String userId , @RequestParam String pwd ,@RequestParam String type , HttpSession session) {
		
		User user = userService.getById(Long.parseLong(userId));
				
		if(session.getAttribute("user") != null) {
			User loggedUser = (User)session.getAttribute("user");
						
			if(Long.parseLong(userId) != loggedUser.getUserId()) {
				user.setForcePassword(true);
			}else {
				user.setForcePassword(false);
			}
		}else {
			user.setForcePassword(false);
		}
		
		userService.save(user);
		
		if(!user.equals(null)){	
			password = passwordService.getPasswordByUser(user.getUserId());
			password.setUserId(user);
			password.setPassword(passwordEncoder.encode(pwd));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			email = new EmailDetails(
					user.getEmail(),
					("QuizzMart - Password Has Been Change! "),
					("Dear User, \n\n Your Password has been change. please login to the system and set profile settings."));
			emailService.sendMail(email);
			
			notification = new Notification(
					user,
					("QuizzMart - Password Has Been Change! "),
					("Your Password has been change. please login to the system and set profile settings."));
			notificationService.save(notification,session);
				
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/createUserRole")
	public ResponseEntity<String> createUserRole(@RequestParam Long userId,@RequestParam Long roleId,HttpSession session) {
		User user = userService.getById(userId);
		Role role = roleService.get(roleId);
		int count = userRoleService.getCountByUserAndRole(user.getUserId(),roleId);
		
		if(count > 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			if((userRoleService.getCountByUserAndRole(userId,roleService.getByRole("GUEST").getRoleId())) > 0) {
				userRoleService.delete(userRoleService.getByUserAndRole(userId, roleService.getByRole("GUEST").getRoleId()).getUrId());
			}
			
			if(role.getRole().equals("STUDENT")) {
				Student student = new Student();
				student.setAttemptsAllowed(10);
				student.setGpa(0);
				student.setGrade(gradeService.getByGrade(1));
				student.setUserId(user);
				studentService.save(student);						
			}else if(role.getRole().equals("LECTURER")) {
				Lecturer lecturer = new Lecturer();
				lecturer.setDescription(null);
				lecturer.setEducationQualification(null);
				lecturer.setInstituteName(null);
				lecturer.setNicNumber(null);
				lecturer.setUserId(user);
				lecturerService.save(lecturer);
			}
			
			UserRole uRole = new UserRole();
			uRole.setUserId(user);
			uRole.setRoleId(role);
			userRoleService.save(uRole);
			//Login.loggedRoles = roleService.listByUser(user.getUserId());
		    //Login.loggedRolesName = roleService.listRoleNamesByUser(user.getUserId());  
			session.setAttribute("loggedRoles", roleService.listByUser(user.getUserId()));
			session.setAttribute("loggedRolesName", roleService.listRoleNamesByUser(user.getUserId()));
		    List<Long> listRoleFunctionsId = roleFunctionService.getFunctionsForUser(user.getUserId());
		    List<RoleFunction> listRoleFunctions = new ArrayList<RoleFunction>();
		    List<String> listRoleFunctionsActions = new ArrayList<String>();
		    for(Long r : listRoleFunctionsId) {
		    	listRoleFunctions.add(roleFunctionService.getByFunction(r));
		    	listRoleFunctionsActions.add(roleFunctionService.getByFunction(r).getFunctionId().getFunctionAction());
		    }
		    session.setAttribute("listRoleFunctions", listRoleFunctions);
		    //Login.listRoleFunctions = listRoleFunctions;
		    session.setAttribute("listRoleFunctionsId", listRoleFunctionsId);
		    //Login.listRoleFunctionsId = listRoleFunctionsId;
		    session.setAttribute("listRoleFunctionsActions", listRoleFunctionsActions);
		    //Login.listRoleFunctionsActions = listRoleFunctionsActions;
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
	}
	
	@GetMapping("/deleteUserRole")
	public ResponseEntity<String> deleteUserRole(@RequestParam Long uRoleId,HttpSession session) {
		UserRole uRole = userRoleService.get(uRoleId);
		Role role = uRole.getRoleId();
		User user = uRole.getUserId();
		int count = userRoleService.getCountByUserRole(uRoleId);
		
		if(count > 0) {
			if((userRoleService.getCountByUser(user.getUserId())) == 1) {
				uRole = new UserRole();
				uRole.setUserId(user);
				uRole.setRoleId(roleService.getByRole("GUEST"));
				userRoleService.save(uRole);
			}
			
			try {
				if(role.getRole().equals("STUDENT")) {
					studentService.delete(studentService.getStudentByUserId(user.getUserId()).getStudentId());						
				}else if(role.getRole().equals("LECTURER")) {
					lecturerService.delete(lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId());
				}else if(role.getRole().equals("AUTHOR") || role.getRole().equals("EDITOR")) {
					publisherUserService.delete(publisherUserService.getByUserAndType(user.getUserId(),role.getRoleId()).getPbUserId());
				}
				
				userRoleService.delete(uRoleId);
				
			}catch(Exception ex) {
				System.out.println(ex);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			session.setAttribute("loggedRoles", roleService.listByUser(user.getUserId()));
		    session.setAttribute("loggedRolesName", roleService.listRoleNamesByUser(user.getUserId()));
		    
		    List<Long> listRoleFunctionsId = roleFunctionService.getFunctionsForUser(user.getUserId());
		    List<RoleFunction> listRoleFunctions = new ArrayList<RoleFunction>();
		    List<String> listRoleFunctionsActions = new ArrayList<String>();
		    for(Long r : listRoleFunctionsId) {
		    	listRoleFunctions.add(roleFunctionService.getByFunction(r));
		    	listRoleFunctionsActions.add(roleFunctionService.getByFunction(r).getFunctionId().getFunctionAction());
		    }
		    session.setAttribute("listRoleFunctions", listRoleFunctions);
		    //Login.listRoleFunctions = listRoleFunctions;
		    
		    session.setAttribute("listRoleFunctionsId", listRoleFunctionsId);
		    //Login.listRoleFunctionsId = listRoleFunctionsId;
		    
		    session.setAttribute("listRoleFunctionsActions", listRoleFunctionsActions);
		    //Login.listRoleFunctionsActions = listRoleFunctionsActions;
		    
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}
	}
	
	
	@GetMapping("/getStudentListByEmail")
	public ResponseEntity<List<User>> getStudentsByEmail(@RequestParam String email,HttpSession session) {
		
		List<User> studentList = userService.getStudentListByEmail(email);
		  
		return new ResponseEntity<>(studentList, HttpStatus.OK);
	}
	
	@PostMapping("/secure/savePhoto")
	public ResponseEntity<String> savePhoto(@RequestParam String type , @RequestParam String data,@RequestParam String name,HttpSession session) {		
		User user = (User)session.getAttribute("user");
		Profile profile = profileService.getByUserId(user.getUserId());
		
		if(!((data).equals(""))) {
			byte[] decodedImg = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
			if(type.equals("cover")) {
				Path destinationFile = Paths.get(imageLocation + "/users/cover-photos", name);
				try {
					Files.write(destinationFile, decodedImg);
					profile.setCoverImage("/users/cover-photos/" + name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(type.equals("profile")) {
				Path destinationFile = Paths.get(imageLocation + "/users/profile-photos", name);
				try {
					Files.write(destinationFile, decodedImg);
					profile.setProfilePic("/users/profile-photos/" + name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		profileService.save(profile);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

}

