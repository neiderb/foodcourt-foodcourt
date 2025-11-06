package com.foodcourt.foodcourt.domain.exception.dish;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class DishNotFoundException extends BusinessException {
	public DishNotFoundException(String message) {
		super(message);
	}
}
