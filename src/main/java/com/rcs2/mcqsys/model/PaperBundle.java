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
@Table(name = "paper_bundle")
public class PaperBundle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bundleId;
	
	private String name;
	
	private String image;
	
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "grade")
    private Grade grade;
	
	@ManyToOne
	@JoinColumn(name = "publisher")
    private Publisher Publisher;
	
	@ManyToOne
	@JoinColumn(name = "userId")
    private User userId;
	
	private boolean availableForBuy;
	private int paperCount;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}

