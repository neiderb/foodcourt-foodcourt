package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.auth.User;

public interface UserServiceGateway {
	
	User findById(Long id);
	
}
