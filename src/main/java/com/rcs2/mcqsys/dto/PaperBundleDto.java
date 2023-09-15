package com.rcs2.mcqsys.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PaperBundleDto {
	private Long bundleId;
	private String name;
	private String description;
    private long grade;
    private long Publisher;
    private long user;
	private boolean availableForBuy;
	private boolean active;
	private String createBy;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	private String imageData;
	private String imageName;
	private String extension;
}

