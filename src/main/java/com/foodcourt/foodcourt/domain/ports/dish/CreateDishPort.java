package com.foodcourt.foodcourt.domain.ports.dish;

import com.foodcourt.foodcourt.domain.model.dish.Dish;

public interface CreateDishPort {
	
	Dish execute(Dish dish, Long idUserCreator);
	
}
