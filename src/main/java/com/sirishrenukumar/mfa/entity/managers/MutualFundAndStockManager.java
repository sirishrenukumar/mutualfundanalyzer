package com.sirishrenukumar.mfa.entity.managers;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	public void associateStockWithMutualFund(String stockName, String stockSector, MutualFund mutualFund, StockMetrics stockMetrics) {
		
		Stock stock = getStock(stockName);
		if(stock == null) {
			stock = storeStock(stockName, stockSector);
		}
		
		updateMutualFund(mutualFund, stock, stockMetrics);
	}
	
	@Transactional(readOnly = true)
	public List<MutualFund> getMutualFunds() {
		return em.createQuery("SELECT mf FROM MutualFund mf", MutualFund.class).getResultList();
	}
		
	@Transactional(readOnly = true)
	public List<Stock> getStocks() {
		return em.createQuery("SELECT s FROM Stock s", Stock.class).getResultList();
	}
	
	public void update() {
		
		for(Stock stock : getStocks()) {
			
		}
	}
	
	private void updateMutualFund(MutualFund mutualFund, Stock stock, StockMetrics stockMetrics){
		MutualFund mutualFundToUpdate = em.createQuery(String.format("SELECT mf from MutualFund mf WHERE code = %s", mutualFund.getCode()), MutualFund.class).getSingleResult();
		
		MutualFundAndStockAssociation mutualFundAndStockAssociation = new MutualFundAndStockAssociation(mutualFundToUpdate, stock, stockMetrics.getAssetPercentage());
		mutualFundToUpdate.update(mutualFundAndStockAssociation);
		stock.update(mutualFundAndStockAssociation);
		
		em.persist(mutualFundToUpdate);
		em.persist(stock);
		em.persist(mutualFundAndStockAssociation);
	}
	
	private Stock getStock(String name) {
		name = name.replace('\'',' ');
		
		List<Stock> stocks = em.createQuery(String.format("SELECT s FROM Stock s WHERE name = '%s'",name), Stock.class).getResultList();

		if(!stocks.isEmpty()) {
			return stocks.get(0);
		}
		
		return null;
	}
	
	private Stock storeStock(String name, String sector) {
		Stock stock = new Stock(name, sector);
		em.persist(stock);
		return stock;
	}
}
