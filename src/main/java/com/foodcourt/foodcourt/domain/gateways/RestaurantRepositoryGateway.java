package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.Restaurant;

public interface RestaurantRepositoryGateway {
	
	Restaurant save(Restaurant restaurant);
	
	Restaurant findByName(String name);
	
	Restaurant findById(Long id);
	
}
