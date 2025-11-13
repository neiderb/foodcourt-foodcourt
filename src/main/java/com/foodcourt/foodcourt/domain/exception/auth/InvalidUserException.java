package com.foodcourt.foodcourt.domain.exception.auth;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidUserException extends BusinessException {
	public InvalidUserException(String message) {
		super(message);
	}
}
