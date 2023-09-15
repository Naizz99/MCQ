package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import java.util.Comparator;

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
@Table(name = "student_paper_review_detail")
public class StudentPaperReviewDetail{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sprdId;
	
	@ManyToOne
	@JoinColumn(name = "paperID")
    private Paper paperID;
	
	@ManyToOne
	@JoinColumn(name = "questionId")
    private PaperQuestion questionId;
	
	@ManyToOne
	@JoinColumn(name = "moduleId")
    private Module moduleId;
	
	@ManyToOne
    private PaperAnswer correctAnswer;
	
	@ManyToOne
    private PaperAnswer givenAnswer;
	
	@ManyToOne
	@JoinColumn(name = "studentId")
    private Student studentId;
	
	@ManyToOne
	@JoinColumn(name = "reviewId")
    private StudentPaperReview reviewId;
	
	@ManyToOne
	@JoinColumn(name = "reviewerId")
    private User reviewerId;
	
	private String reviewerComment;
	
	private LocalDate date;
	private Integer result;
	private boolean status;
	private boolean active;
			
}


