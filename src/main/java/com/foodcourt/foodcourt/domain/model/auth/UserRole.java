package com.foodcourt.foodcourt.domain.model.auth;

import com.foodcourt.foodcourt.domain.exception.user.InvalidRoleException;

import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.INVALID_ROLE;

public enum UserRole {
	
	ADMIN,
	OWNER,
	EMPLOYEE,
	CLIENT;
	
	public static UserRole getRoleof(String role) {
		for (UserRole userRole : UserRole.values()) {
			if (userRole.name().equalsIgnoreCase(role)) {
				return userRole;
			}
		}
		throw new InvalidRoleException(INVALID_ROLE);
	}
	
}
