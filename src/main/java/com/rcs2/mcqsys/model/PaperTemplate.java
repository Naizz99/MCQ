package com.rcs2.mcqsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "paper_template")
public class PaperTemplate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long templateId;
	
    private int answerPerRow;
	
	private String name;
	
	private String url;
	
	private boolean isDefault;
}
