package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
@Table(name = "paper_question")
public class PaperQuestion {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pqId;
	
	private int questionId;
	
	@ManyToOne
	@JoinColumn(name = "paperID")
    private Paper paperID;
	
	@ManyToOne
	@JoinColumn(name = "moduleId")
	private Module moduleId;
	
	@Length(max = 1000)
	private String question;
	
	private String image;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
}

