package com.foodcourt.foodcourt.domain.ports;

import com.foodcourt.foodcourt.domain.model.Restaurant;

public interface GetRestaurantByIdPort {
	
	Restaurant execute(Long id);
	
}
