package com.foodcourt.foodcourt.domain.exception.order;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidOrderException extends BusinessException {
	public InvalidOrderException(String message) {
		super(message);
	}
}
