package com.rcs2.mcqsys.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.dto.MarkedQuestionsdto;
import com.rcs2.mcqsys.dto.ModuleResultDto;
import com.rcs2.mcqsys.dto.ModuleWiseResults;
import com.rcs2.mcqsys.dto.PaperViewDto;
import com.rcs2.mcqsys.dto.QuestionWithCorrectAnswerDto;
import com.rcs2.mcqsys.dto.StudentPaperReviewDetailDto;
import com.rcs2.mcqsys.dto.SubjectWiseModelResultsDto;
import com.rcs2.mcqsys.dto.answerDto;
import com.rcs2.mcqsys.model.ActiveSessions;
import com.rcs2.mcqsys.model.ClassRoom;
import com.rcs2.mcqsys.model.ClassRoomPaperReview;
import com.rcs2.mcqsys.model.ClassRoomPaperReviewDetail;
import com.rcs2.mcqsys.model.ClassRoomStudent;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.LinkPaperBundle;
import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperAnswer;
import com.rcs2.mcqsys.model.PaperBundle;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.model.StudentPaperReviewDetail;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.ActiveSessionsService;
import com.rcs2.mcqsys.service.ClassRoomPaperReviewDetailService;
import com.rcs2.mcqsys.service.ClassRoomPaperReviewService;
import com.rcs2.mcqsys.service.ClassRoomStudentService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.LinkPaperBundleService;
import com.rcs2.mcqsys.service.ModuleService;
import com.rcs2.mcqsys.service.PaperAnswerService;
import com.rcs2.mcqsys.service.PaperBundleService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.StudentPaperReviewService;
import com.rcs2.mcqsys.service.StudentService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.StudentPaperReviewDetailService;

@Controller
public class StudentController {	
	//Paper Management
		
	@Autowired
	private StudentService studentService;
	
	@Autowired
    private PaperService paperService;
	
	@Autowired
    private GradeService gradeService;
	
	@Autowired
    private SubjectService subjectService;
	
	@Autowired
    private ModuleService moduleService;
	
	@Autowired
    private PublisherService publisherService;
	
	@Autowired
	private StudentPaperReviewService studentPaperReviewService;
	
	@Autowired
	private StudentPaperReviewDetailService studentPaperReviewDetailService;
	
	@Autowired
	private ClassRoomPaperReviewService classRoomPaperReviewService;
	
	@Autowired
	private ClassRoomPaperReviewDetailService classRoomPaperReviewDetailService;
	
	@Autowired
	private PaperQuestionService paperQuestionService;
	
	@Autowired
	private PaperAnswerService paperAnswerService;
	
	@Autowired
	private ActiveSessionsService activeSessionsService;
	
	@Autowired
	private ClassRoomStudentService classRoomStudentService;
	
	@Autowired
	private PaperBundleService paperBundleService;
	
	@Autowired
	private LinkPaperBundleService linkPaperBundleService;
		
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	//Student Management
	
	@RequestMapping("/student/listPapers")
	public String studentListPapers(Model model,HttpSession session) {
	    List<Paper> listPapers = paperService.listAllActive();
	    
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
	    model.addAttribute("listPapers", listPapers);
	     
	    return "student/paper-list";
	}
	
	@RequestMapping("/listStudentClassroom")
	public String listStudentClassroom(Model model,HttpSession session) {
		
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<ClassRoomStudent> listClassrooms = classRoomStudentService.listByUser(studentService.getStudentByUserId(user.getUserId()).getStudentId());
	    model.addAttribute("listClassrooms", listClassrooms);
	    
	    return "student/classroom-list";
	}
	
