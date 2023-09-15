package com.rcs2.mcqsys.model;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "classroom")
public class ClassRoom {
	public enum ClassroomType {PUBLIC,PRIVATE}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long classroomId;

	@ManyToOne
	@JoinColumn(name = "lecturerId")
	private Lecturer lecturerId;
	
	@ManyToOne
	@JoinColumn(name = "grade")
	private Grade grade;
	
	@ManyToOne
	@JoinColumn(name = "subjectId")
	private Subject subjectId;
	
	@ManyToOne
	@JoinColumn(name = "packageId")
	private ClassRoomPackage packageId;
	
	private String loginId;
	private String password;
	private ClassroomType type;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	private boolean status;
	private String name;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	private int studentCount;
	private int availableDates;
	private boolean expired;
	
	//CSS Variable
	private String bodyBgColor;
	private String bodyFontFamily;
	private String fontColor;
	private String cardBgColor;
	private String cardSideColor;
	private String cardSelectedColor;
}




