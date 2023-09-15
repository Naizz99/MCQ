package com.rcs2.mcqsys.controller;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcs2.mcqsys.dto.QuestionAnswerDto;
import com.rcs2.mcqsys.dto.ImageDto;
import com.rcs2.mcqsys.dto.QuestionListDto;
import com.rcs2.mcqsys.dto.answerDto;
import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperAnswer;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.ModuleService;
import com.rcs2.mcqsys.service.PaperAnswerService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class PaperQuestionController {
	
	//PaperQuestion Management
	
	@Autowired
	private PaperQuestionService paperQuestionService;
	
	@Autowired
	private PaperAnswerService paperAnswerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PaperService paperService;
	
	@Autowired
	private ModuleService ModuleService;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	@RequestMapping("/listQuestions")
	public String listQuestions(Model model,HttpSession session) {
				
	    List<PaperQuestion> listPaperQuestions = paperQuestionService.listAll();
	    
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
	    model.addAttribute("listQuestions", listPaperQuestions);
	     
	    return "services/question-list";
	}
	
	@RequestMapping("/listQuestionsByPaperId")
	public ModelAndView listQuestionsByPaperId(@RequestParam Long paperId,HttpSession session) {
		
	    List<PaperQuestion> listPaperQuestions = paperQuestionService.listAllByPaperId(paperId);
	    
	    ModelAndView mav = new ModelAndView("services/question-list");	  
	    
	    User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		mav.addObject("listQuestions", listPaperQuestions);
		mav.addObject("paperId", paperId);
		
		Paper paper = paperService.get(paperId);
		mav.addObject("paper", paper);
		
		int questionCount = 0;
		
		List<PaperQuestion> questionList = paperQuestionService.listAllByPaperId(paperId);
		for(PaperQuestion q: questionList) {
			questionCount++;
		}
		mav.addObject("questionCount", questionCount);
		
		List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByPaperId(paperId);
		mav.addObject("listAnswers", listPaperAnswers);
		
		return mav;
	}
	
	@GetMapping("/showQuestionUpdate")
	public PaperQuestion showQuestionUpdate(@RequestParam Long pqId,HttpSession session) {
			
		PaperQuestion paperQuestion = paperQuestionService.get(pqId);
				
		User user = (User)session.getAttribute("user");
		
		paperQuestion.setUpdateBy(user.getName());
		paperQuestion.setUpdateDate(LocalDate.now());		
	    	
		ModelAndView mav = new ModelAndView("services/question-form");
		
		mav.addObject("user", user);
		
		//Get all modules
		 List<Module> listModule = ModuleService.listAllActive();
	     mav.addObject("moduleList", listModule);
	     
		
		mav.addObject("paperQuestion", paperQuestion);
		return paperQuestion;
	}
	
	@PostMapping("/secure/saveQuestion")
	public ResponseEntity<String> saveQuestion(@ModelAttribute PaperQuestion question) {

		boolean isDupplicate = false;
		boolean isAllQuestionSaved = true;
		
		question.setUpdateDate(LocalDate.now());
		 
		Paper paper = question.getPaperID();
				
		List<PaperQuestion> questionList = paperQuestionService.listAllByPaperId(question.getPaperID().getPaperId());
		for(PaperQuestion q:questionList) {
			if(q.getQuestionId() == question.getQuestionId() ) {
				isDupplicate = true;
				break;
			}
		}
		
		for(PaperQuestion q:questionList) {
			if(q.getQuestion().equals(null)) {
				isAllQuestionSaved = false;
				break;
			}
		}
		
		if(isAllQuestionSaved) {
			paperService.save(paper);
		}
		
		if(question.getQuestionId() == 0) {
			isDupplicate = true;
		}
		
		if(!isDupplicate) {
			paperQuestionService.save(question);
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			return x ;
		}else {
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}
	}
	
	@PostMapping("/secure/saveQuestion2")
	public ResponseEntity<Object> saveQuestion2(@ModelAttribute QuestionAnswerDto q) {
		
		String fileName = null;
		byte[] decodedImg = null;
		Path destinationFile = null;
		boolean isDupplicate = false;
							
		PaperQuestion question = new PaperQuestion();
		question.setQuestion(q.getQuestion());
		question.setModuleId(ModuleService.get(Long.parseLong(q.getModuleId())));
		question.setActive(Boolean.parseBoolean(q.getActive()));
		question.setCreateBy(paperService.get(Long.parseLong(q.getPaperID())).getCreateBy());
		question.setCreateDate(LocalDate.now());
		question.setUpdateBy(paperService.get(Long.parseLong(q.getPaperID())).getUpdateBy());
		question.setUpdateDate(LocalDate.now());
		question.setPaperID(paperService.get(Long.parseLong(q.getPaperID())));
		question.setQuestionId(Integer.parseInt(q.getQuestionId()));
				
		if(!((q.getImage()).equals(""))) {
			fileName = q.getSerial() + "." + q.getExtention();
			decodedImg = Base64.getDecoder().decode(q.getImage().getBytes(StandardCharsets.UTF_8));
			destinationFile = Paths.get(imageLocation + "/questions", fileName);
			try {
				Files.write(destinationFile, decodedImg);
				question.setImage("/questions/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		List<PaperQuestion> questionList = paperQuestionService.listAllByPaperId(question.getPaperID().getPaperId());
		for(PaperQuestion pq:questionList) {
			if(pq.getQuestionId() == question.getQuestionId() ) {
				isDupplicate = true;
			}
		}
				
		if(question.getQuestionId() == 0) {
			isDupplicate = true;
		}
				
		if(!isDupplicate) {
			
			try {
				paperQuestionService.save(question);
			}catch(Exception e) {
				System.out.println("Error while saving question \n " + e);
			}
			
			for(String s : q.getAnswerList()) {
				PaperAnswer answer = new PaperAnswer();
				answer.setActive(true);
				answer.setCreateBy(question.getCreateBy());
				answer.setCreateDate(LocalDate.now());
				answer.setPaperID(question.getPaperID());
				answer.setPqId(question);
				answer.setUpdateBy(question.getUpdateBy());
				answer.setUpdateDate(LocalDate.now());
				
				ObjectMapper mapper = new ObjectMapper();
				try {
		        	answerDto ans = mapper.readValue(s, answerDto.class);
		           
		        	answer.setAnswer(ans.getAnswer());
		        	answer.setAnswer_status(Boolean.parseBoolean(ans.getAnswer_status()));
		        	
		        	if(!((ans.getImage()).equals(""))) {
		        		fileName = ans.getSerial() + "." + ans.getExtention();
			    		decodedImg = Base64.getDecoder().decode(ans.getImage().getBytes(StandardCharsets.UTF_8));
			    		destinationFile = Paths.get(imageLocation + "/answers", fileName);
			    		try {
			    			Files.write(destinationFile, decodedImg);
			    			answer.setImage("/answers/" + fileName);
			    		} catch (IOException e) {
			    			e.printStackTrace();
			    		}
		        	}
		        	
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				try {
					paperAnswerService.save(answer);
				}catch(Exception e) {
					System.out.println("Error while saving answer \n " + e);
				}
				
			}
						
			ResponseEntity<Object> x = new ResponseEntity<>(question.getPqId(), HttpStatus.OK);
			return x ;
		}else {
			ResponseEntity<Object> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}
	}
	
	@PostMapping("/secure/saveAllQuestion")
	public ResponseEntity<String> saveAllQuestion(@ModelAttribute QuestionListDto questionList) {
				
		for(String q: questionList.getQuestionList()) {
			
			ObjectMapper mapper = new ObjectMapper();
			try {
	        	QuestionAnswerDto que = mapper.readValue(q, QuestionAnswerDto.class);
	           
	        	PaperQuestion question = paperQuestionService.get((Long.parseLong(que.getPqId())));
	        	
	        	try {
	        		question.setQuestion(que.getQuestion());
		        	question.setUpdateBy(question.getUpdateBy());
		        	question.setUpdateDate(LocalDate.now());
		        	
	        		paperQuestionService.save(question);
	        		for(String a:que.getAnswerList()) {
		        		ObjectMapper answerMapper = new ObjectMapper();
		        		answerDto ans = answerMapper.readValue(a, answerDto.class);
		    	           
	    	        	PaperAnswer answer = paperAnswerService.get((Long.parseLong(ans.getPaId())));
	    	        	answer.setAnswer(ans.getAnswer());
	    	        	answer.setAnswer_status(Boolean.parseBoolean(ans.getAnswer_status()));
	    	        	answer.setUpdateBy(question.getUpdateBy());
	    	        	answer.setUpdateDate(LocalDate.now());
	    	        	
	    	        	paperAnswerService.save(answer);
		        	}
        		}
        		catch(Exception e) {
        			e.printStackTrace();
        		}	        	
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		ResponseEntity<String> x = new ResponseEntity<>("Success", HttpStatus.OK);
		return x ;
	}
	
	@PostMapping("/secure/saveQuestionImage")
	public ResponseEntity<String> saveQuestionImage(@ModelAttribute ImageDto img) {
	
		String fileName = img.getSerial() + "." + img.getExtention();
		
		byte[] decodedImg = Base64.getDecoder().decode(img.getImage().getBytes(StandardCharsets.UTF_8));
		Path destinationFile = Paths.get(imageLocation + "/questions", fileName);
		try {
			Files.write(destinationFile, decodedImg);
			PaperQuestion question = paperQuestionService.get(Long.parseLong(img.getReference()));
			question.setImage("/questions/" + fileName);
			paperQuestionService.save(question);
			ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
			return x;
		} catch (IOException e) {
			e.printStackTrace();
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}
	} 
	
	@PostMapping("/secure/updateQuestion")
	public ResponseEntity<String> updateQuestion(@ModelAttribute PaperQuestion question) {
						
		question.setUpdateDate(LocalDate.now());
		
		paperQuestionService.save(question);
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x ;
	}	
	
	@GetMapping("/addQuestion")
	public ModelAndView viewAddQuestion(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/question-form");
		PaperQuestion question = new PaperQuestion();
				
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		question.setCreateBy(user.getUsername());
		question.setCreateDate(LocalDate.now());
		question.setUpdateBy(user.getUsername());
		question.setUpdateDate(LocalDate.now());	
		question.setActive(true);
		
		mav.addObject("question", question);
		
		//Get all modules
		 List<Module> listModule = ModuleService.listAllActive();
	     mav.addObject("moduleList", listModule);
	     
		//Get all papers
		List<Paper> listPapers = paperService.listAllActive();
	    mav.addObject("paperList", listPapers);
	    
		return mav;
	}
	
	@GetMapping("/addQuestionByPaperId")
	public ModelAndView viewAddQuestion(@RequestParam Paper paperId,HttpSession session) {
		
		boolean isCountExceed = false;
		int questionCount = 0;
		
		List<PaperQuestion> questionList = paperQuestionService.listAllByPaperId(paperId.getPaperId());
		for(PaperQuestion q: questionList) {
			questionCount++;
		}
		
		if(questionCount >= paperId.getNumberOfQuestion()) {
			isCountExceed = true;
		}
		
		ModelAndView mav = new ModelAndView("services/question-form");
								
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		//Create Question
		PaperQuestion question = new PaperQuestion();
		question.setCreateBy(user.getUsername());
		question.setCreateDate(LocalDate.now());
		question.setUpdateBy(user.getUsername());
		question.setUpdateDate(LocalDate.now());
		question.setPaperID(paperId);
		question.setActive(true);
		question.setQuestionId(++questionCount);
		mav.addObject("question", question);
		
		//Create Answer
		PaperAnswer answer = new PaperAnswer();
		answer.setCreateBy(user.getUsername());
		answer.setCreateDate(LocalDate.now());
		answer.setUpdateBy(user.getUsername());
		answer.setUpdateDate(LocalDate.now());
		answer.setPaperID(paperId);
		answer.setAnswer_status(false);
		answer.setActive(true);
		mav.addObject("answer", answer);
		
		//Get new answer
		ArrayList<PaperAnswer> answerList = new ArrayList<PaperAnswer>();
		
		for(int i=1;i<=question.getPaperID().getAnswersPerQuestion();i++) {
			PaperAnswer paperAnswer = new PaperAnswer();
			
			paperAnswer.setActive(true);
			paperAnswer.setCreateBy(user.getUsername());
			paperAnswer.setCreateDate(LocalDate.now());
			paperAnswer.setPaperID(paperId);
			paperAnswer.setPqId(question);
			paperAnswer.setUpdateBy(user.getUsername());
			paperAnswer.setUpdateDate(LocalDate.now());
			paperAnswer.setAnswer("Answer : " + i);
			//paperAnswerService.save(paperAnswer);
			
			answerList.add(paperAnswer);
		}
		mav.addObject("answerList", answerList);
		
		//Get all modules
		 List<Module> listModule = ModuleService.listAllActiveBySubject(paperId.getSubjectId());
	     mav.addObject("moduleList", listModule);
	     
		//Get all papers
		List<Paper> listPapers = paperService.listAllActive();
	    mav.addObject("paperList", listPapers);
		
	    mav.addObject("paperId", paperId);
	    
		if(!isCountExceed) {
			return mav;
		}
		//return null;
		return mav;
		
	}	
	
	@GetMapping("/updateQuestionByQuestionId")
	public ModelAndView updateQuestionByPaperId(@RequestParam Long pqId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/question-update");
								
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		//Get Question
		PaperQuestion question = paperQuestionService.get(pqId);
		question.setUpdateBy(user.getName());
		question.setUpdateDate(LocalDate.now());
		mav.addObject("question", question);
		
		//Get answer list
		List<PaperAnswer> answerList = paperAnswerService.listAllByQuestionId(pqId);
		
		for(PaperAnswer answer:answerList) {
			answer.setUpdateBy(user.getName());
			answer.setUpdateDate(LocalDate.now());
		}
		mav.addObject("answerList", answerList);
		
		//Get all modules
		 List<Module> listModule = ModuleService.listAllActiveBySubject(question.getModuleId().getSubjectId());
		 //List<Module> listModule = ModuleService.listAll();
	     mav.addObject("moduleList", listModule);
	     
		//Get all papers
		List<Paper> listPapers = paperService.listAllActive();
	    mav.addObject("paperList", listPapers);
		
	    mav.addObject("paperId", question.getPaperID());
	    
		return mav;
	}	
	
	@GetMapping("/getQuestionImage")
	public ResponseEntity<String> getQuestionImage(@RequestParam Long pqId) {
		
		PaperQuestion question = paperQuestionService.get(pqId);
		
		if(question.getImage() != null) {
			
			/*
			String path = (imageLocation + question.getImage()).replace('/', '\\');
			String base64String = null;
			
			byte[] byteData;
			try {
				byteData = Files.readAllBytes(Paths.get(path));
				base64String = Base64.getEncoder().encodeToString(byteData);
				ResponseEntity<String> x = new ResponseEntity<>(base64String, HttpStatus.OK);
				return x;
			} catch (IOException e) {
				e.printStackTrace();
				ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return x;
			}
			*/
			
			ResponseEntity<String> x = new ResponseEntity<>(question.getImage(), HttpStatus.OK);
			return x;
			
		}else {
			ResponseEntity<String> x = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return x;
		}
		
		
	}
	
	@GetMapping("/deleteQuestion")
	public ResponseEntity<String> deleteQuestion(@RequestParam Long pqId,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		
		try {
			List<PaperAnswer> answerList = paperAnswerService.listAllByQuestionId(pqId);
			for(PaperAnswer answer:answerList) {
				answer.setAnswer(null);
				answer.setAnswer_status(false);
				answer.setUpdateBy(user.getName());
				answer.setUpdateDate(LocalDate.now());
				paperAnswerService.save(answer);
			}
			
			PaperQuestion question = paperQuestionService.get(pqId);
			
			File delteFile = new File(imageLocation+ question.getImage());
	        delteFile.delete();
	        
			question.setQuestion(null);
			question.setUpdateBy(user.getName());
			question.setUpdateDate(LocalDate.now());
			question.setImage(null);
			
			paperQuestionService.save(question);
			
			Paper paper = paperService.get(question.getPaperID().getPaperId());
			paper.setActive(false);
			paperService.save(paper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x;
	}
	
	@GetMapping("/questionDeactive")
	public String questionDeactive(@RequestParam Long pqId) {
		PaperQuestion question = paperQuestionService.get(pqId);
		question.setActive(false);
		paperQuestionService.save(question);
		return "redirect:/listQuestionsByPaperId?paperId="+question.getPaperID().getPaperId();
	}
	
	@GetMapping("/questionActive")
	public String questionActive(@RequestParam Long pqId) {
		PaperQuestion question = paperQuestionService.get(pqId);
		question.setActive(true);
		paperQuestionService.save(question);
		return "redirect:/listQuestionsByPaperId?paperId="+question.getPaperID().getPaperId();
	}
	
}
