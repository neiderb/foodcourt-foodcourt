package com.foodcourt.foodcourt.domain.ports;

public interface ToggleDishAvailabilityPort {
	
	void execute(Long idDish, Long idUserCreator);
	
}
