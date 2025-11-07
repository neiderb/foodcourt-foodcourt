package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.UserClaims;

public interface TokenServiceGateway {
	
	UserClaims parseToken(String token);
	
}
