package com.foodcourt.foodcourt.domain.ports;

import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;

public interface GetRestaurantByIdPort {
	
	Restaurant execute(Long id);
	
}
