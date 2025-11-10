package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.auth.UserClaims;

public interface TokenServiceGateway {
	
	UserClaims parseToken(String token);
	
}
