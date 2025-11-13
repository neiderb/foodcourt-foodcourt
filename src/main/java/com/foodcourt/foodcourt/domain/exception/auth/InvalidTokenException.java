package com.foodcourt.foodcourt.domain.exception.auth;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidTokenException extends BusinessException {
	public InvalidTokenException(String message) {
		super(message);
	}
}
