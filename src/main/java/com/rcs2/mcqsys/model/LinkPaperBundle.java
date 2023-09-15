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
@Table(name = "link_paper_bundle")
public class LinkPaperBundle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pbLinkId;
	
	@ManyToOne
	@JoinColumn(name = "paperId")
    private Paper paperId;
			
	@ManyToOne
	@JoinColumn(name = "bundleId")
    private PaperBundle bundleId;
	
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
}
