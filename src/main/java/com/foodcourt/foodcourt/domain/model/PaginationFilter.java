package com.foodcourt.foodcourt.domain.model;

import static java.util.Objects.isNull;

public abstract class PaginationFilter {
	
	public abstract int getSize();
	public abstract int getPage();
	public abstract String getSortBy();
	public abstract SortDirection getSortDirection();
	
	public boolean isAscending() {
		return isNull(getSortDirection()) || getSortDirection() == SortDirection.ASC;
	}
	
}
