package com.sirishrenukumar.mfa.core;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;

@ManagedBean
public class StockAssetsCalculator {

	@Inject
	private MutualFundAndStockManager mutualFundAndStockManager;
	
	@Inject
	private JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
	private EntityManager em;	

	StockAssetsCalculator() {
	}
	
	@Transactional
	public void calculateStockAssets() {
		
		for(Stock stock : mutualFundAndStockManager.getStocks()) {
			
			String sql = String.format("SELECT SUM(mf.netAssetsInCrores * mfasa.assetPercentage) from MutualFund mf INNER JOIN MutualFundAndStockAssociation mfasa ON mf.mutualfund_id = mfasa.mutualfund_id WHERE mfasa.stock_id = %s", stock.getStock_id());
			
			stock.setNetAssetsInCrores(jdbcTemplate.queryForObject(sql, Float.class));
			em.persist(stock);
		}
	}
}