	@RequestMapping("/student/listPapersByPublisher")
	public ModelAndView studentListPapersByPublisher(@RequestParam Long subjectId,Long poId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("student/paper-list");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		Student student = studentService.getStudentByUserId(user.getUserId());
		
		int availableAttempts = student.getAttemptsAllowed() - studentService.countCompletedAttempts(student.getStudentId());
		if(availableAttempts < 0) availableAttempts = 0;
		
	    List<Paper> listPapers = paperService.studentListPapersByPublisher(student.getGrade(),subjectId,poId);
	   
	    mav.addObject("listPapers", listPapers);
	    mav.addObject("subject", subjectId);
	    mav.addObject("publisher", publisherService.get(poId)); 
	    mav.addObject("availableAttempts", availableAttempts);
	    return mav;
	}
	
	@RequestMapping("/student/listPublishersByGrade")
	public ModelAndView studentListPublishersByGrade(@RequestParam Subject subjectId,HttpSession session) {
		ModelAndView mav = new ModelAndView("student/publisher-list");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		Student student = studentService.getStudentByUserId(user.getUserId());
		
		List<Paper> listPapers = paperService.studentListPublishersByGrade(student.getGrade(),subjectId);
	    
		ArrayList<Publisher> listPublishers = new ArrayList<Publisher>();
	    
	    for(Paper p:listPapers) {
	    	if(!(listPublishers.contains(p.getPublisher())) && (p.getPublisher().getPoId() != 0)) {
	    		listPublishers.add(p.getPublisher());
	    	}
	    }
	    	    
	    mav.addObject("listPapers", listPublishers);
	    mav.addObject("subject", subjectId);
	     
	    return mav;
	}
	
	@RequestMapping("student/listSubject")
	public String studentListSubjectByGrade(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		Student student = studentService.getStudentByUserId(user.getUserId());
		
	    List<Subject> listSubjects = subjectService.studentListSubjectsByGrade(student.getGrade());
	   
	    List<ClassRoomStudent> listClassrooms = classRoomStudentService.listByUser(user.getUserId());
	    model.addAttribute("listSubjects", listSubjects);
	    model.addAttribute("listClassrooms", listClassrooms);
	     
	    return "student/student-panel";
	}
	
	@GetMapping("student/viewPaper")
	public ModelAndView studentViewPaper(@RequestParam Long paperId,HttpSession session) {
		int questionCount = 0;
		
		ModelAndView mav = new ModelAndView("student/paper-attend");
		
		Paper paper = paperService.get(paperId);
		List<PaperQuestion> listQuestions = paperQuestionService.listAllByPaperId(paperId);		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paperId);		
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
			
		StudentPaperReview review = new StudentPaperReview();
		review.setCreateBy(user.getUsername());
		review.setCreateDate(LocalDate.now());
		review.setUpdateBy(user.getUsername());
		review.setUpdateDate(LocalDate.now());
		review.setPaperID(paper);
		review.setSeatedDate(LocalDate.now());
		review.setResult(0);	
		review.setActive(true);
		review.setStudentId(studentService.getStudentByUserId(user.getUserId()));
		studentPaperReviewService.save(review);
		
		ActiveSessions activeSession = new ActiveSessions();
		activeSession.setActive(true);
		activeSession.setCreateBy(user.getUsername());
		activeSession.setCreateDate(LocalDate.now());
		activeSession.setUpdateBy(user.getUsername());
		activeSession.setUpdateDate(LocalDate.now());
		activeSession.setStudentId(studentService.getStudentByUserId(user.getUserId()));
		activeSession.setStartTime(LocalTime.now());
		activeSession.setPaperID(paper);
		activeSession.setEndTime(LocalTime.now());
		
		activeSessionsService.save(activeSession);

		Subject subject = subjectService.get(paper.getSubjectId().getSubjectId());
		mav.addObject("subject", subject);
		
		Publisher publisher = publisherService.get(paper.getPublisher().getPoId());
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
			
		mav.addObject("activeSession", activeSession);
		mav.addObject("review", review);
		mav.addObject("paper", paper);
		mav.addObject("listQuestions", listPaperQuestions);
		mav.addObject("listAnswers", listPaperAnswers);
		mav.addObject("subject", paper.getSubjectId());
		mav.addObject("questionCount", questionCount);
		
