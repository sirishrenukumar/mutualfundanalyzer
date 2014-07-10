package com.sirishrenukumar.mfa.entity;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class Stock {
	
	private String name;
	private String sector;
	
	public Stock(String name, String sector) {
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
	
	public String getKey() {
		return getName();
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
}
