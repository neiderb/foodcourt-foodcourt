package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.request.UpdateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;

public interface DishHandler {
	
	CreateDishResponse createDish(CreateDishRequest request);
	
	void updateDish(UpdateDishRequest request);
	
	void toggleDishAvailability(Long idDish);
	
}
