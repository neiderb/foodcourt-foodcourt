package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;

public interface RestaurantRepositoryGateway {
	
	Restaurant save(Restaurant restaurant);
	
	boolean existByNameOrNit(String name, String nit);
	
	Restaurant findById(Long id);
	
	boolean isRestaurantOwner(Long idRestaurant, Long idUser);
	
	PaginationResponse<RestaurantSummary> getAllRestaurantSummaries(RestaurantPaginationFilter filter);
	
	boolean existsById(Long id);
	
}
