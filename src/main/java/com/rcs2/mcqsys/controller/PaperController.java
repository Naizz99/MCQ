package com.rcs2.mcqsys.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.rcs2.mcqsys.dto.PaperDto;
import com.rcs2.mcqsys.dto.PaperViewDto;
import com.rcs2.mcqsys.model.ActiveSessions;
import com.rcs2.mcqsys.model.ClassRoomPaperReviewDetail;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperAnswer;
import com.rcs2.mcqsys.model.PaperDetailAnswer;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.Role;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.model.PaperTemplate;
import com.rcs2.mcqsys.model.StudentPaperReview;
import com.rcs2.mcqsys.model.StudentPaperReviewDetail;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.ActiveSessionsService;
import com.rcs2.mcqsys.service.ClassRoomPaperReviewDetailService;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.PaperAnswerService;
import com.rcs2.mcqsys.service.PaperDetailAnswerService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.PaperTemplateService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.StudentPaperReviewDetailService;
import com.rcs2.mcqsys.service.StudentPaperReviewService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.ModuleService;

@Controller
public class PaperController {
	//Paper Management
	
	@Autowired
    private PaperService paperService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
    private SubjectService subjectService;
	
	@Autowired
    private PublisherService publisherService;
	
	@Autowired
    private PublisherUserService publisherUserService;
	
	@Autowired
	private StudentPaperReviewService studentPaperReviewService;
	
	@Autowired
	private StudentPaperReviewDetailService studentPaperReviewDetailService;
	
	@Autowired
	private ClassRoomPaperReviewDetailService classRoomPaperReviewDetailService;
	
	@Autowired
	private PaperQuestionService paperQuestionService;
	
	@Autowired
	private PaperDetailAnswerService paperDetailAnswerService;
	
	@Autowired
	private PaperAnswerService paperAnswerService;
	
	@Autowired
	private ActiveSessionsService activeSessionsService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private PaperTemplateService paperTemplateService;
		
	@Autowired
	private RoleService roleService;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	@RequestMapping("/listPapers")
	public String listPapers(Model model,HttpSession session) {

		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		List<Paper> listPapers = new ArrayList<Paper>();
		
		Publisher publisher = new Publisher();
		
		if (loggedRoles.contains(roleService.getByRole("EDITOR"))) {
		
			for(Paper p : paperService.listAllByUsername(user.getUsername())) {
				
				if(!p.isActive()) {
					listPapers.add(p);
				}
				
			}
			publisher = (publisherUserService.getByUserId(user.getUserId())).getPoId();
		
		}else if (loggedRoles.contains(roleService.getByRole("AUTHOR"))) {
		
			listPapers = paperService.listAllByPublisher((publisherUserService.getByUserId(user.getUserId())).getPoId());
			publisher = (publisherUserService.getByUserId(user.getUserId())).getPoId();
		
		}else if (loggedRoles.contains(roleService.getByRole("ADMIN"))) {
		
			listPapers = paperService.listAll();
		
		}else if (loggedRoles.contains(roleService.getByRole("LECTURER"))) {
			
//			List<Paper> listPapers = paperService.listAllByLecturer(user.getUsername(),publisherService.get(0));
			listPapers = paperService.listAllByUsername(user.getUsername());
		    model.addAttribute("poId", 0);
		
		}else {
			listPapers = paperService.listAll();
		}
		model.addAttribute("poId", publisher);	
		model.addAttribute("listPapers", listPapers);
		return "services/paper-list";
	}
	
	@CrossOrigin
	@GetMapping("/viewPaper")
	public ModelAndView adminViewPaper(@RequestParam Long paperId,HttpSession session) {
		ModelAndView mav = new ModelAndView("services/paper-view");
		
		Paper paper = paperService.get(paperId);
		List<PaperQuestion> listQuestions = paperQuestionService.listAllByPaperId(paperId);		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paperId);		

		List<PaperViewDto> listPaperQuestions = new ArrayList();
		
		for(PaperQuestion q:listQuestions) {
			
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
		
		/*
		for(PaperAnswer a:listPaperAnswers) {
			if(a.getImage() != null) {
				String path = (imageLocation + a.getImage()).replace('/', '\\');
				String base64String = null;
				
				byte[] byteData;
				try {
					byteData = Files.readAllBytes(Paths.get(path));
					base64String = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(byteData);
					a.setImage(base64String);
				} catch (IOException e) {
					e.printStackTrace();
					a.setImage(null);
				}
			}
		}
		*/
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		mav.addObject("paper", paper);
		mav.addObject("listQuestions", listPaperQuestions);
		mav.addObject("listAnswers", listPaperAnswers);
		
		return mav;
	}
	
	@GetMapping("/showPaperUpdate")
	public ModelAndView viewPaperUpdate(@RequestParam Long paperId,HttpSession session) {
		ModelAndView mav = new ModelAndView("services/paper-form");
		Paper paper = paperService.get(paperId);
				
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		List<PaperQuestion> listQuestions = paperQuestionService.listAllByPaperId(paperId);		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paperId);		

