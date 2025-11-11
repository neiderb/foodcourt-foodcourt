package com.foodcourt.foodcourt.domain.gateways;

public interface UserServiceGateway {
	
	boolean isOwner(Long idUser);
	
	boolean isClient(Long idUser);
	
}
