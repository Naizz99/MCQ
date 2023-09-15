package com.rcs2.mcqsys.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
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
import com.rcs2.mcqsys.dto.AnswerImageDto;
import com.rcs2.mcqsys.dto.answerDto;
import com.rcs2.mcqsys.model.Paper;
import com.rcs2.mcqsys.model.PaperAnswer;
import com.rcs2.mcqsys.model.PaperQuestion;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.PaperAnswerService;
import com.rcs2.mcqsys.service.PaperQuestionService;
import com.rcs2.mcqsys.service.PaperService;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class AnswerController {
	
	@Autowired
	private PaperAnswerService paperAnswerService;
	
	@Autowired
	private PaperQuestionService paperQuestionService;
	
	@Autowired
	private PaperService paperService;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	@RequestMapping("/listAnswers")
	public String listAnswers(Model model,HttpSession session) {
		
	    List<PaperAnswer> listPaperAnswers = paperAnswerService.listAll();
	    model.addAttribute("listAnswers", listPaperAnswers);
	   
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
	    Paper paper = null;
	    for(PaperAnswer p:listPaperAnswers) {
	    	paper = p.getPaperID();
	    }
	    model.addAttribute("paper", paper);
	    
	    return "services/answer-list";
	}
	
	@RequestMapping("/listAnswersByPqId")
	public ModelAndView listAnswersByQuestionId(@RequestParam Long pqId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/answer-list");	 
		
	    List<PaperAnswer> listPaperAnswers = paperAnswerService.listAllByQuestionId(pqId);
	    mav.addObject("listAnswers", listPaperAnswers);
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		 
		Paper paper = null;
	    for(PaperAnswer p:listPaperAnswers) {
	    	paper = p.getPaperID();
	    }
	    mav.addObject("paper", paper);
	    
		return mav;
	}
	
	@GetMapping("/updateAnswerByPaId")
	public ModelAndView updateAnswerByPaId(@RequestParam Long paId,HttpSession session) {
				
		ModelAndView mav = new ModelAndView("services/answer-update");
		
		PaperAnswer paperAnswer = paperAnswerService.get(paId);
		mav.addObject("answer", paperAnswer);
		mav.addObject("paper", paperAnswer.getPaperID());
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);		
		paperAnswer.setUpdateBy(user.getName());
		paperAnswer.setUpdateDate(LocalDate.now());		
	    
		mav.addObject("paperList", paperService.listAllActive());
		
		return mav;
	}
	
	@PostMapping("/secure/saveAnswer")
	public ResponseEntity<String> saveAnswer(@ModelAttribute PaperAnswer answer) {
		
		PaperQuestion question = paperQuestionService.getLastQuestionByPaper(answer.getPaperID().getPaperId());
		answer.setPqId(question);
		
		Paper paper = paperService.get(answer.getPaperID().getPaperId());
		
		int ansCount = paper.getAnswersPerQuestion();
		int totAns = 0;
		List<PaperAnswer> listAnswers = paperAnswerService.listAllByQuestionId(question.getPqId());
		
		for(PaperAnswer p:listAnswers) {
			totAns++;
		}
		if(totAns < ansCount) {							
			answer.setUpdateDate(LocalDate.now());
			paperAnswerService.save(answer);
						
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/secure/saveAnswer2")
	public ResponseEntity<String> saveAnswer2(@ModelAttribute answerDto ans) {
		
		PaperAnswer answer = new PaperAnswer();
		answer.setAnswer(ans.getAnswer());
		answer.setAnswer_status(Boolean.parseBoolean(ans.getAnswer_status()));
		answer.setCreateDate(LocalDate.now());
		
		int ansCount = paperService.get(answer.getPaperID().getPaperId()).getAnswersPerQuestion();
		int totAns = 0;
		
		if(totAns < ansCount) {
			answer.setUpdateDate(LocalDate.now());
			paperAnswerService.save(answer);
						
			return new ResponseEntity<>("success", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/secure/updateAnswer")
	public ResponseEntity<String> updateAnswer(@ModelAttribute PaperAnswer answer) {
		
		PaperQuestion question = paperQuestionService.getLastQuestionByPaper(answer.getPaperID().getPaperId());
		
		answer.setUpdateDate(LocalDate.now());
		
		paperAnswerService.save(answer);
					
		return new ResponseEntity<>("success", HttpStatus.OK);		
	}
	
	@PostMapping("/secure/saveAnswerImage")
	public ResponseEntity<String> saveAnswerImage(@ModelAttribute AnswerImageDto img) {
		
		String fileName = img.getSerial() + "." + img.getExtention();
		
		byte[] decodedImg = Base64.getDecoder().decode(img.getImage().getBytes(StandardCharsets.UTF_8));
		Path destinationFile = Paths.get(imageLocation + "/answers", fileName);
		try {
			Files.write(destinationFile, decodedImg);
			PaperAnswer answer = paperAnswerService.get(Long.parseLong(img.getAnswerNo()));
			answer.setImage("/answers/" + fileName);
			paperAnswerService.save(answer);
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	} 
	
	@GetMapping("/getAnswerImage")
	public ResponseEntity<String> getAnswerImage(@RequestParam Long paId) {
		
		PaperAnswer answer = paperAnswerService.get(paId);
		
//		if(answer.getImage() != null) {
//			String path = (imageLocation + answer.getImage()).replace('/', '\\');
//			String base64String = null;
//			
//			byte[] byteData;
//			try {
//				byteData = Files.readAllBytes(Paths.get(path));
//				base64String = Base64.getEncoder().encodeToString(byteData);
//				return new ResponseEntity<>(base64String, HttpStatus.OK);
//			} catch (IOException e) {
//				e.printStackTrace();
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//			}
//		}else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}	
		
		return new ResponseEntity<>(answer.getImage(), HttpStatus.OK);
	}
	
	@GetMapping("/addAnswerByQuestionId")
	public ModelAndView viewAddAnswer(@RequestParam long pqId,long paperId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/answer-form");
		PaperAnswer answer = new PaperAnswer();
		PaperQuestion question = paperQuestionService.get(pqId);
		
		Paper paper = paperService.get(paperId);
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		answer.setCreateBy(user.getUsername());
		answer.setCreateDate(LocalDate.now());
		answer.setUpdateBy(user.getUsername());
		answer.setUpdateDate(LocalDate.now());
		answer.setPqId(question);
		answer.setPaperID(paper);
		answer.setActive(true);
		mav.addObject("answer", answer);
		
		//Get all papers
		List<Paper> listPapers = paperService.listAllActive();
	    mav.addObject("paperList", listPapers);
	    
	    mav.addObject("paper", paper);
	    
		return mav;
	}
	
	
	
	@GetMapping("/deleteAnswer")
	public ResponseEntity<String> deletePaper(@RequestParam Long paId) {
		
		PaperAnswer answer = paperAnswerService.get(paId);
		answer.setAnswer(null);
		answer.setAnswer_status(false);
		answer.setCreateBy(null);
		answer.setCreateDate(null);
		answer.setUpdateBy(null);
		answer.setUpdateDate(null);
		paperAnswerService.save(answer);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/correctAnswer")
	public ResponseEntity<Object> correctAnswer(@RequestParam Long paId) {
		
		PaperAnswer answer = paperAnswerService.get(paId);
		answer.setAnswer_status(true);
		paperAnswerService.save(answer);	
		
		return new ResponseEntity<>(answer.getPaperID(), HttpStatus.OK);
	}
	
	@GetMapping("/incorrectAnswer")
	public ResponseEntity<Object> incorrectAnswer(@RequestParam Long paId) {
		
		PaperAnswer answer = paperAnswerService.get(paId);
		answer.setAnswer_status(false);
		paperAnswerService.save(answer);	
		
		return new ResponseEntity<>(answer.getPaperID(), HttpStatus.OK);
	}
	
}
