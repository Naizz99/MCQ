package com.rcs2.mcqsys.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
//	@GetMapping("/loginMessage")
//	public ResponseEntity<String> serialExtract(@RequestParam String code) {
//		
//		String[] arrOfStr = serial.split("AccNo:  ", 5);
//  
//		String temp = arrOfStr[1].substring(0, arrOfStr[1].indexOf(' '));
//		serial = temp.replace("/", "");
//		
//		return new ResponseEntity<>(temp, HttpStatus.OK);
//	}
}
