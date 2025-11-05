package com.foodcourt.foodcourt.domain.ports;

import com.foodcourt.foodcourt.domain.model.Dish;

public interface CreateDishPort {
	
	Dish execute(Dish dish, Long idUserCreator);
	
}
