package com.sirishrenukumar.mfa.core;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;

@ManagedBean
public class StockAssetsCalculator {

	@Inject
	private MutualFundAndStockManager mutualFundAndStockManager;
	
	@Inject
	private JdbcTemplate jdbcTemplate;
	
	StockAssetsCalculator() {
	}
	
	public void calculateStockAssets() {
		
		for(Stock stock : mutualFundAndStockManager.getStocks()) {
			
			String sql = "SELECT SUM(mf.netAssetsInCrores * mfasa.assetPercentage) from MutualFund mf INNER JOIN MutualFundAndStockAssociation msasa ON mf.mutualfund_id = mfasa.mutualfund_id";
			
			jdbcTemplate.queryForObject(sql, Float.class);
			
		}
	}
}
