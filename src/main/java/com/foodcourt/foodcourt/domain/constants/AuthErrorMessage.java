package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthErrorMessage {
	
	public static final String USER_NOT_FOUND = "User not found with the provided ID";
	public static final String USER_HAS_NO_VALID_ROLE = "User does not have a valid role assigned";
	public static final String INVALID_ROLE = "The provided role is invalid";
	public static final String INVALID_TOKEN = "The provided token is invalid";
	public static final String UNAUTHORIZED_ACTION = "You are not authorized to perform this action";
	
}
