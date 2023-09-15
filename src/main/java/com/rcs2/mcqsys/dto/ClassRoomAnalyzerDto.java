package com.rcs2.mcqsys.dto;

import lombok.Data;

@Data 
public class ClassRoomAnalyzerDto {
	
	/**Result Summery*/
	//0-20 marks
	private int countCategory1;
	//21-35 marks
	private int countCategory2;
	//36-55 marks
	private int countCategory3;
	//56-75 marks
	private int countCategory4;
	//76-100 marks
	private int countCategory5;
	
	/**Monthly paper count*/
	//January
	private int completedPaperCount1;
	//February
	private int completedPaperCount2;
	//March
	private int completedPaperCount3;
	//April
	private int completedPaperCount4;
	//May
	private int completedPaperCount5;
	//June
	private int completedPaperCount6;
	//July
	private int completedPaperCount7;
	//August
	private int completedPaperCount8;
	//September
	private int completedPaperCount9;
	//October
	private int completedPaperCount10;
	//November
	private int completedPaperCount11;
	//December
	private int completedPaperCount12;
	
	/**Monthly student registration*/
	//January
	private int studentCount1;
	//February
	private int studentCount2;
	//March
	private int studentCount3;
	//April
	private int studentCount4;
	//May
	private int studentCount5;
	//June
	private int studentCount6;
	//July
	private int studentCount7;
	//August
	private int studentCount8;
	//September
	private int studentCount9;
	//October
	private int studentCount10;
	//November
	private int studentCount11;
	//December
	private int studentCount12;
}
