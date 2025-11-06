package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DishValidationMessage {
	
	public static final String NAME_REQUIRED = "Name is required";
	public static final String CATEGORY_ID_REQUIRED = "Category is required";
	public static final String DESCRIPTION_REQUIRED = "Description is required";
	public static final String PRICE_REQUIRED = "Price is required";
	public static final String RESTAURANT_ID_REQUIRED = "Restaurant is required";
	public static final String IMAGE_URL_REQUIRED = "Image URL is required";
	
	public static final String PRICE_MUST_BE_MORE_THAN_ZERO = "Price must be more than zero";
	
}
