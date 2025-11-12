package com.foodcourt.foodcourt.domain.model.dish;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import com.foodcourt.foodcourt.domain.model.SortDirection;
import lombok.Builder;
import lombok.Getter;

import java.util.function.UnaryOperator;

@Builder
public class DishPaginationFilter extends PaginationFilter {
	
	private Integer size;
	private Integer page;
	private String sortBy;
	private String sortDirection;
	
	@Getter
	private Long idCategory;
	
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
		return fetchDishSortByEnum().getValue();
	}
	
	@Override
	public SortDirection getSortDirection() {
		return SortDirection.of(this.sortDirection);
	}
	
	@Override
	public void sanitizeSortBy(UnaryOperator<String> sanitizer) {
		// Not implemented
	}
	
	private DishSortBy fetchDishSortByEnum() {
		return DishSortBy.of(this.sortBy);
	}
	
}
