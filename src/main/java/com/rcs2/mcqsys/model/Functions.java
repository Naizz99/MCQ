package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "functions")
public class Functions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long functionId;
	
	private String functionName;
	private String functionAction;
	private String functionControl;
	private boolean isParent;
	private int parentId;
	private boolean linked;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
}
