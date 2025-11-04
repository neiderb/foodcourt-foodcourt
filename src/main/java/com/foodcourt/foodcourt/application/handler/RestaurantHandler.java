package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;

public interface RestaurantHandler {
	
	CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request);
	
}
