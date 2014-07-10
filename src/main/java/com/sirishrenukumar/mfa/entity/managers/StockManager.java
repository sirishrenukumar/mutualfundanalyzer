package com.sirishrenukumar.mfa.entity.managers;

import java.util.Collection;
import java.util.Set;

import javax.annotation.ManagedBean;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.sirishrenukumar.mfa.entity.Stock;

@ManagedBean
public class StockManager {
	
	private Set<Stock> stocks;
	
	private StockManager(){
		stocks = Sets.newHashSet();
	}
	
	public Stock storeStock(final String name, String sector) {
		Stock stock = getStock(name);
		if(stock == null) {
			stock = new Stock(name, sector);
			stocks.add(stock);
		}
		
		return stock;
	}
	
	public Stock getStock(final String name){
		
		Collection<Stock> filteredStocks = Collections2.filter(stocks, new Predicate<Stock>() {

			@Override
			public boolean apply(Stock stock) {
				return stock.getName().equalsIgnoreCase(name);
			}
		});
		
		if(filteredStocks.size() == 1) {
			return filteredStocks.iterator().next();
		} else {
			return null;
		}
		
	}
	public Set<Stock> getStocks() {
		return stocks;
	}
}
