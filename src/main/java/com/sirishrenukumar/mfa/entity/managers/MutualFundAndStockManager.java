package com.sirishrenukumar.mfa.entity.managers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.sirishrenukumar.mfa.entity.MutualFund;
import com.sirishrenukumar.mfa.entity.MutualFundAndStockAssociation;
import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.StockMetrics;
import com.sirishrenukumar.mfa.entity.constants.Category;
import com.sirishrenukumar.mfa.entity.constants.Rating;

@ManagedBean
public class MutualFundAndStockManager {
	
	private static final Comparator<Stock> STOCK_NET_ASSETS_ASCENDING_ORDER_COMPARATOR = new Comparator<Stock>() {
		@Override
		public int compare(Stock o1, Stock o2) {
			return Float.valueOf(o1.getNetAssetsInCrores()).compareTo(Float.valueOf(o2.getNetAssetsInCrores()));
		}
	};
	
	private static final Function<MutualFund, String> MUTUAL_FUND_ID_EXTRACTOR_FUNCTION = new Function<MutualFund,String>() {
		@Override
		public String apply(MutualFund input) {
			return Long.valueOf(input.getMutualfund_id()).toString();
		}
	};
	
	private static final Function<String,String> SINGLE_QUOTE_STRING_FUNCTION = new Function<String, String>() {
		@Override
		public String apply(String input) {
			return String.format("'%s'",input);
		}
	};
		
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
	public void associateMutualFundAndStock(long mutualFundCode, String stockName, String stockSector, StockMetrics stockMetrics) {
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
	
	@Transactional(readOnly = true)
	public List<MutualFund> getMutualFunds() {
		return getMutualFunds(null,null);
	}

	@Transactional(readOnly = true)
	public List<MutualFund> getMutualFunds(Set<Category> requiredCategories, Set<Rating> requiredRatings) {

		Set<String> fragments = Sets.newLinkedHashSet();
		if(requiredCategories!= null && !requiredCategories.isEmpty()) {
			fragments.add(String.format("category IN (%s)", Joiner.on(',').join(requiredCategories)));
		}
		if(requiredRatings!= null && !requiredRatings.isEmpty()) {
			fragments.add(String.format("rating IN (%s)", Joiner.on(',').join(requiredRatings)));
		}
		
		StringBuilder sql = new StringBuilder("SELECT mf FROM MutualFund mf");
		if(!fragments.isEmpty()) {
			sql.append(" WHERE ");
			
			Iterator<String> iterator = fragments.iterator();
			while(iterator.hasNext()) {
				sql.append(iterator.next());
				if(iterator.hasNext()) {
					sql.append(" AND ");
				}
			}
		}
		return em.createQuery(sql.toString(), MutualFund.class).getResultList();
	}
	
	@Transactional(readOnly = true)
	public List<Stock> getStocks() {
		return getStocks(null);
	}

	@Transactional(readOnly = true)
	public List<Stock> getStocks(Set<String> requiredSectors) {
		
		StringBuilder sql = new StringBuilder("SELECT s FROM Stock s");
		if(requiredSectors!= null && !requiredSectors.isEmpty()) {
			sql.append(String.format(" WHERE sector IN (%s)", Joiner.on(',').join(Iterables.transform(requiredSectors, SINGLE_QUOTE_STRING_FUNCTION))));
		}

		return em.createQuery(sql.toString(), Stock.class).getResultList();
	}

	
	@Transactional(readOnly = true)
	public List<Stock> getStocksOrderedByNetAssets() {
		return getStocksOrderedByNetAssets(null,null,null);
	}

	@Transactional(readOnly = true)
	public List<Stock> getStocksOrderedByNetAssets(Set<Category> requiredCategories, Set<Rating> requiredRatings, Set<String> requiredSectors) {
		
		List<MutualFund> mutualFunds = getMutualFunds(requiredCategories, requiredRatings);
		List<Stock> stocks = getStocks(requiredSectors);
		
		for(Stock stock : stocks) {
			String sql = String.format("SELECT SUM(mf.netAssetsInCrores * mfasa.assetPercentage/100) from MutualFund mf "
					+ "INNER JOIN MutualFundAndStockAssociation mfasa ON mf.mutualfund_id = mfasa.mutualfund_id "
					+ "WHERE mfasa.stock_id = %s AND mfasa.mutualfund_id IN (%s)", 
					stock.getStock_id(), 
					Joiner.on(',').join(Iterables.transform(mutualFunds, MUTUAL_FUND_ID_EXTRACTOR_FUNCTION )));
			stock.setNetAssetsInCrores(jdbcTemplate.queryForObject(sql, Float.class));
		}
		
		Collections.sort(stocks, Collections.reverseOrder(STOCK_NET_ASSETS_ASCENDING_ORDER_COMPARATOR));
		
		return stocks;
	}
	
	@Transactional(readOnly = true)
	public List<String> getStockSectors() {
		return jdbcTemplate.queryForList("SELECT DISTINCT(sector) FROM Stock ORDER BY sector", String.class); 
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
