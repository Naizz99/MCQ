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
@Table(name = "paper_answer")
public class PaperAnswer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paId;
	
	@ManyToOne
	@JoinColumn(name = "paperID")
    private Paper paperID;
	
	
	@ManyToOne
	//@JoinColumns({@JoinColumn(name = "pqId" , referencedColumnName = "pqId"),@JoinColumn(name = "questionId" , referencedColumnName = "questionId")	})
	@JoinColumn(name = "pqId")
    private PaperQuestion pqId;
	
	
	private String answer;
	private String image;
	private boolean answer_status;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}
