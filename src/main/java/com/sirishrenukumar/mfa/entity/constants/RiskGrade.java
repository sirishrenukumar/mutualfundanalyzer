package com.sirishrenukumar.mfa.entity.constants;

import com.google.common.base.Preconditions;

public enum RiskGrade {
	
	LOW("Low", "Low"),
	BELOW_AVERAGE("Below Avg.", "Below Average"),
	AVERAGE("Avg.", "Average"),
	ABOVE_AVERAGE("Above Avg.", "Above Average"),
	HIGH("High", "High"),
	UNKNOWN("Unknown", "Unknown");

	private String shortName;
	private String longName;
	
	private RiskGrade(String shortName, String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}
	
	public static RiskGrade fromString(String riskGradeString) {
		Preconditions.checkNotNull(riskGradeString);
		riskGradeString = riskGradeString.trim();
		
		for(RiskGrade riskGrade : RiskGrade.values()) {
			if(riskGrade.shortName.equalsIgnoreCase(riskGradeString) || riskGrade.longName.equalsIgnoreCase(riskGradeString)) {
				return riskGrade;
			}
		}
		return UNKNOWN;
	}
}
