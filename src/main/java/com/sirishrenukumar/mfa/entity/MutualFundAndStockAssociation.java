package com.sirishrenukumar.mfa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MutualFundAndStockAssociation {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "mutualfund_id")
	private MutualFund mutualFund;
	
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	private float assetPercentage;
	
	MutualFundAndStockAssociation() {
	}

	public MutualFundAndStockAssociation(MutualFund mutualFund, Stock stock,
			float assetPercentage) {
		super();
		this.mutualFund = mutualFund;
		this.stock = stock;
		this.assetPercentage = assetPercentage;
	}

	public float getAssetPercentage() {
		return assetPercentage;
	}

}
