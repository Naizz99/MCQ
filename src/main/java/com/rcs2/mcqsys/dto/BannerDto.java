package com.rcs2.mcqsys.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BannerDto {

	private Long bannerId;
	private Long userId;
	private Long publisherId;
	private String contentType;
	private int width;
	private int height;
	private String content;
	private String extension;
	private String serial;
	private String header;
	private String subHeader;
	private String image;
	private boolean active;
	private LocalDate createDate;
	private String updateBy;
	private LocalDate updateDate;
	private String roles;
	private Long grade;
}

