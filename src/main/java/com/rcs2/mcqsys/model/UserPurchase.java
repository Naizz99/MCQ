package com.rcs2.mcqsys.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user_purchase")
public class UserPurchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userPaperId;
	
	@OneToOne
	@JoinColumn(name = "userId")
	private User userId;
	
	@ManyToOne
	@JoinColumn(name = "paperId")
    private Paper paperId;
	
	@ManyToOne
	@JoinColumn(name = "bundleId")
    private PaperBundle bundleId;
	
	@ManyToOne
	@JoinColumn(name = "paperPackageID")
    private PaperPackage paperPackageId;
	
	private String type;
	private int availableDates;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isUse;
	private boolean active;
	private boolean expired;
}
