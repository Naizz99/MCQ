package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "paper")
public class Paper{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paperId;
	
	@ManyToOne
	@JoinColumn(name = "subjectId")
    private Subject subjectId;
		
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "grade")
    private Grade grade;
	
	private int time;
	
	@ManyToOne
	@JoinColumn(name = "publisher")
    private Publisher Publisher;

	private int numberOfQuestion;
	private int answersPerQuestion;
	
	@ManyToOne
	@JoinColumn(name = "template_id")
    private PaperTemplate templateId;
	
	private boolean availableForBuy;
	
	private boolean active;
	//private String status;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}
