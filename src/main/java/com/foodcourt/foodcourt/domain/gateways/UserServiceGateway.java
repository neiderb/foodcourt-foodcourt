package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.User;

public interface UserServiceGateway {
	
	User findById(Long id);
	
}
