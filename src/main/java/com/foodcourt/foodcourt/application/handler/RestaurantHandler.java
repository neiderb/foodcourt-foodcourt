package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.application.dto.response.RestaurantResponse;

public interface RestaurantHandler {
	
	CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request);
	
	RestaurantResponse getRestaurantById(Long id);
	
}
