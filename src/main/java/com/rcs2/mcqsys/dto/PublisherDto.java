package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class PublisherDto {
	private Long poId;
	private String name;
	private String email;
	private String mobile;
	private String address;
	private int authors;
	private int editors;
	private String date;
	private String active;
	private int paperCount;
}
