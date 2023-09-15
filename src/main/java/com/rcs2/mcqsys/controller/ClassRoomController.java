package com.rcs2.mcqsys.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.config.MCQAuthenticationSuccessHandler;
import com.rcs2.mcqsys.dto.ClassRoomAnalyzerDto;
import com.rcs2.mcqsys.dto.IdDto;
import com.rcs2.mcqsys.dto.ModuleWiseResults;
import com.rcs2.mcqsys.dto.PaperDto;
import com.rcs2.mcqsys.dto.PaperViewDto;
import com.rcs2.mcqsys.dto.ProfileDto;
import com.rcs2.mcqsys.dto.StudentPaperReviewDetailDto;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoom.ClassroomType;
import com.rcs2.mcqsys.model.ClassRoomBanner;
import com.rcs2.mcqsys.model.ClassRoomMessages;
import com.rcs2.mcqsys.model.ClassRoomPackage;
import com.rcs2.mcqsys.model.ClassRoomPaper;
import com.rcs2.mcqsys.model.ClassRoomPaperReview;
import com.rcs2.mcqsys.model.ClassRoomPaperReviewDetail;
import com.rcs2.mcqsys.model.ClassRoomSessions;
import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.model.EmailDetails;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Lecturer;
import com.rcs2.mcqsys.model.LinkPaperBundle;
import com.rcs2.mcqsys.model.UserPurchase;
import com.rcs2.mcqsys.model.UserRole;
import com.rcs2.mcqsys.model.Notification;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperAnswer;
import com.rcs2.mcqsys.model.PaperBundle;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.model.StudentPaperReviewDetail;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.model.Password;
import com.rcs2.mcqsys.model.Profile;
import com.rcs2.mcqsys.model.StudentParent;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.repository.EmailService;
import com.rcs2.mcqsys.service.ClassRoomBannerService;
import com.rcs2.mcqsys.service.ClassRoomMessagesService;
import com.rcs2.mcqsys.service.ClassRoomPackageService;
import com.rcs2.mcqsys.service.ClassRoomPaperReviewDetailService;
import com.rcs2.mcqsys.service.ClassRoomPaperReviewService;
import com.rcs2.mcqsys.service.ClassRoomPaperService;
import com.rcs2.mcqsys.service.ClassRoomService;
import com.rcs2.mcqsys.service.ClassRoomSessionsService;
import com.rcs2.mcqsys.service.ClassRoomStudentService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.LecturerService;
import com.rcs2.mcqsys.service.LinkPaperBundleService;
import com.rcs2.mcqsys.service.ModuleService;
import com.rcs2.mcqsys.service.NotificationService;
import com.rcs2.mcqsys.service.PaperAnswerService;
import com.rcs2.mcqsys.service.PaperBundleService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.PasswordService;
import com.rcs2.mcqsys.service.ProfileService;
import com.rcs2.mcqsys.service.StudentParentService;
import com.rcs2.mcqsys.service.StudentService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.UserPurchaseService;
import com.rcs2.mcqsys.service.UserRoleService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class ClassRoomController {
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private LecturerService lecturerService;
	
	@Autowired
	private ClassRoomService classRoomService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private SubjectService subjectService;

	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private PublisherService publisherService;
		
	@Autowired
	private ClassRoomPackageService classRoomPackageService;
	
	@Autowired
	private ClassRoomStudentService classRoomStudentService;
	
	@Autowired
	private ClassRoomPaperService classRoomPaperService;
	
	@Autowired
	private ClassRoomSessionsService classRoomSessionsService;
	
	@Autowired
	private ClassRoomMessagesService classRoomMessagesService;
	
	@Autowired
	private ClassRoomBannerService classRoomBannerService;
		
	@Autowired
	private UserPurchaseService userPaperService;
	
	@Autowired
	private PaperService paperService;
		
	@Autowired
	private SimpMessagingTemplate webSoketTemplate;
	
	@Autowired
	private PaperQuestionService paperQuestionService;
	
	@Autowired
	private PaperAnswerService paperAnswerService;
		
	@Autowired
    private PublisherService PublisherService;
	
	@Autowired
    private ClassRoomPaperReviewService classRoomPaperReviewService;
	
	@Autowired
    private ClassRoomPaperReviewDetailService classRoomPaperReviewDetailService;
	
	@Autowired
    private StudentParentService studentParentService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired 
	private EmailService emailService;
	
	@Autowired 
	private NotificationService notificationService;
	
	@Autowired 
	private StudentService studentService;
	
	@Autowired 
	private UserRoleService userRoleService;
	
	@Autowired 
	private ProfileService profileService;
	
	@Autowired 
	private PasswordService passwordService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PaperBundleService paperBundleService;
	
	@Autowired
	private LinkPaperBundleService linkPaperBundleService;
	
	EmailDetails email;
	Notification notification;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	@RequestMapping("/listClassRooms")
	public String listClassRooms(Model model,HttpSession session) {
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<ClassRoom> listClassRooms = new ArrayList<>();
		
		if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			listClassRooms = classRoomService.listAllByLecturer(lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId());
		}
		if ((loggedRoles.contains(roleService.getByRole("SUPERADMIN"))) || loggedRoles.contains(roleService.getByRole("ADMIN"))) {
			listClassRooms = classRoomService.listAll();
		}
		
		if((loggedRoles.contains(roleService.getByRole("SUPERADMIN"))) || loggedRoles.contains(roleService.getByRole("ADMIN")) || loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			model.addAttribute("listClassRooms", listClassRooms);
			return "classroom/classroom-list";
		}
		else {
			return null;
		}
	}
	
	@GetMapping("/addClassRoom")
	public ModelAndView viewAddGrade(HttpSession session) {	

		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		ModelAndView mav = new ModelAndView("classroom/classroom-form");
		
		User user = (User)session.getAttribute("user");
		Profile profile = profileService.getByUserId(user.getUserId());
		
		ClassRoom classroom = new ClassRoom();
		classroom.setCreateBy(user.getUsername());
		classroom.setCreateDate(LocalDate.now());
		classroom.setUpdateBy(user.getUsername());
		classroom.setUpdateDate(LocalDate.now());
		classroom.setStartDate(LocalDate.now());
		classroom.setEndDate(LocalDate.now());
		classroom.setType(ClassroomType.PUBLIC);
		classroom.setStudentCount(0);
		classroom.setPackageId(new ClassRoomPackage());
		classroom.setBodyBgColor(profile.getBodyBgColor());
		classroom.setBodyFontFamily(profile.getBodyFontFamily());
		classroom.setFontColor(profile.getFontColor());
		classroom.setCardBgColor(profile.getCardBgColor());
		classroom.setCardSideColor(profile.getCardSideColor1());
		classroom.setCardSelectedColor(profile.getCardSelectedColor());
		if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			classroom.setLecturerId(lecturerService.getLecturerByUserId(user.getUserId()));
		}else {
			classroom.setLecturerId(null);
		}
		mav.addObject("classroom", classroom);
		
		
		ClassRoomAnalyzerDto resultSummery = new ClassRoomAnalyzerDto();
		mav.addObject("resultSummery", resultSummery);
		
		//Get User
		mav.addObject("user", user);
		
	    //Get all lecturers
  		List<Lecturer> listLecturers = lecturerService.listAll();
  	    mav.addObject("lecturerList", listLecturers);
	    
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
	    
	    //Get all subjects
		List<Subject> listSubjects = subjectService.listAllActive();
	    mav.addObject("subjectList", listSubjects);
	    
	   //Get all packages
  		List<ClassRoomPackage> listPackages = classRoomPackageService.listAllActive();
  		mav.addObject("packageList", listPackages);
  	    
		return mav;
	}
	
	@PostMapping("/secure/saveClassRoom")
	public ResponseEntity<ClassRoom> saveClassRoom(@ModelAttribute ClassRoom classRoom) {
		boolean isDupplicate = false;
		List<ClassRoom> listClassRooms = classRoomService.listAll();
		for (ClassRoom selectedClassRoom : listClassRooms) 
		{
			if((selectedClassRoom.getClassroomId()).equals(classRoom.getClassroomId())) {
				isDupplicate = true;
				break;
			}
		}
		if(!isDupplicate) {
			classRoom.setCreateDate(LocalDate.now());
		}
		//ClassRoomPackage cPackage = classRoom.getPackageId();
		classRoom.setUpdateDate(LocalDate.now());
		classRoom.setLoginId("clz000"+classRoom.getClassroomId());
		classRoom.setEndDate(LocalDate.now());
		//classRoom.setEndDate(classRoom.getStartDate().plusDays(cPackage.getDuration()));
		//classRoom.setAvailableDates((int)Duration.between(LocalDate.now().atStartOfDay(), (classRoom.getStartDate().plusDays(cPackage.getDuration())).atStartOfDay()).toDays());
		classRoomService.save(classRoom);
		
		return new ResponseEntity<>(classRoom, HttpStatus.OK);
	}
	
	@GetMapping("/viewClassRoom")
	public ModelAndView viewClassRoom(@RequestParam Long classroomId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		
		ClassRoom classroom = classRoomService.get(classroomId);
				
		int noOfDays = (int)Duration.between(LocalDate.now().atStartOfDay(), classroom.getEndDate().atStartOfDay()).toDays();
		if(noOfDays < 0) {
			classroom.setExpired(true);
			classroom.setStatus(false);
			classroom.setActive(false);
			classroom.setAvailableDates(0);
		}else {
			classroom.setAvailableDates(noOfDays);
		}
		classRoomService.save(classroom);
		
		User user = new User();
		
		if(session.getAttribute("user") == null) {
			mav = new ModelAndView("classroom/classroom-view");
		}else {
			user = (User)session.getAttribute("user");
			
			if(((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("SUPERADMIN"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("ADMIN"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("LECTURER"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("AUTHOR")))) {
				mav = new ModelAndView("classroom/classroom-form");
				classroom.setUpdateBy(user.getName());
				classroom.setUpdateDate(LocalDate.now());
			}else if(((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("STUDENT"))) || ((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("PARENT")))) {
				mav = new ModelAndView("classroom/classroom-view");
			}else {
				mav = new ModelAndView("classroom/classroom-form");
				classroom.setUpdateBy(user.getName());
				classroom.setUpdateDate(LocalDate.now());
			}
			
		}
		mav.addObject("user", user);	
		mav.addObject("classroom", classroom);
			
		ClassRoomAnalyzerDto resultSummery = new ClassRoomAnalyzerDto();
		for(ClassRoomPaperReview rv : classRoomPaperReviewService.listAllByClassRoom(classroomId)) {
			if(rv.getResult() < 21) {
				resultSummery.setCountCategory1(resultSummery.getCountCategory1()+1);
			}else if(rv.getResult() < 36) {
				resultSummery.setCountCategory2(resultSummery.getCountCategory2()+1);
			}else if(rv.getResult() < 56) {
				resultSummery.setCountCategory3(resultSummery.getCountCategory3()+1);
			}else if(rv.getResult() < 76) {
				resultSummery.setCountCategory4(resultSummery.getCountCategory4()+1);
			}else {
				resultSummery.setCountCategory5(resultSummery.getCountCategory5()+1);
			}
			
			int month = rv.getCreateDate().getMonthValue();
			if(month == 1) {
				resultSummery.setCompletedPaperCount1(resultSummery.getCompletedPaperCount1()+1);	
			}else if(month == 2) {
				resultSummery.setCompletedPaperCount2(resultSummery.getCompletedPaperCount2()+1);	
			}else if(month == 3) {
				resultSummery.setCompletedPaperCount3(resultSummery.getCompletedPaperCount3()+1);	
			}else if(month == 4) {
				resultSummery.setCompletedPaperCount4(resultSummery.getCompletedPaperCount4()+1);	
			}else if(month == 5) {
				resultSummery.setCompletedPaperCount5(resultSummery.getCompletedPaperCount5()+1);	
			}else if(month == 6) {
				resultSummery.setCompletedPaperCount6(resultSummery.getCompletedPaperCount6()+1);	
			}else if(month == 7) {
				resultSummery.setCompletedPaperCount7(resultSummery.getCompletedPaperCount7()+1);	
			}else if(month == 8) {
				resultSummery.setCompletedPaperCount8(resultSummery.getCompletedPaperCount8()+1);	
			}else if(month == 9) {
				resultSummery.setCompletedPaperCount9(resultSummery.getCompletedPaperCount9()+1);	
			}else if(month == 10) {
				resultSummery.setCompletedPaperCount10(resultSummery.getCompletedPaperCount10()+1);	
			}else if(month == 11) {
				resultSummery.setCompletedPaperCount11(resultSummery.getCompletedPaperCount11()+1);	
			}else if(month == 12) {
				resultSummery.setCompletedPaperCount12(resultSummery.getCompletedPaperCount12()+1);	
			}
		}
		
		for(ClassRoomStudent cs : classRoomStudentService.listAllByClassRoom(classroomId)) {
			int month = cs.getCreateDate().getMonthValue();
			if(month == 1) {
				resultSummery.setStudentCount1(resultSummery.getStudentCount1()+1);	
			}else if(month == 2) {
				resultSummery.setStudentCount2(resultSummery.getStudentCount2()+1);	
			}else if(month == 3) {
				resultSummery.setStudentCount3(resultSummery.getStudentCount3()+1);	
			}else if(month == 4) {
				resultSummery.setStudentCount4(resultSummery.getStudentCount4()+1);	
			}else if(month == 5) {
				resultSummery.setStudentCount5(resultSummery.getStudentCount5()+1);	
			}else if(month == 6) {
				resultSummery.setStudentCount6(resultSummery.getStudentCount6()+1);	
			}else if(month == 7) {
				resultSummery.setStudentCount7(resultSummery.getStudentCount7()+1);	
			}else if(month == 8) {
				resultSummery.setStudentCount8(resultSummery.getStudentCount8()+1);	
			}else if(month == 9) {
				resultSummery.setStudentCount9(resultSummery.getStudentCount9()+1);	
			}else if(month == 10) {
				resultSummery.setStudentCount10(resultSummery.getStudentCount10()+1);	
			}else if(month == 11) {
				resultSummery.setStudentCount11(resultSummery.getStudentCount11()+1);	
			}else if(month == 12) {
				resultSummery.setStudentCount12(resultSummery.getStudentCount12()+1);	
			}
		}
		mav.addObject("resultSummery", resultSummery);
		
		//Get all lecturers
		List<Lecturer> listLecturers = lecturerService.listAll();
  	    mav.addObject("lecturerList", listLecturers);
	    
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
	    
	   //Get all subjects
		List<Subject> listsubjects = subjectService.listAllActive();
	    mav.addObject("subjectList", listsubjects);
	    
	   //Get all packages
  		List<ClassRoomPackage> listPackages = classRoomPackageService.listAll();
  		mav.addObject("packageList", listPackages);
  		
  	   //Get all ClassRoomStudents
  		List<ClassRoomStudent> liststudents = classRoomStudentService.listAllByClassRoom(classroomId);
  		mav.addObject("studentList", liststudents);
  		
  		//Get all ClassRoomPapers
  		List<ClassRoomPaper> listPapers = classRoomPaperService.listAllByClassRoom(classroomId);
  		mav.addObject("paperList", listPapers);
  		
  		//Get all LecturerPapers
  		List<UserPurchase> listLecturerPapers = userPaperService.listAllByUser(classroom.getLecturerId().getUserId().getUserId());
  		mav.addObject("lecturerPaperList", listLecturerPapers);
  		
		return mav;
	}
	
	@GetMapping("/deleteClassRoom")
	public ResponseEntity<String> deleteClassRoom(@RequestParam Long classroomId) {
		
		List<ClassRoomPaper> listPapers = classRoomPaperService.listAllByClassRoom(classroomId);
		for (ClassRoomPaper paper : listPapers){
			classRoomPaperService.delete(paper.getClassRoomPaperId());
		}
		
		List<ClassRoomPaperReview> listPaperReviews = classRoomPaperReviewService.listAllByClassRoom(classroomId);
		for (ClassRoomPaperReview review : listPaperReviews){
			classRoomPaperReviewService.delete(review.getCprId());
		}
		
		List<ClassRoomSessions> listSessions = classRoomSessionsService.listAllByClassRoom(classroomId);
		for (ClassRoomSessions session : listSessions){
			classRoomSessionsService.delete(session.getCsId());
		}
		
		List<ClassRoomStudent> listStudents = classRoomStudentService.listAllByClassRoom(classroomId);
		for (ClassRoomStudent student : listStudents){
			classRoomStudentService.delete(student.getClassRoomStudentId());
		}
		
		List<ClassRoomMessages> listMessages = classRoomMessagesService.listAllByClassRoom(classroomId);
		for (ClassRoomMessages message : listMessages){
			classRoomMessagesService.delete(message.getCmId());
		}
		
		classRoomService.delete(classroomId);
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/classRoomDeactive")
	public ResponseEntity<String> paperDeactive(@RequestParam Long classroomId) {
		
		ClassRoom classroom = classRoomService.get(classroomId);
		classroom.setActive(false);
		classroom.setStatus(false);
		
		classRoomService.save(classroom);
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	

	@GetMapping("/classRoomActive")
	public ResponseEntity<String> paperActive(@RequestParam Long classroomId) {
		
		ClassRoom classroom = classRoomService.get(classroomId);
		if(classroom.isExpired()) {
			return new ResponseEntity<>("expired", HttpStatus.OK);
		}else {
			classroom.setActive(true);
			classroom.setStatus(false);
			
			classRoomService.save(classroom);
			
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
	}
	
	@GetMapping("/stopClassRoom")
	public ResponseEntity<String> stopClassRoom(@RequestParam Long classroomId) {
		
		ClassRoom classroom = classRoomService.get(classroomId);
		classroom.setStatus(false);
		
		classRoomService.save(classroom);
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/startClassRoom")
	public ResponseEntity<String> startClassRoom(@RequestParam Long classroomId) {
		
		ClassRoom classroom = classRoomService.get(classroomId);
		
		if(classroom.isExpired()) {
			return new ResponseEntity<>("expired", HttpStatus.OK);
		}else if(!classroom.isActive()) {
			return new ResponseEntity<>("notActive", HttpStatus.OK);
		}else{
			classroom.setStatus(true);
			classRoomService.save(classroom);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
	}
	
	@PostMapping("/secure/updateClassRoomTheme")
	public ResponseEntity<Object> updateClassRoomTheme(@ModelAttribute ProfileDto profileDto,HttpSession session) {

		ClassRoom classroom = classRoomService.get(profileDto.getProfileId());

		if(profileDto.getType().equals("new")) {
			classroom.setBodyBgColor(profileDto.getBodyBgColor());
			classroom.setBodyFontFamily(profileDto.getBodyFontFamily());
			classroom.setFontColor(profileDto.getFontColor());
			classroom.setCardBgColor(profileDto.getCardBgColor());
			classroom.setCardSideColor(profileDto.getCardSideColor1());
			classroom.setCardSelectedColor(profileDto.getCardSelectedColor());
		}else if(profileDto.getType().equals("default")) {
			classroom.setBodyBgColor("#f6f9ff");
			classroom.setBodyFontFamily("Nunito, sans-serif");
			classroom.setFontColor("#000");
			classroom.setCardBgColor("#ffffff");
			classroom.setCardSideColor("#e1f0fa");
			classroom.setCardSelectedColor("#e1f0fc");
		}		

		classRoomService.save(classroom);
		
		return new ResponseEntity<>(classroom, HttpStatus.OK);
	}
	
	@GetMapping("/listClassromStudents")
	public ModelAndView listClassromStudents(@RequestParam Long classroomId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("classroom/classroom-student-list");
		
		mav.addObject("classroom", classRoomService.get(classroomId));
		
		//Get all ClassRoomStudents
  		List<ClassRoomStudent> liststudents = classRoomStudentService.listAllByClassRoom(classroomId);
  		mav.addObject("studentList", liststudents);
  		
		return mav;
	}
	
	@GetMapping("/listClassromPapers")
	public ModelAndView listClassromPapers(@RequestParam Long classroomId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("classroom/classroom-paper-list_OLD");
		
		mav.addObject("classroom", classRoomService.get(classroomId));
		
		//Get all ClassRoomPapers
  		List<ClassRoomPaper> listPapers = classRoomPaperService.listAllByClassRoom(classroomId);
  		mav.addObject("paperList", listPapers);
  		
  		//Get all LecturerPapers
  		List<UserPurchase> listLecturerPapers = userPaperService.listAllByUser(classRoomService.get(classroomId).getLecturerId().getUserId().getUserId());
  		mav.addObject("lecturerPaperList", listLecturerPapers);
  		
		return mav;
	}
	
	@GetMapping("/addClassRoomStudent")
	public ResponseEntity<String> addClassRoomStudent(@RequestParam Long classroomId, @RequestParam String studentEmail, @RequestParam Long userid, HttpSession session) {
		
		boolean isDupplicate = false;
			
		ClassRoom classRoom = classRoomService.get(classroomId);
		User user = (User)session.getAttribute("user");
		Student student = new Student();
		User studentUser = new User();
		
		if(userid != 0) {
			student = studentService.getStudentByUserId(userid);
		}else if((userService.getCountByEmail(studentEmail)) > 0){
			studentUser = userService.getStudentsByEmail(studentEmail);
			student = studentService.getStudentByUserId(studentUser.getUserId());
		}else {
			studentUser = new User();
			studentUser.setActive(true);
			studentUser.setEmail(studentEmail);
			studentUser.setGender("male");
			studentUser.setMobile("");
			studentUser.setName(studentEmail);
			studentUser.setUsername(studentEmail);
			studentUser.setCreateBy(user.getUsername());
			studentUser.setCreateDate(LocalDate.now());
			studentUser.setUpdateBy(user.getUsername());
			studentUser.setUpdateDate(LocalDate.now());
			studentUser.setForcePassword(true);
			studentUser.setSso(false);
			studentUser.setSsoEmail(null);
			userService.save(studentUser);
			
			UserRole uRole = new UserRole();
			uRole.setUserId(studentUser);
			uRole.setRoleId(roleService.getByRole("STUDENT"));
			userRoleService.save(uRole);
			
			Password password = new Password();
			password.setUserId(studentUser);
			password.setPassword(passwordEncoder.encode("1qaz@WSX"));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			Profile profile = new Profile();
			profile.setUser(studentUser);
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
			
			student = new Student();
			student.setAttemptsAllowed(10);
			student.setGpa(0);
			student.setGrade(classRoom.getGrade());
			student.setUserId(studentUser);
			studentService.save(student);
		}
			
		if(!(student).equals(null)) {
			List<ClassRoomStudent> listStudent = classRoomStudentService.listAllByClassRoom(classroomId);
			for (ClassRoomStudent selectedStudent : listStudent) 
			{
				if(selectedStudent.getStudentId().getStudentId() == student.getStudentId()) {
					isDupplicate = true;
					break;
				}
			}
			
			if(!isDupplicate) {
				ClassRoomStudent std = new ClassRoomStudent();
				std.setClassRoomId(classRoomService.get(classroomId));
				std.setActive(true);
				std.setStatus(false);
				std.setCreateBy(user.getUsername());
				std.setCreateDate(LocalDate.now());
				std.setUpdateBy(user.getUsername());
				std.setUpdateDate(LocalDate.now());
				std.setStudentId(student);

				classRoomStudentService.save(std);
				
				classRoom.setStudentCount(classRoom.getStudentCount() + 1);
				classRoomService.save(classRoom);
						
				EmailDetails email = new EmailDetails();
				email.setRecipient(studentEmail);
				email.setSubject("New Group");
				email.setMsgBody("You have been added To The Groups. Please join to the group using folowing details. \n\n Group ID : " + classRoom.getLoginId() + "\n Passcode : " + classRoom.getPassword());
				
			}
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/registerClassRoomStudent")
	public ResponseEntity<String> registerClassRoomStudent(@RequestParam Long classroomId, @RequestParam String email,HttpSession session) {
					
		ClassRoom classRoom = classRoomService.get(classroomId);
		boolean isDupplicate = false;
		Student student = new Student();
		User studentUser = new User();
		
		if(MCQAuthenticationSuccessHandler.getName() == "anonymousUser") {
			
			if(userService.getStudentCountByEmail(email) > 0) {
				return new ResponseEntity<>("Dupplicate", HttpStatus.OK);
			}
			
			studentUser = new User();
			studentUser.setActive(true);
			studentUser.setEmail(email);
			studentUser.setGender("male");
			studentUser.setMobile("");
			studentUser.setName(email);
			studentUser.setUsername(email);
			studentUser.setCreateBy(email);
			studentUser.setCreateDate(LocalDate.now());
			studentUser.setUpdateBy(email);
			studentUser.setUpdateDate(LocalDate.now());
			studentUser.setForcePassword(true);
			studentUser.setSso(false);
			studentUser.setSsoEmail(null);
			userService.save(studentUser);
			
			UserRole uRole = new UserRole();
			uRole.setUserId(studentUser);
			uRole.setRoleId(roleService.getByRole("STUDENT"));
			userRoleService.save(uRole);
			
			Password password = new Password();
			password.setUserId(studentUser);
			password.setPassword(passwordEncoder.encode("1qaz@WSX"));
			password.setActive(true);
			password.setLastUpdated(LocalDate.now());
			passwordService.save(password);
			
			Profile profile = new Profile();
			profile.setUser(studentUser);
			profile.setProfilePic(null);
			profile.setCoverImage(null);
			profileService.save(profile);
			
			student = new Student();
			student.setAttemptsAllowed(10);
			student.setGpa(0);
			student.setGrade(classRoom.getGrade());
			student.setUserId(studentUser);
			studentService.save(student);
			
		}else {
			User user = (User)session.getAttribute("user");
			student = studentService.getStudentByUserId(user.getUserId());
			studentUser = user;
		}
		
		if(!(student).equals(null)) {
			List<ClassRoomStudent> listStudent = classRoomStudentService.listAllByClassRoom(classroomId);
			for (ClassRoomStudent selectedStudent : listStudent) 
			{
				if(selectedStudent.getStudentId().getStudentId() == student.getStudentId()) {
					isDupplicate = true;
					break;
				}
			}
			
			if(!isDupplicate) {
				ClassRoomStudent std = new ClassRoomStudent();
				std.setClassRoomId(classRoomService.get(classroomId));
				std.setActive(true);
				std.setStatus(false);
				std.setCreateBy(studentUser.getUsername());
				std.setCreateDate(LocalDate.now());
				std.setUpdateBy(studentUser.getUsername());
				std.setUpdateDate(LocalDate.now());
				std.setStudentId(student);
				
				classRoomStudentService.save(std);
				
				classRoom.setStudentCount(classRoom.getStudentCount() + 1);
				classRoomService.save(classRoom);
				
				EmailDetails e = new EmailDetails();
				e.setRecipient(email);
				e.setSubject("New Group");
				e.setMsgBody("You have been added To The Group. Please join to the group using folowing details. \n\n Group ID : " + classRoom.getLoginId() + "\n Passcode : " + classRoom.getPassword());
				
			}
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/viewStudentClassRoomResult")
	public ModelAndView viewStudentClassRoomResult(@RequestParam long classroomId,@RequestParam long studentId,HttpSession session) {
		ModelAndView mav = new ModelAndView("classroom/result-list");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		Student student = studentService.get(studentId);
		mav.addObject("student", student);
		
		//Get all Reviews
		List<ClassRoomPaperReview> listReviews = classRoomPaperReviewService.listByStudentAndClassroom(classroomId,student.getStudentId());
		mav.addObject("listReviews", listReviews);
		
		//Get all Reviews
		List<Subject> listSubjects = subjectService.listByClassRoomPaperReview(student.getStudentId());
		mav.addObject("listSubjects", listSubjects);
		
		mav.addObject("classroom", classRoomService.get(classroomId));
		mav.addObject("classroomStudent", classRoomStudentService.getByStudentId(classroomId,student.getStudentId()));
		
		return mav;
	}
	
	@PostMapping("/secure/addClassRoomPaperList")
	public ResponseEntity<String> addClassRoomPaperList(@ModelAttribute IdDto papers,HttpSession session) {
		//System.out.println("classroomId : " + classroomId + " | selectedPapers : " + selectedPapers + " | selectedBundles : " + selectedBundles);
		System.out.println(" selectedPapers : " + papers);
//		boolean isDupplicate = false;
//		ResponseEntity<String> x;
//				
//		User user = (User)session.getAttribute("user");
//
//		if(!selectedPapers.isEmpty()) {
//			for(Long pId : selectedPapers) {
//				List<ClassRoomPaper> listPapers = classRoomPaperService.listAllByClassRoom(classroomId);
//				for (ClassRoomPaper selectedPaper : listPapers) 
//				{
//					//System.out.println((selectedPaper.getPaperID() != null));
//					
//					if((selectedPaper.getPaperID() != null) && !((selectedPaper.getPaperID().getPaperId()).equals(pId))) {
//						System.out.println("selectedPaper.getPaperID().getPaperId() " + selectedPaper.getPaperID().getPaperId() + " | pId " + pId + !((selectedPaper.getPaperID().getPaperId()).equals(pId)));
//						ClassRoomPaper clp = new ClassRoomPaper();
//						clp.setClassRoomId(classRoomService.get(classroomId));
//						clp.setPaperID(paperService.get(pId));
//						clp.setBundleId(null);
//						clp.setActive(true);
//						clp.setStatus(false);
//						clp.setCreateBy(user.getUsername());
//						clp.setCreateDate(LocalDate.now());
//						clp.setUpdateBy(user.getUsername());
//						clp.setUpdateDate(LocalDate.now());
//						
//						//classRoomPaperService.save(clp);
//					}
//				}
//			}
//		}
//		
//		if(!selectedBundles.isEmpty()) {
//			for(Long bId : selectedBundles) {
//				List<ClassRoomPaper> listPapers = classRoomPaperService.listAllByClassRoom(classroomId);
//				for (ClassRoomPaper selectedPaper : listPapers) 
//				{
//					
//					if((selectedPaper.getBundleId() != null) && !((selectedPaper.getBundleId().getBundleId()).equals(bId))) {
//						System.out.println("(selectedPaper.getBundleId().getBundleId() " + (selectedPaper.getBundleId().getBundleId() + " | bId " + bId + !((selectedPaper.getBundleId().getBundleId()).equals(bId))));
//						ClassRoomPaper clp = new ClassRoomPaper();
//						clp.setClassRoomId(classRoomService.get(classroomId));
//						clp.setPaperID(null);
//						clp.setBundleId(paperBundleService.get(bId));
//						clp.setActive(true);
//						clp.setStatus(false);
//						clp.setCreateBy(user.getUsername());
//						clp.setCreateDate(LocalDate.now());
//						clp.setUpdateBy(user.getUsername());
//						clp.setUpdateDate(LocalDate.now());
//						
//						//classRoomPaperService.save(clp);
//					}
//				}
//			}
//		}
//		
//		
//		
//		List<ClassRoomStudent> classRoomStudentList = classRoomStudentService.listAllByClassRoom(classroomId);
//		
//		for(ClassRoomStudent std : classRoomStudentList) {
//			StudentParent student = new StudentParent();
//			String status;
//			String emailAddress = std.getStudentId().getUserId().getEmail();
//			
//			if((userService.getCountByEmail(emailAddress)) > 0) {
//				student = studentParentService.getByStudentEmail(emailAddress);
//				email = new EmailDetails(
//						emailAddress,
//						("New Paper Added! | " + classRoomService.get(classroomId).getName() + " | " + LocalDate.now()),
//						("Dear Student, \n\n New paper has been added to your classroom " + classRoomService.get(classroomId).getName() + ". Please login to the classroom and refer paper."));
//				emailService.sendMail(email);
//						
//			}
//
//			if((userService.getParentCountByStudent(emailAddress)) > 0) {
//				email = new EmailDetails(
//						student.getParentId().getEmail(),
//						("New Paper Added! | " + student.getStudentId().getName() + " | " + classRoomService.get(classroomId).getName() + " | " + LocalDate.now()),
//						("Dear Parent, \n\n New paper has been added to classroom " + classRoomService.get(classroomId).getName() + ". Please make sure that your child will login to the classroom and refer paper."));
//				emailService.sendMail(email);
//				
//				notification = new Notification(
//						student.getParentId(),
//						("New Paper Added! | " + student.getStudentId().getName() + " | " + classRoomService.get(classroomId).getName() + " | " + LocalDate.now()),
//						("New paper has been added to classroom " + classRoomService.get(classroomId).getName() + ". Please make sure that your child will login to the classroom and refer paper."));
//				notificationService.save(notification,session);
//			}
//		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/addClassRoomPaper")
	public ResponseEntity<String> addClassRoomPaper(@RequestParam Long classroomId , @RequestParam Long paperId,@RequestParam Long bundleId,HttpSession session) {
		boolean isDupplicate = false;
		ResponseEntity<String> x;
		
		User user = (User)session.getAttribute("user");

		List<ClassRoomPaper> listPapers = classRoomPaperService.listAllByClassRoom(classroomId);
		for (ClassRoomPaper selectedPaper : listPapers) 
		{
			if(((selectedPaper.getPaperID() != null) && (selectedPaper.getPaperID().getPaperId()).equals(paperId)) || ((selectedPaper.getBundleId() != null) && (selectedPaper.getBundleId().getBundleId()).equals(bundleId))) {
				isDupplicate = true;
				break;
			}
		}
		
		if(!isDupplicate) {
			ClassRoomPaper clp = new ClassRoomPaper();
			clp.setClassRoomId(classRoomService.get(classroomId));
			if(paperId == 0) {
				clp.setPaperID(null);
				clp.setBundleId(paperBundleService.get(bundleId));
			}else if(bundleId == 0) {
				clp.setPaperID(paperService.get(paperId));
				clp.setBundleId(null);
			}
			
			clp.setActive(true);
			clp.setStatus(false);
			clp.setCreateBy(user.getUsername());
			clp.setCreateDate(LocalDate.now());
			clp.setUpdateBy(user.getUsername());
			clp.setUpdateDate(LocalDate.now());
			
			classRoomPaperService.save(clp);
			
			List<ClassRoomStudent> classRoomStudentList = classRoomStudentService.listAllByClassRoom(classroomId);
			
			for(ClassRoomStudent std : classRoomStudentList) {
				
				StudentParent student = new StudentParent();
				
				//EmailDetails email = new EmailDetails();
				String status;
				String emailAddress = std.getStudentId().getUserId().getEmail();
				if((userService.getCountByEmail(emailAddress)) > 0) {
					student = studentParentService.getByStudentEmail(emailAddress);
					

					email = new EmailDetails(
							emailAddress,
							("New Paper Added! | " + classRoomService.get(classroomId).getName() + " | " + LocalDate.now()),
							("Dear Student, \n\n New paper has been added to your classroom " + classRoomService.get(classroomId).getName() + ". Please login to the classroom and refer paper."));
					emailService.sendMail(email);
							
				}
						
				if((userService.getParentCountByStudent(emailAddress)) > 0) {
					
					email = new EmailDetails(
							student.getParentId().getEmail(),
							("New Paper Added! | " + student.getStudentId().getName() + " | " + classRoomService.get(classroomId).getName() + " | " + LocalDate.now()),
							("Dear Parent, \n\n New paper has been added to classroom " + classRoomService.get(classroomId).getName() + ". Please make sure that your child will login to the classroom and refer paper."));
					emailService.sendMail(email);
					
					notification = new Notification(
							student.getParentId(),
							("New Paper Added! | " + student.getStudentId().getName() + " | " + classRoomService.get(classroomId).getName() + " | " + LocalDate.now()),
							("New paper has been added to classroom " + classRoomService.get(classroomId).getName() + ". Please make sure that your child will login to the classroom and refer paper."));
					notificationService.save(notification,session);
				}
			}
			
			x = new ResponseEntity<>("success", HttpStatus.OK);
		}else {
			x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return x ;
	}
	
	@GetMapping("/sessionClose")
	public ResponseEntity<String> sessionClose(@RequestParam Long classroomStudentId) {
		
		ClassRoomStudent student = classRoomStudentService.get(classroomStudentId);
		student.setStatus(false);
		classRoomStudentService.save(student);
				
		webSoketTemplate.convertAndSend("/topic/sessionClose",classroomStudentId);
				
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/sendRequest")
	public ResponseEntity<String> sendRequest(@RequestParam Long classroomStudentId) {
		
		ClassRoomStudent student = classRoomStudentService.get(classroomStudentId);
		
		email = new EmailDetails(
				student.getStudentId().getUserId().getEmail(),
				("Request To Join To The Group"),
				("Your QuizzMart group has been started. Please join to the group using folowing details. \n\n Group ID : " + student.getClassRoomId().getLoginId() + "\n Passcode : " + student.getClassRoomId().getPassword()));
		emailService.sendMail(email);
		
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/removeStudent")
	public ResponseEntity<String> removeStudent(@RequestParam Long classroomStudentId) {
		
		ClassRoom classroom = classRoomStudentService.get(classroomStudentId).getClassRoomId();
		
		classRoomStudentService.delete(classroomStudentId);
		
		classroom.setStudentCount(classroom.getStudentCount()-1);
		classRoomService.save(classroom);

		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/deactiveClassRoomPaper")
	public ResponseEntity<String> deactiveClassRoomPaper(@RequestParam Long classroomPaperId) {
		
		ClassRoomPaper paper = classRoomPaperService.get(classroomPaperId);
		paper.setActive(false);
		paper.setStatus(false);
		
		classRoomPaperService.save(paper);
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/activeClassRoomPaper")
	public ResponseEntity<String> activeClassRoomPaper(@RequestParam Long classroomPaperId) {
		
		ClassRoomPaper paper = classRoomPaperService.get(classroomPaperId);
		paper.setActive(true);
		
		classRoomPaperService.save(paper);
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/removeClassRoomPaper")
	public ResponseEntity<String> removeClassRoomPaper(@RequestParam Long classroomPaperId) {
		
		classRoomPaperService.delete(classroomPaperId);
		
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/classRoomLogin")
	public ResponseEntity<String> classRoomLogin(@RequestParam String logginId,@RequestParam String username,@RequestParam String passcode) {
		String classroomStudentId = null;
		
		ClassRoom classRoom = classRoomService.getByLogginId(logginId);
		if(classRoom.getPassword().equals(passcode)) {
			if(classRoom.isStatus()) {
				Student student = studentService.getStudentByUserId((userService.getByUserName(username)).getUserId());
				ClassRoomStudent classroomStudent = classRoomStudentService.getByStudentId(classRoom.getClassroomId(),student.getStudentId());
				classroomStudentId = Long.toString(classroomStudent.getClassRoomStudentId());
				ResponseEntity<String> x = new ResponseEntity<>(classroomStudentId, HttpStatus.OK);
				return x;
			}else {
				ResponseEntity<String> x = new ResponseEntity<>("not-started",HttpStatus.OK);
				return x;
			}
		}else {
			ResponseEntity<String> x = new ResponseEntity<>("password-incorrect",HttpStatus.OK);
			return x;
		}
	}
	
	@GetMapping("/student/classroomLogin")
	public ModelAndView classroomLogin(@RequestParam Long classroomStudentId,HttpSession session) {
		ModelAndView mav = new ModelAndView("classroom/student-panel");

		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		ClassRoom classroom = classRoomStudentService.get(classroomStudentId).getClassRoomId();
		
		ClassRoomStudent classroomStudent = classRoomStudentService.get(classroomStudentId);
		classroomStudent.setStatus(true);
		classRoomStudentService.save(classroomStudent);
		mav.addObject("classroomStudent", classroomStudent);
		
		List<ClassRoomPaper> paperList = classRoomPaperService.listAllActive(classroom.getClassroomId());
		mav.addObject("paperList", paperList);
		
		List<ClassRoomBanner> bannerList = classRoomBannerService.listAllByClassroom(classroom.getClassroomId());
		mav.addObject("bannerList", bannerList);
		
		List<ClassRoomStudent> loggedstudents = classRoomStudentService.listAllOnline(classroomStudent.getClassRoomId().getClassroomId());
		mav.addObject("loggedstudents", loggedstudents);
		ClassRoomSessions activeSession = new ClassRoomSessions();
		activeSession.setUserId(user);
		activeSession.setLoginEmail(classroomStudent.getStudentId().getUserId().getEmail());
		activeSession.setClassroomId(classroomStudent.getClassRoomId());
		activeSession.setActive(true);
		activeSession.setStartTime(LocalTime.now());
		activeSession.setEndTime(null);
		activeSession.setDate(LocalDate.now());
		classRoomSessionsService.save(activeSession);
		mav.addObject("activeSession", activeSession);
		String time = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond();
		mav.addObject("time", time);
		return mav;
		
	}
	
	@RequestMapping("/secure/classRoomLogin")
	public ResponseEntity<Long> classRoomLogin(@RequestParam long groupId,@RequestParam String password,HttpSession session) {
		User user = (User)session.getAttribute("user");
		
		try {
			ClassRoom classroom = classRoomService.get(groupId);
			ClassRoomStudent clsStudent = classRoomStudentService.getByStudentId(classroom.getClassroomId(), studentService.getStudentByUserId(user.getUserId()).getStudentId());
			
			if(classroom.getPassword().equals(password)) {
				return new ResponseEntity<>(clsStudent.getClassRoomStudentId(), HttpStatus.OK);
			}else {
				return new ResponseEntity<>((long)0, HttpStatus.OK);
			}
		}catch(Exception e) {
			return new ResponseEntity<>((long)0, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/student/classroomLogout")
	public String classroomLogout(@RequestParam Long classroomStudentId,@RequestParam Long activeSessionId) {
		
		ClassRoomStudent classroomStudent = classRoomStudentService.get(classroomStudentId);
		classroomStudent.setStatus(false);
		classRoomStudentService.save(classroomStudent);
		
		ClassRoomSessions activeSession = classRoomSessionsService.get(activeSessionId);
		activeSession.setActive(false);
		activeSession.setEndTime(LocalTime.now());
		classRoomSessionsService.save(activeSession);
		
		return "redirect:/listStudentClassroom";
	}
	
	@GetMapping("/classroom/viewPaper")
	public ModelAndView classroomViewPaper(@RequestParam Long paperId,@RequestParam Long classroomId , @RequestParam Long classRoomStudentId ,  @RequestParam Long activeSessionId,HttpSession session) {
		int questionCount = 0;
		
		ModelAndView mav = new ModelAndView("classroom/paper-attend");
		
		Paper paper = paperService.get(paperId);
		
		List<PaperQuestion> listQuestions = paperQuestionService.listAllByPaperId(paperId);		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paperId);	
		
		User user = classRoomStudentService.get(classRoomStudentId).getStudentId().getUserId();
		mav.addObject("user", user);
		
		ClassRoomPaperReview review = new ClassRoomPaperReview();
		review.setCreateBy(user.getUsername());
		review.setCreateDate(LocalDate.now());
		review.setUpdateBy(user.getUsername());
		review.setUpdateDate(LocalDate.now());
		review.setPaperID(paper);
		review.setSeatedDate(LocalDate.now());
		review.setResult(0);	
		review.setStudentId(classRoomStudentService.get(classRoomStudentId).getStudentId());
		review.setStudentEmail(classRoomStudentService.get(classRoomStudentId).getStudentId().getUserId().getEmail());
		review.setClassRoomId(classRoomService.get(classroomId));
		classRoomPaperReviewService.save(review);
		
		Subject subject = subjectService.get(paper.getSubjectId().getSubjectId());
		mav.addObject("subject", subject);
		
		Publisher publisher = PublisherService.get(paper.getPublisher().getPoId());
		mav.addObject("publisher", publisher);
		
		List<PaperViewDto> listPaperQuestions = new ArrayList();
		
		for(PaperQuestion q:listQuestions) {
			questionCount++;
			
			PaperViewDto pv = new PaperViewDto();
			pv.setPqId(q.getPqId());
			pv.setQuestionId(q.getQuestionId());
			pv.setPaperID(q.getPaperID());
			pv.setModuleId(q.getModuleId());
			pv.setQuestion(q.getQuestion());
			pv.setImage(q.getImage());
			
			/*
			if(q.getImage() != null) {
				String path = (imageLocation + q.getImage()).replace('/', '\\');
				String base64String = null;
				
				byte[] byteData;
				try {
					byteData = Files.readAllBytes(Paths.get(path));
					base64String = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(byteData);
					pv.setBase64Img(base64String);
				} catch (IOException e) {
					e.printStackTrace();
					pv.setBase64Img(null);
				}
			}
			*/
			listPaperQuestions.add(pv);
		}
		
		mav.addObject("review", review);
		mav.addObject("paper", paper);
		mav.addObject("listQuestions", listPaperQuestions);
		mav.addObject("listAnswers", listPaperAnswers);
		mav.addObject("subject", paper.getSubjectId());
		mav.addObject("questionCount", questionCount);
		
		ClassRoom classroom = classRoomService.get(classroomId);
		classroom.setUpdateBy(user.getName());
		classroom.setUpdateDate(LocalDate.now());
		mav.addObject("classroom", classroom);
		
		ClassRoomStudent classroomStudent = classRoomStudentService.get(classRoomStudentId);
		classroomStudent.setStatus(true);
		classRoomStudentService.save(classroomStudent);
		mav.addObject("classroomStudent", classroomStudent);
		
		mav.addObject("activeSession", classRoomSessionsService.get(activeSessionId));
		
		return mav;
	}
	
	@GetMapping("classroom/viewBundle")
	public ModelAndView viewBundle(@RequestParam Long bundleId,@RequestParam Long classroomId , @RequestParam Long classRoomStudentId ,  @RequestParam Long activeSessionId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("classroom/bundle-view");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		PaperBundle bundle = paperBundleService.get(bundleId);
		mav.addObject("bundle", bundle);
		
		List<LinkPaperBundle> listPapers = linkPaperBundleService.listAllByBundleId(bundleId);	
		mav.addObject("listPapers", listPapers);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);
		
		//Get all publishers
		List<Publisher> listPublishers = publisherService.listAllActive();
		mav.addObject("listPublishers", listPublishers);
		 
		mav.addObject("classroom", classRoomService.get(classroomId));
		mav.addObject("classroomStudent", classRoomStudentService.get(classRoomStudentId));
		mav.addObject("activeSession", classRoomSessionsService.get(activeSessionId));
		return mav;
	}
	
	@PostMapping("/secure/saveClassRoomReview")
	public ResponseEntity<String> saveClassRoomReview(@ModelAttribute ClassRoomPaperReview review) {
		review.setUpdateDate(LocalDate.now());
		classRoomPaperReviewService.save(review);
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x ;
	}
	
	@PostMapping("/secure/saveClassRoomReviewDetail")
	public ResponseEntity<String> saveClassRoomReviewDetail(@ModelAttribute StudentPaperReviewDetailDto reviewDetail) {
				
		ClassRoomPaperReviewDetail detail = new ClassRoomPaperReviewDetail();
		detail.setActive(true);
		detail.setCorrectAnswer(paperAnswerService.get(reviewDetail.getCorrectAnswer()));
		detail.setDate(LocalDate.now());
		if(reviewDetail.getGivenAnswer() != 0) {
			detail.setGivenAnswer(paperAnswerService.get(reviewDetail.getGivenAnswer()));
		}else {
			detail.setGivenAnswer(null);
		}
		
		detail.setModuleId(moduleService.get(reviewDetail.getModuleId()));
		detail.setPaperID(paperService.get(reviewDetail.getPaperID()));
		detail.setQuestionId(paperQuestionService.get(reviewDetail.getQuestionId()));
		detail.setResult(reviewDetail.getResult());
		detail.setStatus(reviewDetail.isStatus());
		detail.setStudentId(studentService.get(reviewDetail.getStudentId()));		
		detail.setReviewId(classRoomPaperReviewService.get(reviewDetail.getReviewId()));
		classRoomPaperReviewDetailService.save(detail);
				
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/reviewClassRoomPaper")
	public ModelAndView reviewClassRoomPaper(@RequestParam Long reviewId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("classroom/paper-review");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		ClassRoomPaperReview review = classRoomPaperReviewService.get(reviewId);
		Paper paper = review.getPaperID();
		List<ClassRoomPaperReviewDetail> listReviews = classRoomPaperReviewDetailService.listAllByReviewId(reviewId);		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paper.getPaperId());		
		
		List<ModuleWiseResults> listModuleResults = new ArrayList<ModuleWiseResults>();
		
		for(long m : paperQuestionService.listAllModulesByPaper(paper.getPaperId())) {
			int allQuestionCount = 0;
			int answeredQuestionCount = 0;
			int correctAnsweredCount = 0;
			
			for(ClassRoomPaperReviewDetail reviewDetail : classRoomPaperReviewDetailService.listAllByReviewAndModule(reviewId,m)) {
				allQuestionCount++;
				if(reviewDetail.getGivenAnswer() != null)
					answeredQuestionCount++;
				if(reviewDetail.isStatus())
					correctAnsweredCount++;
			}
			
			ModuleWiseResults mr = new ModuleWiseResults();
			mr.setAllQuestionCount(allQuestionCount);
			mr.setAnsweredQuestionCount(answeredQuestionCount);
			mr.setCorrectAnsweredCount(correctAnsweredCount);
			mr.setModuleId(moduleService.get(m));
			mr.setPaperId(paper.getPaperId());
			mr.setResult(((double)correctAnsweredCount/(double)paperQuestionService.getCountByPaper(paper.getPaperId()))*100.00);
			listModuleResults.add(mr);
		}
		
		mav.addObject("listModuleResults", listModuleResults);						
		mav.addObject("listReviews", listReviews);
		mav.addObject("paper", paper);
		mav.addObject("listAnswers", listPaperAnswers);
		mav.addObject("subject", paper.getSubjectId());
		mav.addObject("publisher", paper.getPublisher());
		mav.addObject("classroom", review.getClassRoomId());
		mav.addObject("classroomStudent", classRoomStudentService.getByStudentId(review.getClassRoomId().getClassroomId(), review.getStudentId().getStudentId()));
		mav.addObject("review", review);
	
		return mav;
	}
	
	@GetMapping("checkClassRoomStudentAvailability")
	public ResponseEntity<Boolean> checkClassRoomStudentAvailability(@RequestParam String email,@RequestParam Long classroomId) {		
		int count = classRoomStudentService.getByStudentEmail(classroomId,email);
		if(count > 0) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
	}
}
