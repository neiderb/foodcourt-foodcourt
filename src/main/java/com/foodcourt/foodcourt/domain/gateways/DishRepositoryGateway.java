package com.foodcourt.foodcourt.domain.gateways;

import com.foodcourt.foodcourt.domain.model.Dish;

public interface DishRepositoryGateway {
	
	Dish save(Dish dish);
	
	Dish findById(Long idDish);
	
}
