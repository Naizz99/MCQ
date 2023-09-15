package com.rcs2.mcqsys.controller;

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

import com.rcs2.mcqsys.dto.PaperBundleDto;
import com.rcs2.mcqsys.model.Grade;
import com.rcs2.mcqsys.model.LinkPaperBundle;
import com.rcs2.mcqsys.model.PaperBundle;
import com.rcs2.mcqsys.model.Publisher;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.GradeService;
import com.rcs2.mcqsys.service.LinkPaperBundleService;
import com.rcs2.mcqsys.service.PaperBundleService;
import com.rcs2.mcqsys.service.PaperService;
import com.rcs2.mcqsys.service.PublisherService;
import com.rcs2.mcqsys.service.PublisherUserService;
import com.rcs2.mcqsys.service.RoleService;
import com.rcs2.mcqsys.service.UserService;

@Controller
public class PaperBundleController {	
	@Autowired
    private PaperBundleService paperBundleService;
	
	@Autowired
    private LinkPaperBundleService linkPaperBundleService;
	
	@Autowired
    private PaperService paperService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private PublisherService publisherService;
	
	@Autowired
    private PublisherUserService publisherUserService;
	
	@Autowired
	private RoleService roleService;
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	
	//Manage Paper Bundle
	
	@GetMapping("/viewBundle")
	public ModelAndView viewBundle(@RequestParam Long bundleId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/bundle-view");
		
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
	
	@RequestMapping("/listPaperBundle")
	public String listPaperBundle(Model model,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<PaperBundle> listBundles = new ArrayList<PaperBundle>();
		
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("AUTHOR")) || (roleService.listByUser(user.getUserId())).contains(roleService.getByRole("EDITOR"))) {
			listBundles = paperBundleService.listAllByPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId().getPoId());
		}
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("LECTURER"))) {
			listBundles = paperBundleService.listAllByUser(user.getUserId());
		}
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("ADMIN")) || (roleService.listByUser(user.getUserId())).contains(roleService.getByRole("SUPERADMIN"))) {
			listBundles = paperBundleService.listAll();
		}
		model.addAttribute("listBundles", listBundles);
		
		return "services/paper-bundle-list";
	}
	
	@GetMapping("/addPaperBundle")
	public ModelAndView addPaperBundle(HttpSession session) {
		ModelAndView mav = new ModelAndView("services/paper-bundle-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		PaperBundle bundle = new PaperBundle();
		bundle.setCreateBy(user.getUsername());
		bundle.setCreateDate(LocalDate.now());
		bundle.setUpdateBy(user.getUsername());
		bundle.setUpdateDate(LocalDate.now());
		bundle.setUserId(user);
		
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("LECTURER"))) {
			bundle.setPublisher(publisherService.get(0));
		}
		if((roleService.listByUser(user.getUserId())).contains(roleService.getByRole("AUTHOR")) || (roleService.listByUser(user.getUserId())).contains(roleService.getByRole("EDITOR"))) {
			bundle.setPublisher(publisherUserService.getByUserId(user.getUserId()).getPoId());
		}
		mav.addObject("bundle", bundle);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);
		
		//Get all publishers
		List<Publisher> listPublishers = publisherService.listAllActive();
		mav.addObject("listPublishers", listPublishers);
		 
		return mav;
	}
	
	@GetMapping("/showPaperBundleUpdate")
	public ModelAndView showPaperBundleUpdate(@RequestParam Long bundleId,HttpSession session) {
		
		ModelAndView mav = new ModelAndView("services/paper-bundle-form");
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		PaperBundle bundle = paperBundleService.get(bundleId);
		bundle.setUpdateBy(user.getUsername());
		bundle.setUpdateDate(LocalDate.now());
		mav.addObject("bundle", bundle);
		
		//Get all grades
		List<Grade> listGrades = gradeService.listAllActive();
		mav.addObject("gradeList", listGrades);
		
		//Get all publishers
		List<Publisher> listPublishers = publisherService.listAllActive();
		mav.addObject("listPublishers", listPublishers);
		
		List<LinkPaperBundle> linkedPaperList = linkPaperBundleService.listAllByBundleId(bundleId);
		mav.addObject("linkedPaperList", linkedPaperList);
		 
		return mav;
	}
	
	@PostMapping("/secure/savePaperBundle")
	public ResponseEntity<PaperBundle> savePaperBundle(@ModelAttribute PaperBundleDto bundleDto,HttpSession session) {
		User user = (User)session.getAttribute("user");
		PaperBundle bundle;
		if((paperBundleService.getCountBuId(bundleDto.getBundleId())) > 0) {
			bundle = paperBundleService.get(bundleDto.getBundleId());
			
			bundle.setName(bundleDto.getName());
			bundle.setDescription(bundleDto.getDescription());
			bundle.setGrade(gradeService.get(bundleDto.getGrade()));
			bundle.setPublisher(publisherService.get(bundleDto.getPublisher()));
			bundle.setUserId(userService.getById(bundleDto.getUser()));
			bundle.setAvailableForBuy(bundleDto.isAvailableForBuy());
			bundle.setUpdateBy(user.getUsername());
			bundle.setUpdateDate(LocalDate.now());
			
			if(!((bundleDto.getImageData()).equals(""))) {
				String fileName = bundleDto.getImageName() + "." + bundleDto.getExtension();
				byte[] decodedImg = Base64.getDecoder().decode(bundleDto.getImageData().getBytes(StandardCharsets.UTF_8));
				Path destinationFile = Paths.get(imageLocation + "/bundle", fileName);
				try{
					Files.write(destinationFile, decodedImg);
					bundle.setImage("/bundle/" + fileName);
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			paperBundleService.save(bundle);
		}else {
			bundle = new PaperBundle();
			
			bundle.setName(bundleDto.getName());
			bundle.setDescription(bundleDto.getDescription());
			bundle.setGrade(gradeService.get(bundleDto.getGrade()));
			bundle.setPublisher(publisherService.get(bundleDto.getPublisher()));
			bundle.setUserId(userService.getById(bundleDto.getUser()));
			bundle.setAvailableForBuy(bundleDto.isAvailableForBuy());
			bundle.setActive(false);
			bundle.setCreateBy(user.getUsername());
			bundle.setCreateDate(LocalDate.now());
			bundle.setUpdateBy(user.getUsername());
			bundle.setUpdateDate(LocalDate.now());
			
			if(!((bundleDto.getImageData()).equals(""))) {
				String fileName = bundleDto.getImageName() + "." + bundleDto.getExtension();
				byte[] decodedImg = Base64.getDecoder().decode(bundleDto.getImageData().getBytes(StandardCharsets.UTF_8));
				Path destinationFile = Paths.get(imageLocation + "/bundle", fileName);
				try{
					Files.write(destinationFile, decodedImg);
					bundle.setImage("/bundle/" + fileName);
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			paperBundleService.save(bundle);
		}
		return new ResponseEntity<>(bundle, HttpStatus.OK);
	}
	
	@GetMapping("/deletePaperBundle")
	public ResponseEntity<String> deletePaperBundle(@RequestParam Long bundleId) {
		
		List<LinkPaperBundle> listBundleLinks = linkPaperBundleService.listAllByBundleId(bundleId);
		for (LinkPaperBundle bundleLink : listBundleLinks) 
		{
			linkPaperBundleService.delete(bundleLink.getPbLinkId());
		}
		paperBundleService.delete(bundleId);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	@GetMapping("/paperBundleDeactive")
	public String paperBundleDeactive(@RequestParam Long bundleId,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		
		PaperBundle bundle = paperBundleService.get(bundleId);
		bundle.setActive(false);
		bundle.setUpdateBy(user.getUsername());
		bundle.setUpdateDate(LocalDate.now());
		
		paperBundleService.save(bundle);
		return "redirect:/listPaperBundle";
	}
	
	@GetMapping("/paperBundleActive")
	public String paperBundleActive(@RequestParam Long bundleId,HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		
		PaperBundle bundle = paperBundleService.get(bundleId);
		bundle.setActive(true);
		bundle.setUpdateBy(user.getUsername());
		bundle.setUpdateDate(LocalDate.now());
		
		paperBundleService.save(bundle);
		return "redirect:/listPaperBundle";
	}
	
	@GetMapping("/addBundlePaper")
	public ResponseEntity<String> addBundlePaper(@RequestParam Long paperId,@RequestParam Long bundleId,HttpSession session) {
		
		User user = (User)session.getAttribute("user");

		PaperBundle bundle = paperBundleService.get(bundleId);
		
		if((linkPaperBundleService.getCount(paperId,bundleId)) > 0) {
			return new ResponseEntity<>("Dupplicate",HttpStatus.NOT_FOUND);
		}else {
			LinkPaperBundle linkPaperBundle = new LinkPaperBundle();
			linkPaperBundle.setBundleId(bundle);
			linkPaperBundle.setPaperId(paperService.get(paperId));
			linkPaperBundle.setActive(true);
			linkPaperBundle.setCreateBy(user.getUsername());
			linkPaperBundle.setCreateDate(LocalDate.now());
			linkPaperBundle.setUpdateBy(user.getUsername());
			linkPaperBundle.setUpdateDate(LocalDate.now());
			linkPaperBundleService.save(linkPaperBundle);
			
			bundle.setPaperCount(bundle.getPaperCount() + 1);
			paperBundleService.save(bundle);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		}
	}
	
	@GetMapping("/removeBundlePaper")
	public ResponseEntity<String> addBundlePaper(@RequestParam Long pbLinkId,HttpSession session) {
		
		User user = (User)session.getAttribute("user");

		LinkPaperBundle linkPaperBundle = linkPaperBundleService.get(pbLinkId);
		linkPaperBundleService.delete(pbLinkId);
		
		PaperBundle bundle = linkPaperBundle.getBundleId();
		
		bundle.setPaperCount(bundle.getPaperCount() - 1);
		paperBundleService.save(bundle);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
		
	}
		
}
