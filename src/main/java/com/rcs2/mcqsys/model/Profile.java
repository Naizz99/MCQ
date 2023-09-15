package com.rcs2.mcqsys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "profile")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;
	
	@ManyToOne
	@JoinColumn(name = "user")
	private User user;
	
	private String profilePic; 
	private String coverImage; 
	
	//CSS Variables
	private String bodyBgColor;
	private String bodyFontFamily;
	private String sideBgColor;
	private String sideMenuTextColor;
	private String sideSubMenuTextColor;
	private String fontColor;
	private String pageTitle;
	private String cardBgColor;
	private String cardSideColor1;
	private String cardSelectedColor;
}
