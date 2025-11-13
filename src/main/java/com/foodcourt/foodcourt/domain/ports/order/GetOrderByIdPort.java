package com.foodcourt.foodcourt.domain.ports.order;

import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.order.Order;

public interface GetOrderByIdPort {
	
	Order execute(Long idOrder, UserClaims userClaims);
	
}
