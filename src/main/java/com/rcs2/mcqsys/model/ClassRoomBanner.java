package com.rcs2.mcqsys.model;

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
@Table(name = "classroom_banner")
public class ClassRoomBanner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cbId;
	
	@ManyToOne
	@JoinColumn(name = "classroomId")
    private ClassRoom classroomId;
	
	@ManyToOne
	@JoinColumn(name = "bannerId")
    private Banner bannerId;
}
