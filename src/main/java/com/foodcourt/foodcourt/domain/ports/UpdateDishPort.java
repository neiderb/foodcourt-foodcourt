package com.foodcourt.foodcourt.domain.ports;

import com.foodcourt.foodcourt.domain.model.Dish;

public interface UpdateDishPort {
	
	void execute(Dish newDish, Long idUserCreator);
	
}
