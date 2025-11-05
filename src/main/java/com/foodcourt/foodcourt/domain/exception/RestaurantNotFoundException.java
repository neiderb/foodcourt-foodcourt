package com.foodcourt.foodcourt.domain.exception;

public class RestaurantNotFoundException extends BusinessException {
	public RestaurantNotFoundException(String message) {
		super(message);
	}
}
