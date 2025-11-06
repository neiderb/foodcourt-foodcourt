package com.foodcourt.foodcourt.domain.exception.restaurant;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class RestaurantNotFoundException extends BusinessException {
	public RestaurantNotFoundException(String message) {
		super(message);
	}
}
