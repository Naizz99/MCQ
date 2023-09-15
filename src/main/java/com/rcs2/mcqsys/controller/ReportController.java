package com.rcs2.mcqsys.controller;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcs2.mcqsys.dto.ClassRoomPaperDto;
import com.rcs2.mcqsys.dto.ClassRoomStudentDto;
import com.rcs2.mcqsys.dto.ModuleResultDto;
import com.rcs2.mcqsys.dto.PaperReportDto;
import com.rcs2.mcqsys.dto.PublisherDto;
import com.rcs2.mcqsys.dto.PurchaseDto;
import com.rcs2.mcqsys.dto.QuestionListDto;
import com.rcs2.mcqsys.dto.StudentDto;
import com.rcs2.mcqsys.dto.StudentPaperReviewDto;
import com.rcs2.mcqsys.dto.SubjectWiseModelResultsDto;
import com.rcs2.mcqsys.dto.UserDto;
import com.rcs2.mcqsys.dto.UserPaperDto;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoomPaper;
import com.rcs2.mcqsys.model.ClassRoomSessions;
import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Login;
import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.PublisherUser;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.model.StudentPaperReviewDetail;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.UserPurchase;
import com.rcs2.mcqsys.service.ClassRoomPaperReviewService;
import com.rcs2.mcqsys.service.ClassRoomPaperService;
import com.rcs2.mcqsys.service.ClassRoomService;
import com.rcs2.mcqsys.service.ClassRoomSessionsService;
import com.rcs2.mcqsys.service.ClassRoomStudentService;
import com.rcs2.mcqsys.service.CommonService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.LecturerService;
import com.rcs2.mcqsys.service.ModuleService;
import com.rcs2.mcqsys.service.UserPurchaseService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.StudentPaperReviewDetailService;
import com.rcs2.mcqsys.service.StudentPaperReviewService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.StudentService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class ReportController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private LecturerService lecturerService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
    private PublisherService publisherService;
	
	@Autowired
    private PublisherUserService publisherUserService;
	
	@Autowired
    private PaperService paperService;
	
	@Autowired
	private ClassRoomService classRoomService;
	
	@Autowired
	private ClassRoomStudentService classRoomStudentService;
	
	@Autowired
	private ClassRoomPaperService classRoomPaperService;
	
	@Autowired
	private ClassRoomSessionsService classRoomSessionsService;
	
	@Autowired
	private UserPurchaseService lecturerPaperService;
		
	@Autowired
	private ClassRoomPaperReviewService classRoomPaperReviewService;
	
	@Autowired
	private StudentPaperReviewDetailService studentPaperReviewDetailService;
	
	@Autowired
	private StudentPaperReviewService studentPaperReviewService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserPurchaseService userPurchaseService;
	
	@RequestMapping("/studentReports")
	public ModelAndView studentReports(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/student-details");
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		List<StudentDto> students = new ArrayList();
		List<Student> listStudents = new ArrayList();
		
		if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			listStudents = studentService.getStudentsByLecturer(lecturerService.getLecturerByUserId(user.getUserId()));
		}else {
			listStudents = studentService.listAll();
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			listStudents = studentService.listAll();
		}
		
		for(Student s:listStudents) {
			
			int allQues = studentPaperReviewDetailService.getCountByStudent(s.getStudentId());
			int attndQues = studentPaperReviewDetailService.getAttendedCountByStudent(s.getStudentId());
			int correctQues = studentPaperReviewDetailService.getCorrectCountByStudent(s.getStudentId());
			int atmpPapers = studentPaperReviewService.getCountByStudent(s.getStudentId());
			
			StudentDto sd = new StudentDto();
			
			sd.setStudentId(Long.toString(s.getStudentId()));
			sd.setUserId(Long.toString(s.getUserId().getUserId()));
			sd.setGrade(s.getGrade().getGradeName());
			sd.setGpa(Double.toString(s.getGpa()));
			if(allQues != 0)
				sd.setAverageMark(Double.toString((correctQues/allQues)*100));
			if(allQues == 0)
				sd.setAverageMark("-");
			sd.setAttendQues(attndQues);
			sd.setActive(Boolean.toString(s.getUserId().isActive()));
			sd.setName(s.getUserId().getName());
			sd.setGender(s.getUserId().getGender());
			sd.setEmail(s.getUserId().getEmail());
			sd.setMobile(s.getUserId().getMobile());
			sd.setTotalAttempts(Integer.toString(atmpPapers));
			
			students.add(sd);
		}
		
		mav.addObject("students", students);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
	    
		return mav;
	}
	
	@PostMapping("/secure/searchStudentList")
	public ResponseEntity<List<StudentDto>> searchStudent(@ModelAttribute StudentDto studentobj,HttpSession session) {
			
		String query;
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		List<Student> students = new ArrayList();
		List<StudentDto> returnList = new ArrayList();
				
		if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			query = "SELECT DISTINCT student.student_id, student.attempts_allowed, student.gpa, student.grade, student.user_id FROM student"
					+ " JOIN classroom_student ON classroom_student.student_id = student.student_id"
					+ " JOIN classroom ON classroom.classroom_id = classroom_student.class_room_id"
					+ " JOIN user ON user.user_id=student.user_id"
					+ " WHERE classroom.lecturer_id = " + lecturerService.getLecturerByUserId(user.getUserId()).getLecturerId();
		}else {
			query = "SELECT * FROM student JOIN user ON user.user_id=student.user_id where student.student_id != ''";
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			query = "SELECT * FROM student JOIN user ON user.user_id=student.user_id where student.student_id != ''";
		}
		
		if(!studentobj.getStudentId().equals("0")) {
			query += " AND student.student_id = " + studentobj.getStudentId();
		}
		if(!studentobj.getName().equals("0")) {
			query += " AND LOWER(user.name) LIKE '%" + studentobj.getName().toLowerCase() + "%'";		
		}
		if(!studentobj.getGender().equals("0")) {
			query += " AND user.gender = '" + studentobj.getGender() + "'";	
		}
		if(!studentobj.getEmail().equals("0")) {
			query += " AND LOWER(user.email) LIKE '%" + studentobj.getEmail().toLowerCase() + "%'";	
		}
		if(!studentobj.getMobile().equals("0")) {
			query += " AND user.mobile LIKE '%" + studentobj.getMobile() + "%'";	
		}
		if(!studentobj.getGrade().equals("0")) {
			query += " AND student.grade = " + studentobj.getGrade();	
		}
		
		List<Map<String, Object>> resultList = commonService.executeNativeQuery(query);
				
		for(Map<String, Object> s : resultList) {
			
			Student student = studentService.get((long)s.get("student_id"));
						
			int allQues = studentPaperReviewDetailService.getCountByStudent(student.getStudentId());
			int attndQues = studentPaperReviewDetailService.getAttendedCountByStudent(student.getStudentId());
			int correctQues = studentPaperReviewDetailService.getCorrectCountByStudent(student.getStudentId());
			int atmpPapers = studentPaperReviewService.getCountByStudent(student.getStudentId());
									
			StudentDto sd = new StudentDto();
			
			sd.setStudentId(Long.toString(student.getStudentId()));
			sd.setUserId(Long.toString(student.getStudentId()));
			sd.setGrade(student.getGrade().getGradeName());
			sd.setGpa(Double.toString(student.getGpa()));
			if(allQues != 0)
				sd.setAverageMark(Double.toString((correctQues/allQues)*100));
			if(allQues == 0)
				sd.setAverageMark("-");
			sd.setAttendQues(attndQues);
			if(student.getUserId().isActive())
				sd.setActive("Active");
			if(!student.getUserId().isActive())
				sd.setActive("Deactive");
			sd.setName(student.getUserId().getName());
			sd.setGender(student.getUserId().getGender());
			sd.setEmail(student.getUserId().getEmail());
			sd.setMobile(student.getUserId().getMobile());
			sd.setTotalAttempts(Integer.toString(studentService.countCompletedAttempts(student.getStudentId())));
			
			if(!studentobj.getTotalAttempts().equals("0")) {
				if(studentobj.getAttendQues() != 0) {
					if((atmpPapers >= Integer.parseInt(studentobj.getTotalAttempts())) && (atmpPapers <= studentobj.getAttendQues())) {
						returnList.add(sd);	
					}
				}else {
					if((atmpPapers >= Integer.parseInt(studentobj.getTotalAttempts()))) {
						returnList.add(sd);	
					}
				}
			}else {
				if(studentobj.getAttendQues() != 0) {
					if((atmpPapers <= studentobj.getAttendQues())) {
						returnList.add(sd);	
					}
				}else {
					returnList.add(sd);
				}
			}
		}
		
		if((returnList.size() == 1)) {
			StudentDto stu = returnList.get(0);
			Student student = studentService.get(Long.parseLong(stu.getStudentId()));

			List<StudentPaperReviewDto> studentPaperReviewDtoList = new ArrayList();
			for(StudentPaperReview spr : studentPaperReviewService.listByStudent(Long.parseLong(stu.getStudentId()))) {
				StudentPaperReviewDto spd = new StudentPaperReviewDto();
				spd.setPaper(spr.getPaperID().getName());
				spd.setDate(spr.getCreateDate());
				spd.setTime(LocalTime.now());
				spd.setTotal(spr.getResult());
				spd.setQuestionCount(studentPaperReviewDetailService.getQuestionCountByReview(spr.getSprId()));
				spd.setAnsweredCount(studentPaperReviewDetailService.getAttendedCountByReview(spr.getSprId()));
				spd.setCorrectCount(studentPaperReviewDetailService.getCorrectCountByReview(spr.getSprId()));	
				
				studentPaperReviewDtoList.add(spd);
			}
			returnList.get(0).setPapers(studentPaperReviewDtoList);
			
			List<SubjectWiseModelResultsDto> moduleResults = new ArrayList<SubjectWiseModelResultsDto>();
			for(Subject sub:subjectService.listByStudentPaperReview(student.getStudentId())) {
				List<ModuleResultDto> listModuleResult = new ArrayList<ModuleResultDto>();
				for(Module m:moduleService.listAllBySubject(sub.getSubjectId())) {
					int allQueCount = 0;
					int answeredQueCount = 0;
					int correctQueCount = 0;
					for(StudentPaperReviewDetail spd : studentPaperReviewDetailService.listAllByStudentAndModule(student.getStudentId(),m.getModuleId())) {
						allQueCount++;
						if(spd.getGivenAnswer() != null) {
							answeredQueCount++;
						}
						if(spd.isStatus()) {
							correctQueCount++;
						}
					}
					final DecimalFormat df = new DecimalFormat("0.00");
					 
					ModuleResultDto moduleDto = new ModuleResultDto();
					moduleDto.setModule(m);
					moduleDto.setAllQuestionCount(allQueCount);
					moduleDto.setAttendQuestionCount(answeredQueCount);
					moduleDto.setCorrectQuestionCount(correctQueCount);
					moduleDto.setResult(df.format(((double)correctQueCount/(double)allQueCount) * 100.00));
					listModuleResult.add(moduleDto);
				}
				SubjectWiseModelResultsDto subDto = new SubjectWiseModelResultsDto();
				subDto.setSubjectId(sub);
				subDto.setPaperCount(studentPaperReviewService.getCompleteCountBySubjectAndStudent(sub.getSubjectId(),student.getStudentId()));
				subDto.setModuleResultList(listModuleResult);
				moduleResults.add(subDto);
			}
			returnList.get(0).setModuleResults(moduleResults);
		}
		
		return new ResponseEntity<>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping("/userReports")
	public ModelAndView userReports(HttpSession session) {

		ModelAndView mav = new ModelAndView("reports/user-details");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		List<User> allUsers = new ArrayList();
		List<UserDto> users = new ArrayList();
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			List<PublisherUser> listUsers = publisherUserService.listAuthorsByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId());
			for(PublisherUser pu : listUsers) {
				allUsers.add(pu.getUserId());
			}
			listUsers = publisherUserService.listEditorsByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId());
			for(PublisherUser pu : listUsers) {
				allUsers.add(pu.getUserId());
			}
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			allUsers = userService.findAll();
		}
				 
		for(User a:allUsers) {
			
			User u = userService.getById(a.getUserId());
			
			UserDto ud = new UserDto();
			
			ud.setUserId(u.getUserId());
			ud.setName(u.getName());
			ud.setGender(u.getGender());
			ud.setEmail(u.getEmail());
			ud.setMobile(u.getMobile());
			ud.setRole(roleService.listRoleNamesByUser(u.getUserId()));
			ud.setActive(Boolean.toString(u.isActive()));
			
			users.add(ud);
		}
		
		mav.addObject("users", users);
		
		//Get all user types
		List<Role> listRoles = roleService.listAll();
	    mav.addObject("listRoles", listRoles);
	    
		return mav;
	}
	
	@PostMapping("/secure/searchUserList")
	public ResponseEntity<List<UserDto>> searchUserList(@ModelAttribute UserDto userobj,HttpSession session) {
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		List<UserDto> returnList = new ArrayList();
				
		String query = "";
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			query = "SELECT * FROM user JOIN publisher_user ON user.user_id = publisher_user.user_id ";
			if(!userobj.getRoleId().equals("0")) {
				query += " JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE role.role_id = " + userobj.getRoleId() + " AND publisher_user.po_id = " + publisherUserService.getByUserId(user.getUserId()).getPoId().getPoId();	
			}else {
				query += " WHERE user.user_id != '' AND publisher_user.po_id = " + publisherUserService.getByUserId(user.getUserId()).getPoId().getPoId();
			}
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			query = "SELECT * FROM user";
			if(!userobj.getRoleId().equals("0")) {
				query += " JOIN user_role ON user.user_id = user_role.user_id JOIN role ON user_role.role_id = role.role_id WHERE role.role_id = " + userobj.getRoleId();	
			}else {
				query += " WHERE user.user_id != ''";
			}
		}
		
		if(userobj.getUserId() != 0) {
			query += " AND user.user_id = " + userobj.getUserId();
		}
		if(!userobj.getName().equals("0")) {
			query += " AND LOWER(user.name) LIKE '%" + userobj.getName().toLowerCase() + "%'";		
		}
		if(!userobj.getGender().equals("0")) {
			query += " AND user.gender = '" + userobj.getGender() + "'";	
		}
		if(!userobj.getEmail().equals("0")) {
			query += " AND LOWER(user.email) LIKE '%" + userobj.getEmail().toLowerCase() + "%'";	
		}
		if(!userobj.getMobile().equals("0")) {
			query += " AND user.mobile LIKE '%" + userobj.getMobile() + "%'";	
		}
		
		System.out.println("query : " + query);
		
		List<Map<String, Object>> resultList = commonService.executeNativeQuery(query);
				
		for(Map<String, Object> s : resultList) {
			
			User u = userService.getById((long)s.get("user_id"));
															
			UserDto ud = new UserDto();
			
			ud.setUserId(u.getUserId());
			if(u.isActive())
				ud.setActive("Active");
			if(!u.isActive())
				ud.setActive("Deactive");
			ud.setName(u.getName());
			ud.setGender(u.getGender());
			ud.setEmail(u.getEmail());
			ud.setMobile(u.getMobile());
			ud.setRole(roleService.listRoleNamesByUser(u.getUserId()));
			returnList.add(ud);
		}
		
		return new ResponseEntity<>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping("/userRights")
	public ModelAndView userRights(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/user-rights-details");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<User> listUsers = userService.findAllByRole("STUDENT");
		List<StudentDto> students = new ArrayList();
				
		for(User u:listUsers) {
			Student s = studentService.getStudentByUserId(u.getUserId());
			
			StudentDto sd = new StudentDto();
			
			sd.setStudentId(Long.toString(s.getStudentId()));
			sd.setUserId(Long.toString(u.getUserId()));
			sd.setGrade(s.getGrade().getGradeName());
			sd.setGpa(Double.toString(s.getGpa()));
			sd.setActive(Boolean.toString(u.isActive()));
			//sd.setCreateDate(s.getCreateDate());
			sd.setName(u.getName());
			sd.setGender(u.getGender());
			sd.setEmail(u.getEmail());
			sd.setMobile(u.getMobile());
			
			students.add(sd);
		}
		
		mav.addObject("students", students);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
	    
		return mav;
	}
	
	@RequestMapping("/publisherReports")
	public ModelAndView publisherReports(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/publisher-details");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<Publisher> listPublishers = publisherService.listAllNonIndividual();
		List<PublisherDto> publishers = new ArrayList();
		
		for(Publisher p : listPublishers) {
			PublisherDto po = new PublisherDto();
			
			po.setPoId(p.getPoId());
			po.setName(p.getName());
			po.setDate(p.getCreateDate().toString());
			po.setEmail(p.getEmail());
			po.setMobile(p.getMobile());
			po.setAddress(p.getAddress());
			po.setAuthors(publisherUserService.getCountByRoleAndPublisher("AUTHOR",p.getPoId()));
			po.setEditors(publisherUserService.getCountByRoleAndPublisher("EDITOR",p.getPoId()));
			po.setPaperCount(publisherService.getPaperCount(p.getPoId()));
			if(p.isActive())
				po.setActive("Active");
			if(!p.isActive())
				po.setActive("Deactive");
				
			publishers.add(po);
		}
		
		mav.addObject("publishers", publishers);
		
		return mav;
	}
	
	@PostMapping("/secure/searchPublisherList")
	public ResponseEntity<List<PublisherDto>> searchPublisherList(@ModelAttribute PublisherDto publisherobj,HttpSession session) {

		List<PublisherDto> returnList = new ArrayList();
				
		String query = "SELECT * FROM publisher WHERE publisher.po_id != 0";
		
		if(publisherobj.getPoId() != 0) {
			query += " AND publisher.po_id = " + publisherobj.getPoId();
		}
		if(!publisherobj.getName().equals("0")) {
			query += " AND LOWER(publisher.name) LIKE '%" + publisherobj.getName().toLowerCase() + "%'";		
		}
		if(!publisherobj.getEmail().equals("0")) {
			query += " AND LOWER(publisher.email) LIKE '%" + publisherobj.getEmail().toLowerCase() + "%'";	
		}
		if(!publisherobj.getMobile().equals("0")) {
			query += " AND publisher.mobile LIKE '%" + publisherobj.getMobile() + "%'";	
		}
		if(!publisherobj.getAddress().equals("0")) {
			query += " AND LOWER(publisher.address) LIKE '%" + publisherobj.getAddress().toLowerCase() + "%'";	
		}
		
		List<Map<String, Object>> resultList = commonService.executeNativeQuery(query);
				
		for(Map<String, Object> s : resultList) {
			
			Publisher p = publisherService.get((long)s.get("po_id"));
															
			PublisherDto pd = new PublisherDto();
			
			pd.setPoId(p.getPoId());
			if(p.isActive())
				pd.setActive("Active");
			if(!p.isActive())
				pd.setActive("Deactive");
			pd.setName(p.getName());
			pd.setDate(p.getCreateDate().toString());
			pd.setEmail(p.getEmail());
			pd.setMobile(p.getMobile());
			pd.setAddress(p.getAddress());
			pd.setAuthors(publisherUserService.getCountByRoleAndPublisher("AUTHOR",p.getPoId()));
			pd.setEditors(publisherUserService.getCountByRoleAndPublisher("EDITOR",p.getPoId()));
			pd.setPaperCount(publisherService.getPaperCount(p.getPoId()));
			returnList.add(pd);
		}
		
		return new ResponseEntity<>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping("/paperReports")
	public ModelAndView paperReports(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/paper-details");
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<Paper> listPapers = new ArrayList();
		List<PaperReportDto> papers = new ArrayList();
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			listPapers = paperService.listAllByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId());
		}else if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			listPapers = paperService.listAllByUsername(user.getUsername());		
		}else {
			listPapers = paperService.listAll();
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			listPapers = paperService.listAll();
		}
			
		for(Paper p : listPapers) {
			PaperReportDto prd = new PaperReportDto();
			
			prd.setPaperId(p.getPaperId());
			prd.setName(p.getName());
			prd.setNumberOfQuestion(p.getNumberOfQuestion());
			prd.setTime(p.getTime());
			prd.setGrade(p.getGrade().getGradeName());
			prd.setSubject(p.getSubjectId().getSubjectName());
			prd.setPublisher(p.getPublisher().getName());
			prd.setAttemptCount(paperService.getTotalAttempts(p.getPaperId()));
			if(p.isActive())
				prd.setActive("Active");
			if(!p.isActive())
				prd.setActive("Deactive");
			
			papers.add(prd);
		}
		mav.addObject("papers", papers);
		
		//Get all grades
		List<Publisher> listPublishers = publisherService.listAllNonIndividual();
	    mav.addObject("publishers", listPublishers);
	    
	    //Get all grades
  		List<Grade> listGrades = gradeService.listAllActive();
  	    mav.addObject("grades", listGrades);
  	    
	  	//Get all grades
		List<Subject> listSubjects = subjectService.listAll();
	    mav.addObject("subjects", listSubjects);
			    
		return mav;
	}
	
	@PostMapping("/secure/searchPaperList")
	public ResponseEntity<List<PaperReportDto>> searchPaperList(@ModelAttribute PaperReportDto paperobj,HttpSession session) {

		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		List<PaperReportDto> returnList = new ArrayList();
				
		String query = "";
		
		query = "SELECT * FROM paper WHERE paper.paper_id != 0" + user.getUsername();
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			query = "SELECT * FROM paper WHERE paper.publisher = " + publisherUserService.getByUserId(user.getUserId()).getPoId().getPoId();
		}else if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			query = "SELECT * FROM paper WHERE paper.create_by = " + user.getUsername();
		}else {
			query = "SELECT * FROM paper WHERE paper.paper_id != 0";
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			query = "SELECT * FROM paper WHERE paper.paper_id != 0";
		}
		
		if(paperobj.getPaperId() != 0) {
			query += " AND paper.paper_id = " + paperobj.getPaperId();
		}
		if(!paperobj.getName().equals("0")) {
			query += " AND LOWER(paper.name) LIKE '%" + paperobj.getName().toLowerCase() + "%'";		
		}
		if(!paperobj.getGrade().equals("0")) {
			query += " AND paper.grade = " + paperobj.getGrade();	
		}
		if(!paperobj.getSubject().equals("0")) {
			query += " AND paper.subject_id = " + paperobj.getSubject();	
		}
		if(!paperobj.getPublisher().equals("0")) {
			query += " AND paper.publisher = " + paperobj.getPublisher();	
		}
		
		List<Map<String, Object>> resultList = commonService.executeNativeQuery(query);
				
		for(Map<String, Object> s : resultList) {
			
			Paper p = paperService.get((long)s.get("paper_id"));
															
			PaperReportDto pd = new PaperReportDto();
			
			pd.setPaperId(p.getPaperId());
			if(p.isActive())
				pd.setActive("Active");
			if(!p.isActive())
				pd.setActive("Deactive");
			pd.setName(p.getName());
			pd.setGrade(p.getGrade().getGradeName());
			pd.setSubject(p.getSubjectId().getSubjectName());
			pd.setPublisher(p.getPublisher().getName());
			pd.setNumberOfQuestion(p.getNumberOfQuestion());
			pd.setAttemptCount(paperService.getTotalAttempts(p.getPaperId()));
			pd.setTime(p.getTime());
						
			returnList.add(pd);
		}
		
		return new ResponseEntity<>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping("/userPaperSubmitReport")
	public ModelAndView userPaperSubmitReport(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/user-paper-details");
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<Paper> listPapers = new ArrayList();
		List<UserPaperDto> papers = new ArrayList();
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			listPapers = paperService.listAllByPublisherGroupByCreateUser(publisherUserService.getByUserId(user.getUserId()).getPoId());
		}else if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			listPapers = paperService.listAllByUsername(user.getUsername());		
		}else {
			listPapers = paperService.listAllGroupByCreateUser();
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			listPapers = paperService.listAllGroupByCreateUser();
		}
			
		for(Paper p : listPapers) {
			UserPaperDto prd = new UserPaperDto();
			
			prd.setUserId(userService.getByUserName(p.getCreateBy()).getUserId());
			prd.setUserName(userService.getByUserName(p.getCreateBy()).getName());
			prd.setTotalCreateCount(paperService.getPaperCountByUser(p.getCreateBy()));
			prd.setOnActiveCount(paperService.getActivePaperCountByUser(p.getCreateBy()));
			prd.setOnDeactiveCount(paperService.getDeactivePaperCountByUser(p.getCreateBy()));
			prd.setAttendedCount(studentPaperReviewService.getTotalAttendCountByUser(p.getCreateBy()));
						
			papers.add(prd);
		}
		mav.addObject("userPapers", papers);
		
		//Get all grades
		List<Publisher> listPublishers = publisherService.listAllNonIndividual();
	    mav.addObject("publishers", listPublishers);
	    
	    //Get all grades
  		List<Grade> listGrades = gradeService.listAllActive();
  	    mav.addObject("grades", listGrades);
  	    
	  	//Get all grades
		List<Subject> listSubjects = subjectService.listAll();
	    mav.addObject("subjects", listSubjects);
			    
		return mav;
	}
	
	@PostMapping("/secure/searchUserPaperList")
	public ResponseEntity<List<UserPaperDto>> searchUserPaperList(@ModelAttribute UserPaperDto upDto,HttpSession session) {

		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		List<UserPaperDto> returnList = new ArrayList();
			
		UserPaperDto prd = new UserPaperDto();
		prd.setUserId(upDto.getUserId());
		prd.setUserName(userService.getById(upDto.getUserId()).getName());
		prd.setTotalCreateCount(paperService.getPaperCountByUser(userService.getById(upDto.getUserId()).getUsername()));
		prd.setOnActiveCount(paperService.getActivePaperCountByUser(userService.getById(upDto.getUserId()).getUsername()));
		prd.setOnDeactiveCount(paperService.getDeactivePaperCountByUser(userService.getById(upDto.getUserId()).getUsername()));
		prd.setAttendedCount(studentPaperReviewService.getTotalAttendCountByUser(userService.getById(upDto.getUserId()).getUsername()));
		returnList.add(prd);
		
		String query = "";
		
		if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
			query = "SELECT * FROM paper WHERE paper.publisher = " + publisherUserService.getByUserId(user.getUserId()).getPoId().getPoId();
		}else {
			query = "SELECT * FROM paper WHERE paper.paper_id != ''";
		}
		
		if ((loggedRoles.contains(roleService.getByRole("ADMIN"))) || (loggedRoles.contains(roleService.getByRole("SUPERADMIN")))) {
			query = "SELECT * FROM paper WHERE paper.paper_id != ''";
		}
		
		
		query += " AND paper.create_by = '" + userService.getById(upDto.getUserId()).getUsername() + "'";
		
		List<Map<String, Object>> resultList = commonService.executeNativeQuery(query);
				
		for(Map<String, Object> s : resultList) {

			Paper p = paperService.get((long)s.get("paper_id"));
															
			UserPaperDto pd = new UserPaperDto();
			pd.setPaperId(p.getPaperId());
			pd.setPaperName(p.getName());
			pd.setGrade(p.getGrade().getGradeName());
			pd.setSubject(p.getSubjectId().getSubjectName());
			pd.setPublisher(p.getPublisher().getName());
			if(p.isActive())
				pd.setActive("Active");
			if(!p.isActive())
				pd.setActive("Deactive");
			pd.setAttendedCount(studentPaperReviewService.getTotalAttendCount(p.getPaperId()));
			pd.setAverageScore(studentPaperReviewService.getAverageScoreByPaperId(p.getPaperId()));
			returnList.add(pd);
		}
		return new ResponseEntity<>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping("/purchaseHistory")
	public ModelAndView purchaseHistory(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/purchase-details");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<UserPurchase> listPurchases = userPurchaseService.listAll();
		List<PurchaseDto> purchases = new ArrayList();
		
		for(UserPurchase p : listPurchases) {
			PurchaseDto po = new PurchaseDto();
			
			po.setUserId(p.getUserId().getUserId());
			po.setUserName(p.getUserId().getName());
			if(p.getPaperId() != null) {
				po.setType("paper");
				po.setId(p.getPaperId().getPaperId());
				po.setPaperName(p.getPaperId().getName());				
			}else if(p.getBundleId() != null) {
				po.setType("bundle");
				po.setId(p.getBundleId().getBundleId());
				po.setPaperName(p.getBundleId().getName());				
			}
			po.setPurchaseDate(p.getStartDate().toString());
			po.setExpireDate(p.getEndDate().toString());
									
			if(p.isActive())
				po.setStatus("Active");;
			if(!p.isActive())
				po.setStatus("Deactive");
			if(p.isExpired())	
				po.setStatus("Expired");
			
			purchases.add(po);
			
		}
		
		mav.addObject("purchases", purchases);
		
		return mav;
	}
	
	@PostMapping("/secure/searchPurchaseList")
	public ResponseEntity<List<PurchaseDto>> searchPurchaseList(@ModelAttribute PurchaseDto purchaseObj,HttpSession session) {

		List<PurchaseDto> returnList = new ArrayList();
				
		String query = "SELECT * FROM user_purchase WHERE user_purchase.user_paper_id != 0";
		
		if(purchaseObj.getUserId() != 0) {
			query += " AND user_purchase.user_id = " + purchaseObj.getUserId();
		}
		if(purchaseObj.getId() != 0) {
			query += " AND (user_purchase.paper_id = " + purchaseObj.getId() + " OR user_purchase.bundle_id = " + purchaseObj.getId() + ")";
		}
		if(!purchaseObj.getPurchaseDate().equals("0")) {
			query += " AND user_purchase.start_date >= '" + purchaseObj.getPurchaseDate() + "'";		
		}
		if(!purchaseObj.getExpireDate().equals("0")) {
			query += " AND user_purchase.end_date <= '" + purchaseObj.getExpireDate() + "'";	
		}
				
		List<Map<String, Object>> resultList = commonService.executeNativeQuery(query);
				
		for(Map<String, Object> s : resultList) {
			
			UserPurchase p = userPurchaseService.get((long)s.get("user_paper_id"));
															
			PurchaseDto po = new PurchaseDto();
			
			po.setUserId(p.getUserId().getUserId());
			po.setUserName(p.getUserId().getName());
			if(p.getPaperId() != null) {
				po.setType("paper");
				po.setId(p.getPaperId().getPaperId());
				po.setPaperName(p.getPaperId().getName());				
			}else if(p.getBundleId() != null) {
				po.setType("bundle");
				po.setId(p.getBundleId().getBundleId());
				po.setPaperName(p.getBundleId().getName());				
			}
			po.setPurchaseDate(p.getStartDate().toString());
			po.setExpireDate(p.getEndDate().toString());
									
			if(p.isActive())
				po.setStatus("Active");;
			if(!p.isActive())
				po.setStatus("Deactive");
			if(p.isExpired())	
				po.setStatus("Expired");
						
			returnList.add(po);
		}
		
		return new ResponseEntity<>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping("/classroom/studentReport")
	public ModelAndView classroomStudentReports(@RequestParam long classroomId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/classroom-students-details");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<ClassRoomStudent> listClassroomStudents = classRoomStudentService.listAllByClassRoom(classroomId);
			
		List<ClassRoomStudentDto> studentList = new ArrayList();
		
		for(ClassRoomStudent clSTD:listClassroomStudents) {
			ClassRoomStudentDto std = new ClassRoomStudentDto();
			
			std.setUserId(clSTD.getStudentId().getUserId().getUserId());
			std.setEmail(clSTD.getStudentId().getUserId().getEmail());
			std.setStatus(clSTD.isStatus());
			std.setTotalAttendPapers(classRoomPaperReviewService.getTotalAttempsByStudent(clSTD.getStudentId().getUserId().getEmail()));
			//std.setTotalMarks(classRoomPaperReviewService.getTotalMarks(clSTD.getStudentEmail()));
			std.setTotalAttendTime(classRoomSessionsService.getTotalAttendTime(clSTD.getStudentId().getUserId().getEmail()));	
			
			studentList.add(std);
		}
		
		ClassRoom classroom = classRoomService.get(classroomId);
  		mav.addObject("classroom", classroom);
		
  		mav.addObject("studentList", studentList);
		
		return mav;
	}
	
	@RequestMapping("/classroom/paperReport")
	public ModelAndView classroomPaperReports(@RequestParam long classroomId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/classroom-paper-details");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<ClassRoomStudent> listClassroomStudents = classRoomStudentService.listAllByClassRoom(classroomId);
		
		List<ClassRoomPaper> papers = classRoomPaperService.listAllByClassRoom(classroomId);
		
		List<ClassRoomPaperDto> paperList = new ArrayList();
		
		for(ClassRoomPaper p : papers) {			
			ClassRoomPaperDto newPaper = new ClassRoomPaperDto();
			newPaper.setPaperName(p.getPaperID().getName());
			newPaper.setAttendCount(classRoomPaperReviewService.getTotalAttemptsByClassRoom(p.getPaperID().getPaperId() , p.getClassRoomId().getClassroomId()));
			newPaper.setActive(p.isActive());
			newPaper.setExpireDate(lecturerPaperService.getByPaperAndUser(p.getPaperID().getPaperId(),p.getClassRoomId().getLecturerId().getLecturerId()).getEndDate());
			
			paperList.add(newPaper);
		}
		mav.addObject("papers", paperList);
		
		ClassRoom classroom = classRoomService.get(classroomId);
  		mav.addObject("classroom", classroom);
		
		return mav;
	}
	
	@RequestMapping("classroom/activeSessionReport")
	public ModelAndView activeSessionReport(@RequestParam long classroomId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/classroom-session-details");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<ClassRoomSessions> listClassroomSessions = classRoomSessionsService.listAllByClassRoom(classroomId);
		mav.addObject("listClassroomSessions", listClassroomSessions);
		
		ClassRoom classroom = classRoomService.get(classroomId);
  		mav.addObject("classroom", classroom);
		
		return mav;
	}
	
	@RequestMapping("classroom/monthlySessionReport")
	public ModelAndView monthlySessionReport(@RequestParam long classroomId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("reports/classroom-monthly-sessions");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		List<ClassRoomSessions> listClassroomSessions = classRoomSessionsService.listAllByClassRoom(classroomId);
				
		List<ClassRoomSessions> monthlySessions = new ArrayList();
		
		for(ClassRoomSessions crSession : listClassroomSessions) {
			if(crSession.getDate().getMonth() == (LocalDate.now().getMonth())) {
				monthlySessions.add(crSession);				
			}
		}
		mav.addObject("listClassroomSessions", monthlySessions);
		
		ClassRoom classroom = classRoomService.get(classroomId);
  		mav.addObject("classroom", classroom);
		
		return mav;
	}
	
}
