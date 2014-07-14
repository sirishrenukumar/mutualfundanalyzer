package com.sirishrenukumar.mfa.entity.constants;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

public enum Rating {
	
	UNRATED(0),
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5);

	private int rating;
	
	private Rating(int rating) {
		this.rating = rating;
	}

	public static Rating fromString(String ratingString) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(ratingString));
		ratingString = ratingString.trim();
		
		int ratingNumber = Integer.parseInt(ratingString);
		
		for(Rating rating: Rating.values()) {
			if(rating.rating == ratingNumber) {
				return rating;
			}
		}
		
		return Rating.UNRATED;
	}
	
	public static final Set<Rating> ALL_RATINGS = ImmutableSet.of(Rating.ONE, Rating.TWO, Rating.THREE, Rating.FOUR, Rating.FIVE);
}
