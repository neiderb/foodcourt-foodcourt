package com.foodcourt.foodcourt.domain.ports;

import com.foodcourt.foodcourt.domain.model.Restaurant;

public interface CreateRestaurantPort {
	
	Restaurant execute(Restaurant restaurant);
	
}
