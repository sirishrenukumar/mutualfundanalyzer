package com.sirishrenukumar.mfa.entity.constants;

import com.google.common.base.Preconditions;

public enum ReturnGrade {
	
	LOW("Low", "Low"),
	BELOW_AVERAGE("Below Avg.", "Below Average"),
	AVERAGE("Avg.", "Average"),
	ABOVE_AVERAGE("Above Avg.", "Above Average"),
	HIGH("High", "High"),
	UNKNOWN("Unknown", "Unknown");

	private String shortName;
	private String longName;
	
	private ReturnGrade(String shortName, String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}
	public static ReturnGrade fromString(String returnGradeString) {
		Preconditions.checkNotNull(returnGradeString);
		returnGradeString = returnGradeString.trim();
		
		for(ReturnGrade returnGrade : ReturnGrade.values()) {
			if(returnGrade.shortName.equalsIgnoreCase(returnGradeString) || returnGrade.longName.equalsIgnoreCase(returnGradeString)) {
				return returnGrade;
			}
		}
		return UNKNOWN;
	}
}
