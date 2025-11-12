package com.foodcourt.foodcourt.domain.model.order;

import lombok.Getter;

@Getter
public enum OrderSortBy {
	
	CLIENT_NAME("clientName"),
	ORDER_DATE("orderDate"),
	STATUS("status"),
	CHEF_NAME("chefName");
	
	private final String value;
	
	OrderSortBy (String value) {
		this.value = value;
	}
	
	public static OrderSortBy of(String sortBy) {
		for (OrderSortBy orderSortBy : OrderSortBy.values()) {
			if (orderSortBy.name().equalsIgnoreCase(sortBy) || orderSortBy.value.equalsIgnoreCase(sortBy)) {
				return orderSortBy;
			}
		}
		return ORDER_DATE;
	}
	
}
