package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "paper_detail_answer")
public class PaperDetailAnswer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pdaId;
	
	@ManyToOne
	@JoinColumn(name = "paperID")
    private Paper paperID;
	
	@OneToOne
	@JoinColumns({
		@JoinColumn(name = "pqId" , referencedColumnName = "pqId"),
		@JoinColumn(name = "questionId" , referencedColumnName = "questionId")
	})
    private PaperQuestion questionId;
	
	private String answer;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}
