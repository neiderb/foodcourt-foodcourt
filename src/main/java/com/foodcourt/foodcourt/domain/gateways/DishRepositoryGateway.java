package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;

public interface DishRepositoryGateway {
	
	Dish save(Dish dish);
	
	Dish findById(Long idDish);
	
	PaginationResponse<DishSummary> getDishesSummaryByIdRestaurant(Long idRestaurant, DishPaginationFilter filter);
	
}
