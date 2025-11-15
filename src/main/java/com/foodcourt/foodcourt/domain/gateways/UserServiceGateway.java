package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.auth.User;

public interface UserServiceGateway {
	
	boolean isOwner(Long idUser);
	
	boolean isClient(Long idUser);
	
	User getUserById(Long idUser);
	
}
