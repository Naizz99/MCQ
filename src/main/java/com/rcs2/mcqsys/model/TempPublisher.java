package com.rcs2.mcqsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "temp_publisher")
public class TempPublisher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tempId;
	
	@OneToOne
	@JoinColumn(name = "poId")
    private Publisher poId;
	
	private String note;
	private boolean rejected;
}
