package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Entity
@Table(name = "banner")
public class Banner {
	
	public enum ContentType {Image,Video,Customize;}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bannerId;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User userId;
	
	@ManyToOne
	@JoinColumn(name = "publisherId")
	private Publisher publisherId;
	
	@ManyToOne
	@JoinColumn(name = "gradeId")
	private Grade grade;
	
	private ContentType contentType;
	private int width;
	private int height;
	private String content;
	
	//Temporary
	private String header;
	private String subHeader;
	private String image;
	private boolean active;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	
	//view User Roles
	//private String roles;
}





