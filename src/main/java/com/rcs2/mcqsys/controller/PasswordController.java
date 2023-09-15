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

import com.rcs2.mcqsys.model.PasswordPolicy;
import com.rcs2.mcqsys.model.User;
import com.rcs2.mcqsys.service.PasswordPolicyService;

@Controller
public class PasswordController {	
	//User Function Management

	@Autowired
	private PasswordPolicyService passwordPolicyService;
	
	@RequestMapping("/listPasswordPolicies")
	public String listPasswordPolicies(Model model,HttpSession session) {
		
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
	    model.addAttribute("listPolicies", listPolicies);
	    
	    User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
				
	    return "user/password-policies-list";
	}
	
	@GetMapping("/showPolicyUpdate")
	public ModelAndView showPolicyUpdate(@RequestParam Long policyId,HttpSession session) {		
		
		ModelAndView mav = new ModelAndView("user/password-policies-form");
		PasswordPolicy policy = passwordPolicyService.get(policyId);
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		
		policy.setUpdateBy(user.getUsername());
		policy.setUpdateDate(LocalDate.now());
		mav.addObject("policy", policy);
		
		return mav;
	}
	
	@GetMapping("/addPolicy")
	public ModelAndView addPolicy(HttpSession session) {
		
		ModelAndView mav = new ModelAndView("user/password-policies-form");
		PasswordPolicy policy = new PasswordPolicy();
		
		User user = (User)session.getAttribute("user");
		mav.addObject("user", user);
		
		policy.setCreateBy(user.getUsername());
		policy.setCreateDate(LocalDate.now());
		policy.setUpdateBy(user.getUsername());
		policy.setUpdateDate(LocalDate.now());
		mav.addObject("policy", policy);
		
		return mav;
	}
	
	@PostMapping("/secure/savePolicy")
	public ResponseEntity<String> savePolicy(@ModelAttribute PasswordPolicy policy) {
		
		policy.setUpdateDate(LocalDate.now());
		
		passwordPolicyService.save(policy);
		
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x ;
	}
	
	@GetMapping("/deletePolicy")
	public ResponseEntity<String> deletePolicy(@RequestParam Long policyId) {
		passwordPolicyService.delete(policyId);
		
		ResponseEntity<String> x = new ResponseEntity<>("success", HttpStatus.OK);
		return x;
	}
		
	@GetMapping("/policyDeactive")
	public String policyDeactive(@RequestParam Long policyId) {
		PasswordPolicy policy = passwordPolicyService.get(policyId);
		policy.setActive(false);
		passwordPolicyService.save(policy);
		return "redirect:/listPasswordPolicies";
	}
	
	@GetMapping("/policyActive")
	public String policyActive(@RequestParam Long policyId) {
		PasswordPolicy policy = passwordPolicyService.get(policyId);
		policy.setActive(true);
		passwordPolicyService.save(policy);
		return "redirect:/listPasswordPolicies";
	}
	
	@PostMapping("/secure/getPolicies")
	public ResponseEntity<List<PasswordPolicy>> getPolicies() {
		List<PasswordPolicy> listPolicies = passwordPolicyService.listAll();
		return new ResponseEntity<>(listPolicies, HttpStatus.OK);
	}

}
