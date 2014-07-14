package com.sirishrenukumar.mfa.entity.constants;

import java.util.Arrays;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public enum Category {
	
	LARGE_CAP("EQ-LC", "Equity: Large Cap"),
	LARGE_AND_MID_CAP("EQ-LC&MC","Equity: Large & Mid Cap"),
	MID_AND_SMALL_CAP("EQ-M&SC", "Equity: Mid & Small Cap"),
	MULTI_CAP("EQ-MLC", "Equity: Multi Cap"),
	TAX_PLANNING("EQ-TP", "Equity: Tax Planning"),
	INFRASTRUCTURE("EQ-INFRA", "Equity: Infrastructure"),
	BANKING("EQ-BANK", "Equity: Banking"),
	OTHERS("EQ-OTHER", "Equity: Others")
	;
	
	private String shortName;
	private String longName;
	private Category(String shortName, String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}
	
	public static Category fromString(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
		name = name.trim();
		
		for(Category category : Category.values()) {
			if(category.longName.equalsIgnoreCase(name) || category.shortName.equalsIgnoreCase(name)) {
				return category;
			}
		}
		return Category.OTHERS;
	}
	
	public static final Set<Category> ALL_CATEGORIES = Sets.newEnumSet(Arrays.asList(Category.values()), Category.class);
}
