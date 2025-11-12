package com.foodcourt.foodcourt.domain.model.order;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import com.foodcourt.foodcourt.domain.model.SortDirection;
import lombok.Builder;

import java.util.function.UnaryOperator;

@Builder
public class OrderPaginationFilter extends PaginationFilter {
	
	private Integer page;
	private Integer size;
	private String sortBy;
	private String sortDirection;
	
	@Override
	public int getPage() {
		return this.page;
	}
	
	@Override
	public int getSize() {
		return this.size;
	}
	
	@Override
	public String getSortBy() {
		return this.sortBy;
	}
	
	@Override
	public SortDirection getSortDirection() {
		return SortDirection.of(this.sortDirection);
	}
	
	@Override
	public void sanitizeSortBy(UnaryOperator<String> sanitizer) {
		this.sortBy = sanitizer.apply(fetchOrderSortByEnum());
	}
	
	private String fetchOrderSortByEnum() {
		return OrderSortBy.of(this.sortBy).getValue();
	}
	
}
