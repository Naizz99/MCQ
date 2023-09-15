package com.rcs2.mcqsys.controller;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcs2.mcqsys.config.CustomOAuth2User;
import com.rcs2.mcqsys.config.MCQAuthenticationSuccessHandler;
import com.rcs2.mcqsys.dto.EmailDto;
import com.rcs2.mcqsys.dto.StudentRegisterDto;
import com.rcs2.mcqsys.dto.answerDto;
import com.rcs2.mcqsys.model.Banner;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.model.PasswordPolicy;
import com.rcs2.mcqsys.model.Profile;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.model.StudentParent;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.UserPurchase;
import com.rcs2.mcqsys.model.UserRole;
import com.rcs2.mcqsys.repository.EmailService;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.RoleFunction;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.service.ActiveSessionsService;
import com.rcs2.mcqsys.service.BannerService;
import com.rcs2.mcqsys.service.CartService;
import com.rcs2.mcqsys.service.ClassRoomMessagesService;
import com.rcs2.mcqsys.service.ClassRoomService;
import com.rcs2.mcqsys.service.ClassRoomStudentService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.UserPurchaseService;
import com.rcs2.mcqsys.service.LecturerService;
import com.rcs2.mcqsys.service.MessageService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleFunctionService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.PasswordPolicyService;
import com.rcs2.mcqsys.service.PasswordService;
import com.rcs2.mcqsys.service.ProfileService;
import com.rcs2.mcqsys.service.StudentPaperReviewService;
import com.rcs2.mcqsys.service.StudentParentService;
import com.rcs2.mcqsys.service.StudentService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.UserRoleService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class WebController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LecturerService lecturerService;
		
	@Autowired
	private StudentPaperReviewService paperReviewService;
	
	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private PublisherUserService publisherUserService;
	
	@Autowired
	private PaperService paperService;
	
	@Autowired
	private ActiveSessionsService activeSessionsService;
	
	@Autowired
	private ClassRoomService classRoomService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private StudentParentService studentParentService;
	
	@Autowired
	private ClassRoomMessagesService classRoomMessagesService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private UserPurchaseService lecturePaperService;
	
	@Autowired
	private ClassRoomStudentService classRoomStudentService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
    private RoleFunctionService roleFunctionService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;

	@Autowired
    private SubjectService subjectService;
	
	@Autowired
    private ProfileService profileService;
	
	@Autowired
    private PasswordService passwordService;
	
	@Autowired
    private PasswordPolicyService passwordPolicyService;
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserPurchaseService userPurchaseService;
	
	@Autowired 
	private EmailService emailService;
	EmailDetails email;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping("/")
	public String Start() {
		return "redirect:/home";
	}
	
	@RequestMapping("/login")
	public String login(Model model){
		return "login";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout();
		return "login";
	}
	
	@RequestMapping("/ssoLogout")
	public String ssoLogout(HttpServletRequest request,@RequestParam int param) throws ServletException {
		request.logout();
		
		if(param == 1) {
			return "redirect:login?sso";
		}else if(param == 2){
			return "redirect:login?notSSO";
		}else {
			return "login";
		}
	}
	
	@RequestMapping("/userNotActive")
	public String userNotActive(HttpServletRequest request) throws ServletException {
		request.logout();
		
		return "redirect:login?userNotActive";
	}
	
	@RequestMapping("/index")
	public String home(Model model,HttpSession session) {
		model.addAttribute("publisherList", publisherService.listAllActive());
		model.addAttribute("classroomList", classRoomService.listAll());
		return "guest-index";
	}
	
	@RequestMapping("/validate")
	public String validate(Model model, HttpSession session,Authentication authentication){
		int countByUsername,countByEmail = 0;
		CustomOAuth2User oauthUser = null;
		
		if(MCQAuthenticationSuccessHandler.getName() != "anonymousUser") {
			countByUsername = userService.getCountByUserName(authentication.getName());
			if(!(authentication.getPrincipal().equals(null)) && (countByUsername <= 0)) {
				oauthUser = (CustomOAuth2User) authentication.getPrincipal();
				countByEmail = userService.getCountByEmail(oauthUser.getEmail());
			}
			User user = new User();
			if(countByUsername > 0) {
				session.setAttribute("valid", true);
				user = userService.getByUserName(authentication.getName());
				if(user.isSso()) {
					return "redirect:/ssoLogout?param=1";
				}
			}else if(countByEmail > 0){
				session.setAttribute("valid", true);
				user = userService.getByEmail(oauthUser.getEmail());
				if(!user.isSso()) {
					return "redirect:/ssoLogout?param=2";
				}
			}else {
				session.setAttribute("valid", false);
				return "redirect:/index";
			}
			 
			session.setAttribute("user", user);
			model.addAttribute("user", user);
			
			session.setAttribute("profile", profileService.getByUserId(user.getUserId()));
		    session.setAttribute("roles", roleService.listAll());		    
		    session.setAttribute("loggedRoles", roleService.listByUser(user.getUserId()));		    	
		    session.setAttribute("loggedRolesName", roleService.listRoleNamesByUser(user.getUserId()));		    
			session.setAttribute("unreadMsgCount", messageService.unreadMessageCountByReceiver(user.getUserId()));			
			session.setAttribute("unreadMsgList", messageService.listAllUnread(user.getUserId()));			
			session.setAttribute("unreadNotificationCount", notificationService.unreadNotificationCountByReceiver(user.getUserId()));			
			session.setAttribute("cartItemCount", cartService.getCountByUser(user.getUserId()));			
		    session.setAttribute("listRoleFunctionsId", roleFunctionService.getFunctionsForUser(user.getUserId()));
		    session.setAttribute("cartItemCount", cartService.getCountByUser(user.getUserId()));
		    		
		    List<RoleFunction> listRoleFunctions = new ArrayList<RoleFunction>();
		    
		    List<String> listRoleFunctionsActions = new ArrayList<String>();
		    for(Long r : roleFunctionService.getFunctionsForUser(user.getUserId())) {
		    	listRoleFunctions.add(roleFunctionService.getByFunction(r));
		    	listRoleFunctionsActions.add(roleFunctionService.getByFunction(r).getFunctionId().getFunctionAction());
		    }
		    session.setAttribute("listRoleFunctions", listRoleFunctions);
		    session.setAttribute("listRoleFunctionsActions", listRoleFunctionsActions);
		    
		    int passwordDateDiff = passwordService.getDateDiff(LocalDate.now(),passwordService.getPasswordByUser(user.getUserId()).getLastUpdated());
		    
		    if(passwordDateDiff >= 40) {
		    	user.setForcePassword(true);
		    	userService.save(user);
		    }
		    
		    int purchaseCount = userPurchaseService.listAllCountByUser(user.getUserId());
		    if(purchaseCount > 0) {
		    	for(UserPurchase up : userPurchaseService.listAllByUser(user.getUserId())) {
		    		long noOfDays = Duration.between(LocalDate.now().atStartOfDay(), up.getEndDate().atStartOfDay()).toDays();
		    		if(noOfDays < 0) {
		    			up.setExpired(true);
		    			up.setAvailableDates(0);
		    		}else {
		    			up.setAvailableDates((int)noOfDays);
		    		}
		    		userPurchaseService.save(up);
		    	}
		    }
		    
		    for(ClassRoom cr : classRoomService.listAll()) {
		    	int noOfDays = (int)Duration.between(LocalDate.now().atStartOfDay(), cr.getEndDate().atStartOfDay()).toDays();
				if(noOfDays < 0) {
					cr.setExpired(true);
					cr.setStatus(false);
					cr.setActive(false);
					cr.setAvailableDates(0);
	    		}else {
	    			cr.setAvailableDates(noOfDays);
	    		}
				classRoomService.save(cr);
			}

		    if(!user.isActive()) {
		    	return "redirect:/userNotActive";
		    }else if((user.isForcePassword()) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("GUEST")))) {
				if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("STUDENT"))) {
					Student student = studentService.getStudentByUserId(user.getUserId());
					model.addAttribute("student",student);
				}
				if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("LECTURER"))) {
					Lecturer lecturer = lecturerService.getLecturerByUserId(user.getUserId());
					model.addAttribute("lecturer",lecturer);
				}
				if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("AUTHOR"))) {
					Publisher publisher = (publisherUserService.getByUserAndType(user.getUserId(), roleService.getByRole("AUTHOR").getRoleId()).getPoId());
					model.addAttribute("publisher",publisher);
				}
				model.addAttribute("profile",profileService.getByUserId(user.getUserId()));
				//Get all policies
		  		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		  		model.addAttribute("listPolicies", listPolicies);
		  		model.addAttribute("listRoles", roleService.listPublicRoles());
		  		model.addAttribute("listUserRoles", userRoleService.listByUser(user.getUserId()));
				return "users-profile";
			}else {
				return "redirect:/home";
			}
		}
		session.setAttribute("user", null);
		return "redirect:/index";
	}
	
	@RequestMapping("/home")
	public String viewIndexPage(Model model, HttpSession session) {
		User user = (User)session.getAttribute("user");
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		if(MCQAuthenticationSuccessHandler.getName() == "anonymousUser") {
			return "redirect:/index";
		}
				
		List<Banner> bannerList2 = new ArrayList<Banner>();
		model.addAttribute("user", user);
		model.addAttribute("bannerList1", bannerService.listAllNonGradeActive());
		model.addAttribute("stdCount", userService.getCountByRole("STUDENT"));
		model.addAttribute("adminCount", userService.getCountByRole("ADMIN"));
		model.addAttribute("lecCount", userService.getCountByRole("LECTURER"));
		model.addAttribute("auCount", userService.getCountByRole("AUTHOR"));
		model.addAttribute("sessionCount", paperReviewService.getReviewCount());
		model.addAttribute("activeCount", activeSessionsService.getActiveCount());
		model.addAttribute("publisherCount", publisherService.getPublisherCount());
		model.addAttribute("paperCount", paperService.getPaperCount());
		model.addAttribute("activePapers", paperService.getActivePapers());
		model.addAttribute("deactivePapers", paperService.getDeactivePapers());
		model.addAttribute("activeClassRoomCount", classRoomService.getAciveCount());
		model.addAttribute("classRoomCount", classRoomService.getAllCount());
	    model.addAttribute("boughtPaperCount", lecturePaperService.listAllCountByUser(user.getUserId()));
	    		    
		List<Notification> notficationList = notificationService.listAllUnread(user.getUserId());
		for(Notification n : notficationList) {
			n.setNotification(n.getNotification().substring(0,25) + "....read");
		}
		session.setAttribute("listRoleFunctionsId", roleFunctionService.getFunctionsForUser(user.getUserId()));
				
		if (loggedRoles.contains(roleService.getByRole("STUDENT"))) {
			
			Student student = studentService.getStudentByUserId(user.getUserId());
			
		    List<Subject> listSubjects = subjectService.studentListSubjectsByGrade(student.getGrade());
		   
		    List<ClassRoomStudent> listClassrooms = classRoomStudentService.listByUser(student.getStudentId());
		    model.addAttribute("listSubjects", listSubjects);
		    model.addAttribute("listClassrooms", listClassrooms);
		    
		    bannerList2 = bannerService.listAllActiveByGrade(student.getGrade().getGradeId());
			model.addAttribute("bannerList2", bannerList2);
			
		} else if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
	
			model.addAttribute("listAuthors", publisherUserService.listAuthorsByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId()));
		    model.addAttribute("listEditors", publisherUserService.listEditorsByPublisher((publisherUserService.getByUserId(user.getUserId())).getPoId()));
		    
		} else if (loggedRoles.contains(roleService.getByRole("EDITOR"))) {
			//List<User> listAuthors = new ArrayList<>();
		    //model.addAttribute("listAuthors", listAuthors);
		    model.addAttribute("listEditors", publisherUserService.listEditorsByPublisher((publisherUserService.getByUserId(user.getUserId())).getPoId()));
		} else if (loggedRoles.contains(roleService.getByRole("PARENT"))) {
			model.addAttribute("childrenCount", studentParentService.getAllStudentsForParent(user.getUserId()));
			model.addAttribute("completedCount", paperReviewService.getCompleteCount(user.getUserId()));
			model.addAttribute("timeCount", paperReviewService.getTotalTime(user.getUserId()));
			
			StudentPaperReview highestMark = paperReviewService.getHighestMark(user.getUserId());
			
			if(highestMark != null) {
				model.addAttribute("highestMark", highestMark.getResult());
				model.addAttribute("highestMarkStd", highestMark.getStudentId().getStudentId());
			}else {
				model.addAttribute("highestMark", 0);
				model.addAttribute("highestMarkStd", "");
			}
			
			model.addAttribute("listStudents", studentParentService.listAllByParent(user.getUserId()));
		    model.addAttribute("listClasses", classRoomService.listAllByParent(user.getUserId()));
		}else if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			model.addAttribute("listClassMessages", classRoomMessagesService.listAllUnreadByReceiver(user.getEmail()));
			model.addAttribute("unreadClsMsgCount", classRoomMessagesService.unreadMessageCountByReceiver(user.getEmail()));
			model.addAttribute("classRoomCountForLecture", classRoomService.getCountByLecture(user.getUserId()));
			model.addAttribute("activeClassRoomCountForLecture", classRoomService.getActiveCountByLecture(user.getUserId()));
			System.out.println("user.getUserId() " + user.getUserId());
			System.out.println("lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId() " + lecturerService.getLecturerByUserId((long)341));
			System.out.println("classRoomStudentService.getCountByLecture(lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId()) " + classRoomStudentService.getCountByLecture(lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId()));
			model.addAttribute("clsStdCount", classRoomStudentService.getCountByLecture(lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId()));
			model.addAttribute("listStudents", classRoomStudentService.listAllOnlineByLecturer(user.getUserId()));
		}
		return "dashboard";
	}
		
	@GetMapping("/serialExtract")
	public ResponseEntity<String> serialExtract(@RequestParam String serial) {
		
		String[] arrOfStr = serial.split("AccNo:  ", 5);
  
		String temp = arrOfStr[1].substring(0, arrOfStr[1].indexOf(' '));
		serial = temp.replace("/", "");
		
		return new ResponseEntity<>(temp, HttpStatus.OK);
	}
	
	@GetMapping("/error")
	public String error() {
		return "redirect:/validate";
	}
	
	@RequestMapping("/forgottenPassword")
	public String forgottenPassword() {
		return "forgot-password";
	}
		
	@RequestMapping("/userProfile")
	public ModelAndView userProfile(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		ModelAndView mav = new ModelAndView("users-profile");
		if(loggedRoles.contains(roleService.getByRole("STUDENT"))) {	
		    mav.addObject("gradeList", gradeService.listAllActive());
		    mav.addObject("student", studentService.getStudentByUserId(user.getUserId()));
		}
		if(loggedRoles.contains(roleService.getByRole("LECTURER"))) {	
		    mav.addObject("lecturer", lecturerService.getLecturerByUserId(user.getUserId()));
		}
		if(loggedRoles.contains(roleService.getByRole("AUTHOR"))) {	
		    mav.addObject("publisher", publisherUserService.getByUserId(user.getUserId()).getPoId());
		}
		mav.addObject("profile",profileService.getByUserId(user.getUserId()));
		mav.addObject("user", user);
  		mav.addObject("listPolicies", passwordPolicyService.listAll());
  		mav.addObject("listRoles", roleService.listPublicRoles());
  		mav.addObject("listUserRoles", userRoleService.listByUser(user.getUserId()));
  		
		return mav;
	}
	
	@RequestMapping("/publisherProfile")
	public ModelAndView publisherProfile(@RequestParam long publisherId,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		ModelAndView mav = new ModelAndView("publisher-profile");
		
		mav.addObject("publisher", publisherService.get(publisherId));
		
		mav.addObject("profile",profileService.getByUserId(user.getUserId()));
		mav.addObject("user", user);
  		
		return mav;
	}
	
	@RequestMapping("/studentPage")
	public String studentPage(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "student/student-panel";
	}
	
	@RequestMapping("/teacherPage")
	public String teacherPage(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "teacher/teacher-panel";
	}
	
	@RequestMapping("/adminPage")
	public String adminPage(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "admin/admin-panel";
	}
	
	@RequestMapping("/superAdminPage")
	public String superAdminPage(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "services/super-admin-panel";
	}
	
	@RequestMapping("/pageNotFound")
	public String pageNotFound(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "error/pages-error-404";
	}
	
	@RequestMapping("/inactiveUserLogin")
	public String inactiveUserLogin(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "error/inactive-login";
	}
	
	@PostMapping("/secure/userValidation")
	public ResponseEntity<String> userValidation(@RequestParam String user) {
		
		boolean isDupplicate = false;
		
		List<User> listUsers = userService.findAll();
		
		for (User selectedUser : listUsers) 
		{
			if((selectedUser.getUsername().equals(user)) || (selectedUser.getEmail().equals(user))) {
				isDupplicate = true;
				break;
			}
		}
		
		if(isDupplicate) {
			return new ResponseEntity<>("Username or Email allready entered to the system. Please select another username.", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}	
	}
	
	@PostMapping("/secure/userNameValidation")
	public ResponseEntity<String> userNameValidation(@RequestParam String username,@RequestParam long userId) {
		
		boolean isDupplicate = false;
		
		List<User> listUsers = userService.findAll();
		
		if(userId != 0) {
			listUsers.remove(userService.getById(userId));
		}
		
		for (User selectedUser : listUsers) 
		{
			if((selectedUser.getUsername().equals(username))) {
				isDupplicate = true;
				break;
			}
		}
		
		if(isDupplicate) {
			return new ResponseEntity<>("Username allready entered to the system. Please select another username.", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}	
	}
	
	@PostMapping("/secure/emailValidation")
	public ResponseEntity<String> emailValidation(@RequestParam String email,@RequestParam long userId) {
		boolean isDupplicate = false;
		
		
		List<User> listUsers = userService.findAll();
		
		if(userId != 0) {
			listUsers.remove(userService.getById(userId));
		}
		
		for (User selectedUser : listUsers) 
		{
			if((selectedUser.getEmail().equals(email)) || (selectedUser.getUsername().equals(email))) {
				isDupplicate = true;
				break;
			}
		}
		
		if(isDupplicate) {
			return new ResponseEntity<>("Email allready entered to the system. Please select another username.", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}	
	}
	
	
	@GetMapping("/secure/sendMessage")
	public ResponseEntity<String> sendMessage(@RequestParam String data) throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
        EmailDto emailDto = objectMapper.readValue(data, EmailDto.class);
		
        List<User> listAdmins = userService.findAllByRole("ADMIN");
		for(User u : listAdmins) {
			email = new EmailDetails(
					u.getEmail(),
					("New Inquery | " + emailDto.getSubject()),
					("Dear Admin, \n \n Message From : " + emailDto.getName() + "\n\n " + emailDto.getSubject() + " \n \n "+ emailDto.getMessage()));
			emailService.sendMail(email);
		}
		
		email = new EmailDetails(
				emailDto.getEmail(),
				("Inquery Has Been Submitted! "),
				("Dear " + emailDto.getName() + ", " + " \n \n Your message been successfully submitted to admin") + "\n\n " + emailDto.getSubject() + " \n \n "+ emailDto.getMessage());
		emailService.sendMail(email);
		
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
}
