package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessage {
	
	public static final String GENERIC_ERROR = "An unexpected error occurred, please try again later";
	
	public static final String INVALID_ROLE = "The provided role is invalid";
	
	public static final String RESTAURANT_NAME_CANNOT_BE_ONLY_NUMBERS = "Restaurant name cannot contain only numbers";
	public static final String RESTAURANT_ALREADY_EXISTS = "A restaurant with the provided name already exists";
	
}
