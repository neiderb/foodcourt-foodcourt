package com.foodcourt.foodcourt.domain.model;

import com.foodcourt.foodcourt.domain.exception.InvalidPaginationFilterException;

import static com.foodcourt.foodcourt.domain.constants.PaginationErrorMessage.INVALID_PAGE_NUMBER;
import static com.foodcourt.foodcourt.domain.constants.PaginationErrorMessage.INVALID_PAGE_SIZE;

public abstract class PaginationFilter {
	
	public abstract int getPage();
	public abstract int getSize();
	public abstract String getSortBy();
	public abstract SortDirection getSortDirection();
	
	public boolean isAscending() {
		return getSortDirection() == SortDirection.ASC;
	}
	
	public void validateFilter() {
		if (getSize() <= 0) throw new InvalidPaginationFilterException(INVALID_PAGE_SIZE);
		if (getPage() < 0) throw new InvalidPaginationFilterException(INVALID_PAGE_NUMBER);
	}
	
}
