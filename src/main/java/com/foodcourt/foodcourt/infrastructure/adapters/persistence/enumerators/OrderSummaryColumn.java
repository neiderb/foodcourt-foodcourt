package com.foodcourt.foodcourt.infrastructure.adapters.persistence.enumerators;

import com.foodcourt.foodcourt.domain.model.order.OrderSortBy;
import lombok.Getter;

@Getter
public enum OrderSummaryColumn {
	
	CLIENT_NAME(OrderSortBy.CLIENT_NAME, "clientName"),
	ORDER_DATE(OrderSortBy.ORDER_DATE, "orderDate"),
	STATUS(OrderSortBy.STATUS, "status"),
	CHEF_NAME(OrderSortBy.CHEF_NAME, "chefName");
	
	private final OrderSortBy sortBy;
	private final String columnName;
	
	OrderSummaryColumn (OrderSortBy sortBy, String columnName) {
		this.sortBy = sortBy;
		this.columnName = columnName;
	}
	
	public static OrderSummaryColumn of(OrderSortBy sortBy) {
		for (OrderSummaryColumn orderSummaryColumn : OrderSummaryColumn.values()) {
			if (orderSummaryColumn.getSortBy() == sortBy) {
				return orderSummaryColumn;
			}
		}
		return ORDER_DATE;
	}
	
}