		List<PaperViewDto> listPaperQuestions = new ArrayList();
		
		for(PaperQuestion q:listQuestions) {
			
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

		mav.addObject("listQuestions", listPaperQuestions);
		mav.addObject("listAnswers", listPaperAnswers);
		
		Publisher publisher = new Publisher();
			
		if ((loggedRoles.contains(roleService.getByRole("AUTHOR"))) || (loggedRoles.contains(roleService.getByRole("EDITOR")))) {
			publisher = (publisherUserService.getByUserId(user.getUserId())).getPoId();
		}
		mav.addObject("poId", publisher);
		paper.setUpdateBy(user.getName());
		paper.setUpdateDate(LocalDate.now());
				
		paper.setSubjectId(paper.getSubjectId());
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);
		    
	    //Get all paper publishers 
		List<Publisher> lists = publisherService.listAllActive();
		mav.addObject("publisherList", lists);
			    
		//Get all templates
		List<PaperTemplate> listTemplates = paperTemplateService.listAll();
		mav.addObject("templateList", listTemplates);
		
		mav.addObject("paper", paper);
		return mav;
	}
	
	@GetMapping("/addPaper")
	public ModelAndView viewAddPaper(HttpSession session) {
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		
		User user = (User)session.getAttribute("user");
		Paper paper = new Paper();
		ModelAndView mav = new ModelAndView("services/paper-form");
		
		if ((loggedRoles.contains(roleService.getByRole("AUTHOR"))) || (loggedRoles.contains(roleService.getByRole("EDITOR")))) {
			paper.setPublisher((publisherUserService.getByUserId(user.getUserId())).getPoId());
		}
		
		paper.setCreateBy(user.getUsername());
		paper.setCreateDate(LocalDate.now());
		paper.setUpdateBy(user.getUsername());
		paper.setUpdateDate(LocalDate.now());
		paper.setNumberOfQuestion(1);
		paper.setAnswersPerQuestion(2);
		
		Subject subject = new Subject();
		paper.setSubjectId(subject);
		
		mav.addObject("user", user);
		
		Publisher publisher = new Publisher();
		if ((loggedRoles.contains(roleService.getByRole("AUTHOR"))) || (loggedRoles.contains(roleService.getByRole("EDITOR")))) {
			publisher = (publisherUserService.getByUserId(user.getUserId())).getPoId();
		}
		mav.addObject("poId", publisher);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);
		    
	   //Get all subjects
		List<Subject> listSubjects = subjectService.listAllActive();
		mav.addObject("subjectList", listSubjects);
		
	    //Get all paper publishers 
		List<Publisher> lists = publisherService.listAllActive();
		mav.addObject("publisherList", lists);
			    
		//Get all templates
		List<PaperTemplate> listTemplates = paperTemplateService.listAll();
		mav.addObject("templateList", listTemplates);
		
		mav.addObject("paper", paper);
		return mav;
	}
	
	@PostMapping("/secure/savePaper")
	public ResponseEntity<Object> savePaper(@ModelAttribute Paper paper) {
		
		int questionCount = 0;
		long tempQuestion = 0;
		List<PaperQuestion> questionList = paperQuestionService.listAllByPaperId(paper.getPaperId());
		for(PaperQuestion q: questionList) {
			questionCount++;
			tempQuestion = q.getQuestionId();
		}
		
		if(paper.getNumberOfQuestion() != questionCount) {
			
		}
		
		int answerCount = 0;
		List<PaperAnswer> answerList = paperAnswerService.listAllByQuestionId(tempQuestion);
		for(PaperAnswer a: answerList) {
			answerCount++;
		}
		
		paper.setUpdateDate(LocalDate.now());
		
		paperService.save(paper);
					
		ResponseEntity<Object> x = new ResponseEntity<>(paper.getPaperId(), HttpStatus.OK);
		return x ;
	}
	
	@PostMapping("/secure/savePaper2")
	public ResponseEntity<Object> savePaper(@ModelAttribute PaperDto p) {
		
		Paper paper = new Paper();
		paper.setName(p.getName());
		paper.setPublisher(publisherService.get(Long.parseLong(p.getPublisher())));
		paper.setSubjectId(subjectService.get(Long.parseLong(p.getSubjectId())));
		paper.setTime(Integer.parseInt(p.getTime()));
		paper.setGrade(gradeService.get(Long.parseLong(p.getGrade())));
		paper.setNumberOfQuestion(Integer.parseInt(p.getNumberOfQuestion()));
		paper.setAnswersPerQuestion(Integer.parseInt(p.getAnswersPerQuestion()));
		paper.setActive(Boolean.parseBoolean(p.getActive()));
		paper.setCreateBy(p.getUser());
		paper.setCreateDate(LocalDate.now());
		paper.setUpdateBy(p.getUser());
		paper.setUpdateDate(LocalDate.now());		
		paper.setTemplateId(p.getTemplate());
		paper.setAvailableForBuy(p.isAvailableForBuy());
		paperService.save(paper);
					
		ResponseEntity<Object> x = new ResponseEntity<>(paper.getPaperId(), HttpStatus.OK);
		return x ;
	}
	
	@GetMapping("/deletePaper")
	public ResponseEntity<String> deletePaper(@RequestParam Long paperId) {
		
		boolean hasRelation = false;
		
		List<ActiveSessions> listActiveSessions = activeSessionsService.listAllByPaperId(paperId);
		for (ActiveSessions activeSession : listActiveSessions) 
		{
//			if((activeSession.getPaperID()).equals(paperId)) {
//				hasRelation = true;
//				break;
//			}
			activeSessionsService.delete(activeSession.getAsId());
		}
		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paperId);
		for (PaperAnswer paperAnswer : listPaperAnswers) 
		{
			paperAnswerService.delete(paperAnswer.getPaId());
		}
		
		List<PaperDetailAnswer> listPaperDetailAnswers = paperDetailAnswerService.listByPaperId(paperId);
		for (PaperDetailAnswer paperDetailAnswer : listPaperDetailAnswers) 
		{
			paperDetailAnswerService.delete(paperDetailAnswer.getPdaId());
		}
		
		List<PaperQuestion> listPaperQuestions = paperQuestionService.listAllByPaperId(paperId);
		for (PaperQuestion paperQuestion : listPaperQuestions) 
		{
			paperQuestionService.delete(paperQuestion.getPqId());
		}
		
		List<StudentPaperReview> listStudentPaperReviews = studentPaperReviewService.listByPaperId(paperId);
		for (StudentPaperReview studentPaperReview : listStudentPaperReviews) 
		{
			for(StudentPaperReviewDetail sd:studentPaperReviewDetailService.listAllByReviewId(studentPaperReview.getSprId())) {
				studentPaperReviewDetailService.delete(sd.getSprdId());
			}
			studentPaperReviewService.delete(studentPaperReview.getSprId());
		}
		paperService.delete(paperId);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/paperDeactive")
	public String paperDeactive(@RequestParam Long paperId) {
		Paper paper = paperService.get(paperId);
		paper.setActive(false);		
		paperService.save(paper);
		return "redirect:/listPapers";
	}
	
	@GetMapping("/paperActive")
	public String paperActive(@RequestParam Long paperId) {
		Paper paper = paperService.get(paperId);
		paper.setActive(true);		
		paperService.save(paper);
		return "redirect:/listPapers";
	}
	
	@RequestMapping("/contentWizard")
	public ModelAndView contentWizard(HttpSession session) {	
		List<Role> loggedRoles = (List<Role>)session.getAttribute("loggedRoles");
		User user = (User)session.getAttribute("user");
		ModelAndView mav = new ModelAndView("services/content-wizard");
		mav.addObject("user", user);
		Publisher publisher = publisherService.get(0);
	    mav.addObject("publisher", publisher);
		if (loggedRoles.contains(roleService.getByRole("AUTHOR")) || (loggedRoles.contains(roleService.getByRole("EDITOR")))) {
			publisher = (publisherUserService.getByUserId(user.getUserId())).getPoId();
		    mav.addObject("publisher", publisher);
		}else {
			//Get all paper publishers 
			List<Publisher> publishers = publisherService.listAllActive();
		    mav.addObject("publisherList", publishers);
		}
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
		//Get all subjects
		 List<Subject> listSubjects = subjectService.listAllActive();
	     mav.addObject("subjectList", listSubjects);
		 //Get all modules
	    List<Module> listModule = moduleService.listAllActive();
	    mav.addObject("moduleList", listModule);
	    //Add Logged User
	    mav.addObject("loggedUser", user.getUsername());
		//Get all templates
		List<PaperTemplate> listTemplates = paperTemplateService.listAll();
	    mav.addObject("templateList", listTemplates);
		return mav;
	}
	
	@GetMapping("/getPaper")
	public ResponseEntity<Paper> getPaper(@RequestParam Long paperId) {
		Paper paper = paperService.get(paperId);
		return new ResponseEntity<>(paper, HttpStatus.OK);
	}
	
	@GetMapping("/addComment")
	public ResponseEntity<String> deletePaper(@RequestParam String type,@RequestParam long reviewId,@RequestParam String comment,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		
		if(type.equals("classroom")) {
			ClassRoomPaperReviewDetail reviewDetail = classRoomPaperReviewDetailService.get(reviewId);
			reviewDetail.setReviewerId(user);
			reviewDetail.setReviewerComment(comment);
			classRoomPaperReviewDetailService.save(reviewDetail);
		}else {
			StudentPaperReviewDetail reviewDetail = studentPaperReviewDetailService.get(reviewId);
			reviewDetail.setReviewerId(user);
			reviewDetail.setReviewerComment(comment);
			studentPaperReviewDetailService.save(reviewDetail);
		}
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
