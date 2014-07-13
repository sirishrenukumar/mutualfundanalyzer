package com.sirishrenukumar.mfa.entity.managers;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.sirishrenukumar.mfa.entity.MutualFund;
import com.sirishrenukumar.mfa.entity.MutualFundAndStockAssociation;
import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.StockMetrics;
import com.sirishrenukumar.mfa.entity.constants.Category;
import com.sirishrenukumar.mfa.entity.constants.Rating;

@ManagedBean
public class MutualFundAndStockManager {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private JdbcTemplate jdbcTemplate;
	

	@Transactional
	public void storeMutualFund(String codeString, String name, String ratingString, String categoryString, float netAssetsInCrores) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(codeString));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
		Preconditions.checkNotNull(ratingString);
		Preconditions.checkNotNull(categoryString);

		long code = Long.parseLong(codeString);
		name = name.trim();
		Rating rating = Rating.fromString(ratingString);
		Category category = Category.fromString(categoryString);

		MutualFund mutualFund = new MutualFund(code,name,category,rating, netAssetsInCrores);
		em.persist(mutualFund);
	}
	
	@Transactional
	public void associateStockWithMutualFund(String stockName, String stockSector, long mutualFundCode, StockMetrics stockMetrics) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(stockName));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(stockName));
		
		/*
		 * Replace the single quote in the stock name with space since that conflicts with the SQL statement
		 */
		stockName = stockName.replace('\'',' ');
		
		Stock stock = getStock(stockName);
		if(stock == null) {
			stock = storeStock(stockName, stockSector);
		}
		
		associateStockWithMutualFund(mutualFundCode, stock, stockMetrics);
	}
	
	@Transactional
	public void updateStockNetAssets() {
		
		for(Stock stock : getStocks()) {
			
			String sql = String.format("SELECT SUM(mf.netAssetsInCrores * mfasa.assetPercentage/100) from MutualFund mf INNER JOIN MutualFundAndStockAssociation mfasa ON mf.mutualfund_id = mfasa.mutualfund_id WHERE mfasa.stock_id = %s", stock.getStock_id());
			
			stock.setNetAssetsInCrores(jdbcTemplate.queryForObject(sql, Float.class));
			em.persist(stock);
		}
	}
	
	@Transactional(readOnly = true)
	public List<MutualFund> getMutualFunds() {
		return em.createQuery("SELECT mf FROM MutualFund mf", MutualFund.class).getResultList();
	}
		
	@Transactional(readOnly = true)
	public List<Stock> getStocks() {
		return em.createQuery("SELECT s FROM Stock s", Stock.class).getResultList();
	}

	@Transactional(readOnly = true)
	public List<Stock> getStocksOrderedByNetAssets() {
		return em.createQuery("SELECT s FROM Stock s ORDER BY netAssetsInCrores DESC", Stock.class).getResultList();
	}

	@Transactional(readOnly = true)
	public List<Stock> getStocksOrderedByName() {
		return em.createQuery("SELECT s FROM Stock s ORDER BY name", Stock.class).getResultList();
	}
	
	@Transactional
	private void associateStockWithMutualFund(long mutualFundCode, Stock stock, StockMetrics stockMetrics){
		MutualFund mutualFund = em.createQuery(String.format("SELECT mf from MutualFund mf WHERE code = %s", mutualFundCode), MutualFund.class).getSingleResult();
		
		MutualFundAndStockAssociation mutualFundAndStockAssociation = new MutualFundAndStockAssociation(mutualFund, stock, stockMetrics.getAssetPercentage());
		mutualFund.update(mutualFundAndStockAssociation);
		stock.update(mutualFundAndStockAssociation);
		
		em.persist(mutualFund);
		em.persist(stock);
		em.persist(mutualFundAndStockAssociation);
	}
	
	@Transactional(readOnly = true)
	private Stock getStock(String name) {
		List<Stock> stocks = em.createQuery(String.format("SELECT s FROM Stock s WHERE name = '%s'",name), Stock.class).getResultList();

		if(!stocks.isEmpty()) {
			return stocks.get(0);
		}
		
		return null;
	}
	
	@Transactional
	private Stock storeStock(String name, String sector) {
		Stock stock = new Stock(name, sector);
		em.persist(stock);
		return stock;
	}
}
