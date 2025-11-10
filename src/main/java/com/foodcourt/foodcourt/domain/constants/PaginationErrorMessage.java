package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaginationErrorMessage {
	
	public static final String FILTER_CANNOT_BE_NULL = "The filter object cannot be null";
	public static final String INVALID_PAGE_NUMBER = "The page number cannot be negative";
	public static final String INVALID_PAGE_SIZE = "The page size must be greater than 0";
	
}
