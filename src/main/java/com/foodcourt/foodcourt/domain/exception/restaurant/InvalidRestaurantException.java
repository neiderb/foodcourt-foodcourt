package com.foodcourt.foodcourt.domain.exception.restaurant;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidRestaurantException extends BusinessException {
	public InvalidRestaurantException(String message) {
		super(message);
	}
}
