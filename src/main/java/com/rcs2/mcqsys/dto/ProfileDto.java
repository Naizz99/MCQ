package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data
public class ProfileDto {
	private Long profileId;
	private Long publisherId;
	private String bodyBgColor;
	private String bodyFontFamily;
	private String sideBgColor;
	private String sideMenuTextColor;
	private String sideMenuBgColor;
	private String sideSubMenuTextColor;
	private String sideSubMenuBgColor;
	private String fontColor;
	private String pageTitle;
	private String cardBgColor;
	private String cardSideColor1;
	private String cardSelectedColor;
	private String type;
}
