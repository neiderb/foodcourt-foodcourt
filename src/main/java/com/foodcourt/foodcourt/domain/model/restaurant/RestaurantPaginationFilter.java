package com.foodcourt.foodcourt.domain.model.restaurant;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import com.foodcourt.foodcourt.domain.model.SortDirection;
import lombok.Builder;

@Builder
public class RestaurantPaginationFilter extends PaginationFilter {
	
	private Integer size;
	private Integer page;
	private String sortBy;
	private String sortDirection;
	
	@Override
	public int getSize() {
		return this.size;
	}
	
	@Override
	public int getPage() {
		return this.page;
	}
	
	@Override
	public String getSortBy() {
		return this.sortBy;
	}
	
	@Override
	public SortDirection getSortDirection() {
		return SortDirection.of(this.sortDirection);
	}
	
}
