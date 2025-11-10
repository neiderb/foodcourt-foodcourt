package com.foodcourt.foodcourt.domain.model.restaurant;

import com.foodcourt.foodcourt.domain.model.SortDirection;
import lombok.Builder;
import lombok.Getter;

@Builder
public class RestaurantPaginationFilter{
	
	@Getter
	private Integer size;
	
	@Getter
	private Integer page;
	
	private String sortBy;
	private String sortDirection;
	
	public RestaurantSortBy getSortBy() {
		return RestaurantSortBy.of(this.sortBy);
	}
	
	public boolean isAscending() {
		return getSortDirection() == SortDirection.ASC;
	}
	
	private SortDirection getSortDirection() {
		return SortDirection.of(this.sortDirection);
	}
	
}
