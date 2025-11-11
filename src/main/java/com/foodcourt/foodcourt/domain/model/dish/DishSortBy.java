package com.foodcourt.foodcourt.domain.model.dish;

import lombok.Getter;

@Getter
public enum DishSortBy {
	
	NAME("name"),
	PRICE("price"),
	CATEGORY_NAME("categoryName");
	
	private final String value;
	
	DishSortBy (String value) {
		this.value = value;
	}
	
	public static DishSortBy of(String sortBy) {
		for (DishSortBy dishSortBy : DishSortBy.values()) {
			if (dishSortBy.name().equalsIgnoreCase(sortBy) || dishSortBy.value.equalsIgnoreCase(sortBy)) {
				return dishSortBy;
			}
		}
		return NAME;
	}
	
}
