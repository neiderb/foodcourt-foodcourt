package com.foodcourt.foodcourt.domain.model.restaurant;

import lombok.Getter;

@Getter
public enum RestaurantSortBy {
	NAME("name"),
	URL_LOGO("urlLogo");
	
	private final String value;
	
	RestaurantSortBy (String value) {
		this.value = value;
	}
	
	public static RestaurantSortBy of(String sortBy) {
		for (RestaurantSortBy restaurantSortBy : RestaurantSortBy.values()) {
			if (restaurantSortBy.name().equalsIgnoreCase(sortBy) || restaurantSortBy.value.equalsIgnoreCase(sortBy)) {
				return restaurantSortBy;
			}
		}
		return NAME;
	}
}
