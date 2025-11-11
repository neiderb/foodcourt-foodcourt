package com.foodcourt.foodcourt.application.handler;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllDishByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.request.UpdateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;

public interface DishHandler {
	
	CreateDishResponse createDish(CreateDishRequest request);
	
	void updateDish(UpdateDishRequest request);
	
	void toggleDishAvailability(Long idDish);
	
	PaginationResponse<DishSummary> getDishesByIdRestaurant(Long idRestaurant, GetAllDishByRestaurantIdRequest request);
	
}
