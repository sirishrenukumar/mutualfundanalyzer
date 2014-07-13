package com.sirishrenukumar.mfa.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

@Entity
public class Stock {
	
	@Id
	@GeneratedValue
	private long stock_id;
	
	private String name;
	
	private String sector;
	
	@OneToMany(mappedBy = "stock")
	private Set<MutualFundAndStockAssociation> mutualFundAndStockAssociations;
	
	private float netAssetsInCrores;

	Stock() {
		mutualFundAndStockAssociations = Sets.newHashSet();
	}
	
	public Stock(String name, String sector) {
		this();
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sector));
		this.name = name.trim();
		this.sector = sector.trim();
	}

	public String getName() {
		return name;
	}

	public String getSector() {
		return sector;
	}
	
	public void update(MutualFundAndStockAssociation mutualFundAndStockAssociation) {
		mutualFundAndStockAssociations.add(mutualFundAndStockAssociation);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stock other = (Stock) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Stock [name=" + name + ", sector=" + sector + "]";
	}

	public float getNetAssetsInCrores() {
		return netAssetsInCrores;
	}

	public void setNetAssetsInCrores(float netAssetsInCrores) {
		this.netAssetsInCrores = netAssetsInCrores;
	}

}
