package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.Restaurant;

public interface RestaurantRepositoryGateway {
	
	Restaurant save(Restaurant restaurant);
	
	boolean existByNameOrNit(String name, String nit);
	
	Restaurant findById(Long id);
	
}
