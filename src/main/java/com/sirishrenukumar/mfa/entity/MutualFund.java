package com.sirishrenukumar.mfa.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.common.collect.Sets;
import com.sirishrenukumar.mfa.entity.constants.Category;
import com.sirishrenukumar.mfa.entity.constants.Rating;

@Entity
public class MutualFund {
	
	@Id
	@GeneratedValue
	private long mutualfund_id;
	
	private long code;
	
	private String name;
	
	private Category category;
	
	private Rating rating;
	
	private float netAssetsInCrores;
	
	@OneToMany(mappedBy = "mutualFund")
	private Set<MutualFundAndStockAssociation> mutualFundAndStockAssociations;

	MutualFund() {
		mutualFundAndStockAssociations = Sets.newHashSet();
	}

	public MutualFund(long code, String name, Category category, Rating rating, float netAssetsInCrores) {
		this();
		this.code = code;
		this.name = name;
		this.category = category;
		this.rating = rating;
		this.netAssetsInCrores = netAssetsInCrores;
	}

	public long getMutualfund_id() {
		return mutualfund_id;
	}

	public long getCode() {
		return code;
	}
	
	public void update(MutualFundAndStockAssociation mutualFundAndStockAssociation) {
		mutualFundAndStockAssociations.add(mutualFundAndStockAssociation);
	}

	@Override
	public String toString() {
		return "MutualFund [code=" + code + ", name=" + name + ", category="
				+ category + ", rating=" + rating + ", netAssetsInCrores="
				+ netAssetsInCrores + "]";
	}
}
