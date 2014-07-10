package com.sirishrenukumar.mfa.entity.managers;

import java.util.List;

import javax.annotation.ManagedBean;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sirishrenukumar.mfa.entity.MutualFund;
import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.StockMetrics;
import com.sirishrenukumar.mfa.entity.constants.Category;
import com.sirishrenukumar.mfa.entity.constants.Rating;

@ManagedBean
public class MutualFundManager {
	
	private List<MutualFund> mutualFunds;
	
	private MutualFundManager() {
		mutualFunds = Lists.newArrayList();
	}
	
	public void storeMutualFund(String idString, String name, String ratingString, String categoryString, float netAssetsInCrores) {
		MutualFund mutualFund = createMutualFund(idString, name, ratingString, categoryString, netAssetsInCrores);
		mutualFunds.add(mutualFund);
	}
	
	public void updateMutualFund(final MutualFund mutualFund, Stock stock, StockMetrics stockMetrics){
		
		MutualFund mutualFundToUpdate = (MutualFund) CollectionUtils.find(mutualFunds,new org.apache.commons.collections.Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				return mutualFund.getCode() == ((MutualFund) arg0).getCode();
			}
		});
		
		mutualFundToUpdate.addStock(stock, stockMetrics);
		
	}
	
	public List<MutualFund> getMutualFunds() {
		return mutualFunds;
	}
	
	private MutualFund  createMutualFund(String idString, String name, String ratingString, String categoryString, float netAssetsInCrores) {
		
		Preconditions.checkNotNull(idString);
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(ratingString);
		Preconditions.checkNotNull(categoryString);

		long id = Long.parseLong(idString);
		name = name.trim();
		Rating rating = Rating.fromString(ratingString);
		Category category = Category.fromString(categoryString);
		
		return new MutualFund(id,name,category,rating, netAssetsInCrores);
	}	
}
