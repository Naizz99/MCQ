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
@Table(name = "publisher")
public class Publisher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long poId;
	
	private String name;
	private String description;
	private String email;
	private String mobile;
	private String address;
	private String image;
	private String coverImage;
	private boolean approved;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
	//CSS Variables
	private String bodyBgColor;
	private String bodyFontFamily;
	private String fontColor;
	private String cardBgColor;
	private String cardSideColor;
	private String cardSelectedColor;
}
