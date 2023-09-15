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
@Table(name = "classroom_paper_review")
public class ClassRoomPaperReview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cprId;
	
	@ManyToOne
	@JoinColumn(name = "paperID")
    private Paper paperID;
	
    private String studentEmail;
	
	@ManyToOne
	@JoinColumn(name = "classRoomId")
    private ClassRoom classRoomId;
	
	@ManyToOne
	@JoinColumn(name = "studentId")
    private Student studentId;
	
	private LocalDate seatedDate;
	private Integer result;
	private String aneleticReport;
	private String weakModules;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}
