 package com.rcs2.mcqsys.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.ModuleService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.SubjectService;

@Controller
public class SubjectController {
	
	//Subject Managements
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private PaperService paperService;
	
	@RequestMapping("/listSubjects")
	public String listSubjects(Model model) {
	    
		List<Subject> listSubjects = subjectService.listAll();
	    model.addAttribute("listSubjects", listSubjects);
	     
	    return "services/subject-list";
	}
	
	@GetMapping("/showSubjectUpdate")
	public ModelAndView viewSubjectUpdate(@RequestParam Long subjectId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/subject-form");
		Subject subject = subjectService.get(subjectId);
				
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		subject.setUpdateBy(user.getName());
		subject.setUpdateDate(LocalDate.now());
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
			    
		mav.addObject("subject", subject);
		return mav;
	}
	
	@GetMapping("/addSubject")
	public ModelAndView viewAddSubject(HttpSession session) {
		ModelAndView mav = new ModelAndView("services/subject-form");
		
		Subject subject = new Subject();
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);

		subject.setCreateBy(user.getUsername());
		subject.setCreateDate(LocalDate.now());
		subject.setUpdateBy(user.getUsername());
		subject.setUpdateDate(LocalDate.now());
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
			    
		mav.addObject("subject", subject);
		return mav;
	}
	
	@PostMapping("/secure/saveSubject")
	public ResponseEntity<String> saveSubject(@ModelAttribute Subject subject) {
		boolean isDupplicate = false;
				
		List<Subject> listSubjects = subjectService.listAll();
		for (Subject selectedSubject : listSubjects) 
		{
			if((selectedSubject.getSubjectName()).equals(subject.getSubjectName()) && (selectedSubject.getGrade().getGradeId()).equals(subject.getGrade().getGradeId())) {
				isDupplicate = true;
				break;
			}
		}
		
		if(!isDupplicate) {
			
			subject.setUpdateDate(LocalDate.now());
			
			subjectService.save(subject);
						
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			return x ;
		}else {				
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}
	}
	
	@GetMapping("/deleteSubject")
	public ResponseEntity<String> deleteSubject(@RequestParam Long subjectId) {

		boolean hasRelation = false;
		
		List<Module> listModules = moduleService.listAll();
		for (Module module : listModules) 
		{
			if((module.getSubjectId().getSubjectId()).equals(subjectId)) {
				hasRelation = true;
				break;
			}
		}
		
		List<Paper> listPapers = paperService.listAll();
		for (Paper paper : listPapers) 
		{
			if((paper.getSubjectId().getSubjectId()).equals(subjectId)) {
				hasRelation = true;				
				break;
			}
		}
		
		/*List<StudentSubject> listStudents = studentSubjectService.listAll();
		for (StudentSubject studentSubject : listStudentSubject) 
		{
			if((studentSubject.getSubjectId().getSubjectId()).equals(subjectId)) {
				hasRelation = true;
				break;
			}
		}*/
		
		if(hasRelation) {
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}if(!hasRelation) {
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			subjectService.delete(subjectId);
			return x;
		}else {
			return null;
		}
	}
	
	@GetMapping("/subjectDeactive")
	public String subjectDeactive(@RequestParam Long subjectId) {
		Subject subject = subjectService.get(subjectId);
		subject.setActive(false);
		subjectService.save(subject);
		return "redirect:/listSubjects";
	}
	
	@GetMapping("/subjectActive")
	public String subjectActive(@RequestParam Long subjectId) {
		Subject subject = subjectService.get(subjectId);
		subject.setActive(true);
		subjectService.save(subject);
		return "redirect:/listSubjects";
	}
	
	@GetMapping("/getSubjectsByGrade")
	public ResponseEntity<Object> getSubjectsByGrade(@RequestParam long gradeId) {
		List<Subject> listSubjects = subjectService.listAllSubjectByGrade(gradeId);
		return new ResponseEntity<>(listSubjects, HttpStatus.OK);
	}
}















