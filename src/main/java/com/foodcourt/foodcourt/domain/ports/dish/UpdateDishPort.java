package com.foodcourt.foodcourt.domain.ports.dish;

import com.foodcourt.foodcourt.domain.model.dish.Dish;

public interface UpdateDishPort {
	
	void execute(Dish newDish, Long idUserCreator);
	
}
