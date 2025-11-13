package com.foodcourt.foodcourt.domain.model.enums;

public enum SortDirection {
	ASC, DESC;
	
	public static SortDirection of(String value) {
		if (SortDirection.DESC.name().equalsIgnoreCase(value)) {
			return SortDirection.DESC;
		}
		return SortDirection.ASC;
	}
}
