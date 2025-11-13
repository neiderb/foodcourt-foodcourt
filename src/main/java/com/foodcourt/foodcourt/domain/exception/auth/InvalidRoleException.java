package com.foodcourt.foodcourt.domain.exception.auth;

import com.foodcourt.foodcourt.domain.exception.BusinessException;

public class InvalidRoleException extends BusinessException {
	public InvalidRoleException(String message) {
		super(message);
	}
}
