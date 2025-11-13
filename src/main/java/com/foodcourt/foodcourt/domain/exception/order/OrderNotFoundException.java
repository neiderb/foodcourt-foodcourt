package com.foodcourt.foodcourt.domain.exception.order;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class OrderNotFoundException extends BusinessException {
	public OrderNotFoundException(String message) {
		super(message);
	}
}
