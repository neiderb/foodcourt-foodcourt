package com.foodcourt.foodcourt.domain.exception.order;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidOrderStatusException extends BusinessException {
	public InvalidOrderStatusException(String message) {
		super(message);
	}
}
