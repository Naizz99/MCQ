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
import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.model.Subject;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.ModuleService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.SubjectService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class ModuleController {
	
	//Module Management
	@Autowired
    private ModuleService moduleService;
	
	@Autowired
    private SubjectService subjectService;
	
	@Autowired
    private GradeService gradeService;
	
	@Autowired
    private UserService userService;

	@Autowired
    private PaperQuestionService paperQuestionService;
	
	@RequestMapping("/listModules")
	public String listModules(Model model,HttpSession session) {
	    List<Module> listModules = moduleService.listAll();
	    model.addAttribute("listModules", listModules);

	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
	    return "services/module-list";
	}
	
	@GetMapping("/getModuleBySubject")
	public ResponseEntity<Object> getModuleBySubject(@RequestParam long subjectId) {
			
		Subject subject = subjectService.get(subjectId);
		
		List<Module> listModules = moduleService.listAllActiveBySubject(subject);
		return new ResponseEntity<>(listModules, HttpStatus.OK);
	}
	
	@GetMapping("/showModuleUpdate")
	public ModelAndView viewModuleUpdate(@RequestParam Long moduleId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/module-form");
		Module module = moduleService.get(moduleId);
				
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		module.setUpdateBy(user.getName());
		module.setUpdateDate(LocalDate.now());
				
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
			    
	  //Get all subjects
  		List<Subject> listSubjects = subjectService.listAllActive();
  	    mav.addObject("subjectList", listSubjects);
  	    
		mav.addObject("module", module);
		return mav;		
	}
	
	@GetMapping("/addModule")
	public ModelAndView viewAddModule(HttpSession session) {
		ModelAndView mav = new ModelAndView("services/module-form");
		
		Module module = new Module();
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);

		module.setCreateBy(user.getUsername());
		module.setCreateDate(LocalDate.now());
		module.setUpdateBy(user.getUsername());
		module.setUpdateDate(LocalDate.now());
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
	    mav.addObject("gradeList", listGrades);
	    	  	    
	    mav.addObject("module", module);
		return mav;
	}
	
	@PostMapping("/secure/saveModule")
	public ResponseEntity<String> saveModule(@ModelAttribute Module module) {
		boolean isDupplicate = false;
				
		List<Module> listModules = moduleService.listAll();
		for (Module selectedModule : listModules) 
		{
			if((selectedModule.getName()).equals(module.getName()) && (selectedModule.getSubjectId()).equals(module.getSubjectId())) {
				isDupplicate = true;
				break;
			}
		}
		
		if(!isDupplicate) {
			
			module.setUpdateDate(LocalDate.now());
			
			moduleService.save(module);
						
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			return x ;
		}else {				
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}
	}
	
	@GetMapping("/deleteModule")
	public ResponseEntity<String> deleteModule(@RequestParam Long moduleId) {

		boolean hasRelation = false;
		List<PaperQuestion> listQuestions = paperQuestionService.listAll();
		for (PaperQuestion paperQuestion : listQuestions) 
		{
			boolean sts = false;
			try {
				sts = paperQuestion.getModuleId().getModuleId() == moduleId;
			}catch(Exception e){
				
			}
			if(sts) {
				hasRelation = true;				
				break;
			}
		}
		if(hasRelation) {
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}if(!hasRelation) {
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			moduleService.delete(moduleId);
			return x;
		}else {
			return null;
		}
	}
	
	@GetMapping("/moduleDeactive")
	public String moduleDeactive(@RequestParam Long moduleId) {
		Module module = moduleService.get(moduleId);
		module.setActive(false);
		module.setUpdateDate(LocalDate.now());
		moduleService.save(module);
		return "redirect:/listModules";
	}
	
	@GetMapping("/moduleActive")
	public String moduleActive(@RequestParam Long moduleId) {
		Module module = moduleService.get(moduleId);
		module.setActive(true);
		module.setUpdateDate(LocalDate.now());
		moduleService.save(module);
		return "redirect:/listModules";
	}
}
