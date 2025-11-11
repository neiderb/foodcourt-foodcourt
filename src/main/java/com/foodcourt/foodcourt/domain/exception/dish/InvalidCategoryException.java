package com.foodcourt.foodcourt.domain.exception.dish;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidCategoryException extends BusinessException {
	public InvalidCategoryException(String message) {
		super(message);
	}
}
