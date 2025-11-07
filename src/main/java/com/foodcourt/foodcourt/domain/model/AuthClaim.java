package com.foodcourt.foodcourt.domain.model;

public enum AuthClaim {
	USER_ID("userId"),
	USER_EMAIL("email"),
	ROLE("role");
	
	public final String value;
	
	AuthClaim(String value) {
		this.value = value;
	}
}
