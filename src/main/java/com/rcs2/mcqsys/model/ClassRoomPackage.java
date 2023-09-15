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
@Table(name = "classroom_package")
public class ClassRoomPackage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long packageId;
	
	private String description;
	private int duration;
	private int studentCount;
	private double price;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
}
