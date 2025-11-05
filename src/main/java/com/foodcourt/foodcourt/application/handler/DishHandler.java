package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;

public interface DishHandler {
	
	CreateDishResponse createDish(CreateDishRequest request);
	
}
