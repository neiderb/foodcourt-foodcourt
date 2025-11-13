package com.foodcourt.foodcourt.domain.ports.dish;

import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;

public interface GetAllDishByRestaurantIdPort {
	
	PaginationResponse<DishSummary> execute(Long idRestaurant, DishPaginationFilter filter);
	
}
