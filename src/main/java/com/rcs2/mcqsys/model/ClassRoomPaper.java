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
@Table(name = "classroom_paper")
public class ClassRoomPaper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long classRoomPaperId;
	
	@ManyToOne
	@JoinColumn(name = "classRoomId")
    private ClassRoom classRoomId;
	
	@ManyToOne
	@JoinColumn(name = "paperID")
    private Paper paperID;
	
	@ManyToOne
	@JoinColumn(name = "bundleId")
    private PaperBundle bundleId;
	
	private boolean active;
	private boolean status;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
}
