package com.sirishrenukumar.mfa.entity;

public class StockMetrics {
	
	private float assetPercentage;

	public StockMetrics(float assetPercentage) {
		super();
		this.assetPercentage = assetPercentage;
	}

	public float getAssetPercentage() {
		return assetPercentage;
	}

	@Override
	public String toString() {
		return "StockMetrics [assetPercentage=" + assetPercentage + "]";
	}
}
