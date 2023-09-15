package com.rcs2.mcqsys.dto;

import com.rcs2.mcqsys.model.Module;
import com.rcs2.mcqsys.model.Paper;

import lombok.Data;

@Data
public class PaperViewDto {
	
	private long pqId;
	
	private int questionId;
	
    private Paper paperID;
	
    private Module moduleId;
	
	private String question;
	
	private String image;
	
	//private String base64Img;
	
}
