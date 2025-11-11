package com.foodcourt.foodcourt.domain.exception.order;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidClientException extends BusinessException {
	public InvalidClientException(String message) {
		super(message);
	}
}
