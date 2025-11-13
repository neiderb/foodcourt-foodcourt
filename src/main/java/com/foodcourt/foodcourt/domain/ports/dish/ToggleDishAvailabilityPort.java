package com.foodcourt.foodcourt.domain.ports.dish;

public interface ToggleDishAvailabilityPort {
	
	void execute(Long idDish, Long idUserCreator);
	
}
