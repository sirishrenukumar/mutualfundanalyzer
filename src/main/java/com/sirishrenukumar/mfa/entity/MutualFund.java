package com.sirishrenukumar.mfa.entity;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.sirishrenukumar.mfa.entity.constants.Category;
import com.sirishrenukumar.mfa.entity.constants.Rating;
import com.sirishrenukumar.mfa.entity.constants.ReturnGrade;
import com.sirishrenukumar.mfa.entity.constants.RiskGrade;

public class MutualFund {
	
	private long code;
	private String name;
	private Category category;
	private RiskGrade riskGrade;
	private ReturnGrade returnGrade;
	private Rating rating;
	private Map<Stock,StockMetrics> portfolio;
	private float netAssetsInCrores;
	
	public MutualFund(long code, String name, Category category, Rating rating, float netAssetsInCrores) {
		this.code = code;
		this.name = name;
		this.category = category;
		this.rating = rating;
		portfolio = Maps.newHashMap();
		this.netAssetsInCrores = netAssetsInCrores;
	}
	
	public long getCode() {
		return code;
	}


	@Override
	public String toString() {
		return "MutualFund [code=" + code + ", name=" + name + ", category="
				+ category + ", riskGrade=" + riskGrade + ", returnGrade="
				+ returnGrade + ", rating=" + rating + ", portfolio="
				+ portfolio + ", netAssetsInCrores=" + netAssetsInCrores + "]";
	}

	public void addStock(Stock stock, StockMetrics stockMetrics) {
		portfolio.put(stock, stockMetrics);
	}
	
	public Set<Stock> getStocks() {
		return portfolio.keySet();
	}
}
