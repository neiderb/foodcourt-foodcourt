package com.foodcourt.foodcourt.domain.model.restaurant;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import com.foodcourt.foodcourt.domain.model.enums.SortDirection;
import com.foodcourt.foodcourt.domain.model.restaurant.enums.RestaurantSortBy;
import lombok.Builder;

import java.util.function.UnaryOperator;

@Builder
public class RestaurantPaginationFilter extends PaginationFilter {
	
	private Integer size;
	private Integer page;
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
		return fetchRestaurantByEnum().getValue();
	}
	
	@Override
	public SortDirection getSortDirection() {
		return SortDirection.of(this.sortDirection);
	}
	
	@Override
	public void sanitizeSortBy(UnaryOperator<String> sanitizer) {
		// Not implemented
	}
	
	private RestaurantSortBy fetchRestaurantByEnum() {
		return RestaurantSortBy.of(this.sortBy);
	}
	
}
