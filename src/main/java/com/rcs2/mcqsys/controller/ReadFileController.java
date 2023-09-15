package com.rcs2.mcqsys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rcs2.mcqsys.dto.FileDto;

@RestController
public class ReadFileController {

	@RequestMapping("/reader")
	public ModelAndView reader(){
		return new ModelAndView("load");
	}
	
	@PostMapping("/secure/readWordFile")
	@ResponseBody
	public Map<String,Object> readWordFile(@ModelAttribute FileDto file) {
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		String suffixName = file.getOriginalFilename();
		
		try {
			
			File fileData = new File("C:\\\\Users\\\\R & D\\\\Downloads\\\\Scratch Programming Quiz.docx");
			File convFile = new File(file.getOriginalFilename());
			convFile.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
			  
			FileInputStream fs = new FileInputStream(convFile);
			
			if(suffixName.equalsIgnoreCase(".doc")){
				StringBuilder result2 = new StringBuilder();
				result.put("content", result2.toString());
			}else{
				XWPFDocument doc = new XWPFDocument(fs);
				XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
				String text = extractor.getText();
				extractor.close();
				fs.close();
				result.put("content", text);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
}
