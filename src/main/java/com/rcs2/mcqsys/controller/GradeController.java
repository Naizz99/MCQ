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
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.Student;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.StudentService;

@Controller
public class GradeController {
	
	@Autowired
	private PaperService paperService;
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private StudentService studentService;
		
	//Grade Management
		@Autowired
	    private GradeService gradeService;
		
		@RequestMapping("/listGrades")
		public String listGrades(Model model,HttpSession session) {
			
		    List<Grade> listGrades = gradeService.listAll();
		    model.addAttribute("listGrades", listGrades);
		    
		    User user = (User)session.getAttribute("user");
			model.addAttribute("user", user);
			
		    return "services/grade-list";
		}
		
		@GetMapping("/showGradeUpdate")
		public ModelAndView viewGradeUpdate(@RequestParam Long gradeId, HttpSession session) {
			ModelAndView mav = new ModelAndView("services/grade-form");
			
			Grade grade = gradeService.get(gradeId);
			
			User user = (User)session.getAttribute("user");
			mav.addObject("user", user);
			
			grade.setUpdateBy(user.getName());
			grade.setUpdateDate(LocalDate.now());
			
			mav.addObject("grade", grade);
			return mav;
		}
		
		@GetMapping("/addGrade")
		public ModelAndView viewAddGrade(HttpSession session) {
			ModelAndView mav = new ModelAndView("services/grade-form");
			
			Grade grade = new Grade();
			
			User user = (User)session.getAttribute("user");
			mav.addObject("user", user);
			
			grade.setCreateBy(user.getUsername());
			grade.setCreateDate(LocalDate.now());
			grade.setUpdateBy(user.getUsername());
			grade.setUpdateDate(LocalDate.now());
			
			mav.addObject("grade", grade);
			return mav;
		}
		
		@PostMapping("/secure/saveGrade")
		public ResponseEntity<String> saveGrade(@ModelAttribute Grade grade) {
			boolean isDupplicate = false;
			
			grade.setUpdateDate(LocalDate.now());
			gradeService.save(grade);
			
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			return x;
		}
		
		@PostMapping("/secure/getGrade")
		public ResponseEntity<Integer> getGrade(@RequestParam Long gradeId) {
			return new ResponseEntity<>(gradeService.get(gradeId).getGrade(), HttpStatus.OK);
		}
		
		@GetMapping("/deleteGrade")
		public ResponseEntity<String> deleteGrade(@RequestParam Long gradeId) {

			boolean hasRelation = false;
			
			List<Paper> listPapers = paperService.listAll();
			for (Paper paper : listPapers) 
			{
				if((paper.getGrade().getGradeId()).equals(gradeId)) {
					hasRelation = true;
					break;
				}
			}
			
			List<Subject> listSubjects = subjectService.listAll();
			for (Subject subject : listSubjects) 
			{
				if((subject.getGrade().getGradeId()).equals(gradeId)) {
					hasRelation = true;
					break;
				}
			}
			
			List<Student> listStudents = studentService.listAll();
			for (Student student : listStudents) 
			{
				if((student.getGrade().getGradeId()).equals(gradeId)) {
					hasRelation = true;
					break;
				}
			}
			
			if(hasRelation) {
				ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}else {
				ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
				gradeService.delete(gradeId);
				return new ResponseEntity<>("success", HttpStatus.OK);
			}
		}
		
		@GetMapping("/gradeDeactive")
		public String gradeDeactive(@RequestParam Long gradeId) {
			Grade grade = gradeService.get(gradeId);
			grade.setActive(false);
			gradeService.save(grade);
			return "redirect:/listGrades";
		}
		
		@GetMapping("/gradeActive")
		public String gradeActive(@RequestParam Long gradeId) {
			Grade grade = gradeService.get(gradeId);
			grade.setActive(true);
			gradeService.save(grade);
			return "redirect:/listGrades";
		}
}