		return mav;
	}
	
	@GetMapping("/reviewPaper")
	public ModelAndView reviewPaper(@RequestParam Long reviewId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("student/paper-review");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		StudentPaperReview review = studentPaperReviewService.get(reviewId);
		Paper paper = review.getPaperID();
		List<StudentPaperReviewDetail> listReviews = studentPaperReviewDetailService.listAllByReviewId(reviewId);		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paper.getPaperId());		
		
		List<ModuleWiseResults> listModuleResults = new ArrayList<ModuleWiseResults>();
		
		for(long m : paperQuestionService.listAllModulesByPaper(paper.getPaperId())) {
			int allQuestionCount = 0;
			int answeredQuestionCount = 0;
			int correctAnsweredCount = 0;
//			for(PaperQuestion pq : paperQuestionService.listAllByPaperAndModule(paper.getPaperId(),m)) {
//				
//			}
			
			for(StudentPaperReviewDetail reviewDetail : studentPaperReviewDetailService.listAllByReviewAndModule(reviewId,m)) {
				allQuestionCount++;
				if(reviewDetail.getGivenAnswer() != null)
					answeredQuestionCount++;
				if(reviewDetail.isStatus())
					correctAnsweredCount++;
			}
			
			for(StudentPaperReviewDetail s : listReviews) {
				System.out.println(s.getQuestionId().getQuestionId());
			}
			List<StudentPaperReviewDetail> listReviews1 = new ArrayList<StudentPaperReviewDetail>();
			//Collections.sort(listReviews1);
			
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
		mav.addObject("review", review);
	
		return mav;
	}
	
	@GetMapping("/reviewClassroomPaper")
	public ModelAndView reviewClassroomPaper(@RequestParam Long reviewId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("classroom/classroom-paper-review");
		
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
			
			for(ClassRoomPaperReviewDetail s : listReviews) {
				System.out.println(s.getQuestionId().getQuestionId());
			}
			List<ClassRoomPaperReviewDetail> listReviews1 = new ArrayList<ClassRoomPaperReviewDetail>();
			//Collections.sort(listReviews1);
			
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
		mav.addObject("review", review);
	
		return mav;
	}
	
	@GetMapping("student/viewBundle")
	public ModelAndView viewBundle(@RequestParam Long bundleId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("student/bundle-view");
		
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
		 
		
		return mav;
	}
	
	@GetMapping("/getQuestionList")
	public ResponseEntity<Object> getQuestionList(@RequestParam Long paperId) {
		
		List<PaperQuestion> listPaperQuestions = paperQuestionService.listAllByPaperId(paperId);
				
		return new ResponseEntity<>(listPaperQuestions,HttpStatus.OK) ;
	}
	
	@RequestMapping("/getAnswerList")
	public ResponseEntity<Object> getAnswerList(@RequestParam Long pqId) {
		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByQuestionId(pqId);
	    
	    return new ResponseEntity<>(listPaperAnswers,HttpStatus.OK) ;
	}
	
	@RequestMapping("/getCorrectAnswer")
	public ResponseEntity<Object> getCorrectAnswer(@RequestParam Long pqId) {
		
		PaperAnswer Answer = paperAnswerService.getCorrectAnswer(pqId);
	    
	    return new ResponseEntity<>(Answer,HttpStatus.OK) ;
	}
	
	@RequestMapping("/getPaperWithCorrectAnswers")
	public ResponseEntity<Object> getPaperWithCorrectAnswers(@RequestParam String paperId) {

		MarkedQuestionsdto markedPaper = new MarkedQuestionsdto();;
		List<QuestionWithCorrectAnswerDto> questionWithCorrectAnswer = new ArrayList();
		List<PaperQuestion> questions = paperQuestionService.listAllByPaperId(Long.parseLong(paperId));

		for(PaperQuestion pq : questions) {

			try {
				PaperAnswer correctAnswer = paperAnswerService.getCorrectAnswer(pq.getPqId());
				
				if(!correctAnswer.equals(null)) {
					
					QuestionWithCorrectAnswerDto q = new QuestionWithCorrectAnswerDto();
					
					q.setPqId(pq.getPqId());
					q.setModuleId(pq.getModuleId().getModuleId());
					q.setPaId(correctAnswer.getPaId());
					q.setAnswerList(paperAnswerService.listAllByQuestionId(pq.getPqId()));
					
					questionWithCorrectAnswer.add(q);				
				}
			}catch(Exception e) {
				
			}
		}
		
		//Collections.sort(getPaperWithCorrectAnswers);	
		markedPaper.setPaperID(paperId);
		markedPaper.setQuestionList(questionWithCorrectAnswer);

		return new ResponseEntity<>(markedPaper,HttpStatus.OK) ;
	}
	
	@PostMapping("/secure/saveReview")
	public ResponseEntity<String> saveReview(@ModelAttribute StudentPaperReview review) {
		
		review.setActive(false);
		review.setUpdateDate(LocalDate.now());
		studentPaperReviewService.save(review);
		
		ActiveSessions activeSession = activeSessionsService.getActiveSessionByStudent(review.getStudentId().getStudentId()); 
		activeSession.setEndTime(LocalTime.now());
		activeSession.setActive(false);
		activeSessionsService.save(activeSession);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@PostMapping("/secure/saveReviewDetail")
	public ResponseEntity<String> saveReviewDetail(@ModelAttribute StudentPaperReviewDetailDto reviewDetail) {
				
		StudentPaperReviewDetail detail = new StudentPaperReviewDetail();
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
		detail.setReviewId(studentPaperReviewService.get(reviewDetail.getReviewId()));
		studentPaperReviewDetailService.save(detail);
				
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/myResults")
	public ModelAndView myResults(HttpSession session) {
		ModelAndView mav = new ModelAndView("student/result-list");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		mav.addObject("student", studentService.getStudentByUserId(user.getUserId()));
		
		for(StudentPaperReview sr : studentPaperReviewService.listByStudent(studentService.getStudentByUserId(user.getUserId()).getStudentId())) {
			if(studentPaperReviewDetailService.getCountByReview(sr.getSprId()) <= 0) {
				studentPaperReviewService.delete(sr.getSprId());
			}
		}
		//Get all Reviews
		List<StudentPaperReview> listReviews = studentPaperReviewService.listByStudent(studentService.getStudentByUserId(user.getUserId()).getStudentId());
		mav.addObject("listReviews", listReviews);
		
		//Get all Reviews
		List<Subject> listSubjects = subjectService.listByStudentPaperReview(studentService.getStudentByUserId(user.getUserId()).getStudentId());
		mav.addObject("listSubjects", listSubjects);
		
		return mav;
	}
	
	@GetMapping("/viewStudentResult")
	public ModelAndView viewStudentResult(@RequestParam long studentId,HttpSession session) {
		ModelAndView mav = new ModelAndView("student/result-list");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		Student student = studentService.get(studentId);
		mav.addObject("student", student);
		
		for(StudentPaperReview sr : studentPaperReviewService.listByStudent(student.getStudentId())) {
			if(studentPaperReviewDetailService.getCountByReview(sr.getSprId()) <= 0) {
				try {
					studentPaperReviewService.delete(sr.getSprId());
				}catch(Exception ex) {}
			}
		}
		//Get all Reviews
		List<StudentPaperReview> listReviews = studentPaperReviewService.listByStudent(student.getStudentId());
		mav.addObject("listReviews", listReviews);
		
		//Get Subjects
		List<Subject> listSubjects = subjectService.listByStudentPaperReview(student.getStudentId());
		mav.addObject("listSubjects", listSubjects);
		
		return mav;
	}
	
	@GetMapping("/viewClassRoomStudentResult")
	public ModelAndView viewClassRoomStudentResult(@RequestParam long classroomStudentId,HttpSession session) {
		ModelAndView mav = new ModelAndView("classroom/classroom-result-list");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
				
		Student student = classRoomStudentService.get(classroomStudentId).getStudentId();
		mav.addObject("student", student);
				
		ClassRoom classroom = classRoomStudentService.get(classroomStudentId).getClassRoomId();
		mav.addObject("classroom", classroom);
				
		for(ClassRoomPaperReview cpr : classRoomPaperReviewService.listByStudentAndClassroom(classroom.getClassroomId(), student.getStudentId())) {
			if(classRoomPaperReviewDetailService.getCountByReview(cpr.getCprId()) <= 0) {
				try {
					classRoomPaperReviewService.delete(cpr.getCprId());
				}catch(Exception ex) {}
			}
		}
				
		//Get all Reviews
		List<ClassRoomPaperReview> listReviews = classRoomPaperReviewService.listByStudentAndClassroom(classroom.getClassroomId(), student.getStudentId());
		System.out.println("listReviews : " + listReviews);
		mav.addObject("listReviews", listReviews);
				
		//Get Subjects
		List<Subject> listSubjects = subjectService.listByClassroomPaperReview(classroom.getClassroomId(),student.getStudentId());
		mav.addObject("listSubjects", listSubjects);
				
		return mav;
	}
	
	@GetMapping("/getResultsBySubjectAndStudent")
	public ResponseEntity<List<StudentPaperReview>> getResultsBySubjectAndStudent(@RequestParam Long subjectId,@RequestParam Long studentId) {

		for(StudentPaperReview sr : studentPaperReviewService.listByStudent(studentId)) {
			if(studentPaperReviewDetailService.getCountByReview(sr.getSprId()) <= 0) {
				studentPaperReviewService.delete(sr.getSprId());
			}
		}
		
		//Get all Reviews
		List<StudentPaperReview> listReviews = studentPaperReviewService.getResultsBySubjectAndStudent(subjectId,studentId);
				
		return new ResponseEntity<>(listReviews, HttpStatus.OK);
	}
	
	@GetMapping("/updateGrade")
	public ResponseEntity<String> updateGrade(@RequestParam Long grade,@RequestParam Long userId) {
		Student student = studentService.getStudentByUserId(userId);
		student.setGrade(gradeService.get(grade));
		studentService.save(student);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/moduleWiseSummeryReport")
	public ModelAndView moduleWiseSummeryReport(@RequestParam Long subjectId,@RequestParam Long studentId,HttpSession session) {
		ModelAndView mav = new ModelAndView("student/result-summery");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		Student student = studentService.get(studentId);
		mav.addObject("student",student);
		
		mav.addObject("localDateTime", LocalDateTime.now());
		
		for(StudentPaperReview sr : studentPaperReviewService.listByStudent(student.getStudentId())) {
			if(studentPaperReviewDetailService.getCountByReview(sr.getSprId()) <= 0) {
				studentPaperReviewService.delete(sr.getSprId());
			}
		}
		//Get all Reviews
		List<StudentPaperReview> listReviews = studentPaperReviewService.listByStudent(student.getStudentId());
		mav.addObject("listReviews", listReviews);
		
		List<Subject> listSubjects = new ArrayList<Subject>();
		
		if(subjectId == 0) {
			listSubjects = subjectService.listByStudentPaperReview(student.getStudentId());
		}else {
			listSubjects.clear();
			listSubjects.add(subjectService.get(subjectId));
		}
		
		List<SubjectWiseModelResultsDto> listSubjectWiseResult = new ArrayList<SubjectWiseModelResultsDto>();
		for(Subject s:listSubjects) {
			List<ModuleResultDto> listModuleResult = new ArrayList<ModuleResultDto>();
			for(Module m:moduleService.listAllBySubject(s.getSubjectId())) {
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
			subDto.setSubjectId(s);
			subDto.setPaperCount(studentPaperReviewService.getCompleteCountBySubjectAndStudent(s.getSubjectId(),student.getStudentId()));
			subDto.setModuleResultList(listModuleResult);
			listSubjectWiseResult.add(subDto);
		}
		mav.addObject("listSubjectWiseResult", listSubjectWiseResult);
		
		return mav;
	}
		
}
