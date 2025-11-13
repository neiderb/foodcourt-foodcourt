package com.foodcourt.foodcourt.domain.ports.order;

import com.foodcourt.foodcourt.domain.model.auth.UserClaims;

public interface CompleteOrderPort {
	
	void execute(Long idOrder, UserClaims userClaims);
	
}
