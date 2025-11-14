package com.foodcourt.foodcourt.domain.ports.order;

import com.foodcourt.foodcourt.domain.model.auth.UserClaims;

public interface DeliverOrderPort {
	
	void execute(Long idOrder, String securePin, UserClaims userClaims);
	
}
