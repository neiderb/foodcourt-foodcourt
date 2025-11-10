package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.application.dto.response.RestaurantResponse;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;

public interface RestaurantHandler {
	
	CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request);
	
	RestaurantResponse getRestaurantById(Long id);
	
	PaginationResponse<RestaurantSummary> getAllRestaurants(GetAllRestaurantRequest request);
	
}
