package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RestaurantErrorMessage {
	
	public static final String RESTAURANT_NAME_CANNOT_BE_ONLY_NUMBERS = "Restaurant name cannot contain only numbers";
	public static final String RESTAURANT_ALREADY_EXISTS = "A restaurant with the provided name or nit already exists";
	public static final String RESTAURANT_NOT_FOUND = "Restaurant not found with the provided ID";
	
}
