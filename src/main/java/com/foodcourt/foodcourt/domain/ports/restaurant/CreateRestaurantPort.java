package com.foodcourt.foodcourt.domain.ports.restaurant;

import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;

public interface CreateRestaurantPort {
	
	Restaurant execute(Restaurant restaurant);
	
}
