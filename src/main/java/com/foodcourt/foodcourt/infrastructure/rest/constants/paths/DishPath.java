package com.foodcourt.foodcourt.infrastructure.rest.constants.paths;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DishPath {
	
	public static final String BASE = "/api/v1/dish";
	
	public static final String TOGGLE_AVAILABILITY = "/{idDish}/toggle-availability";
	
}
