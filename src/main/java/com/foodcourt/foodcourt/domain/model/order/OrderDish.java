package com.foodcourt.foodcourt.domain.model.order;

import com.foodcourt.foodcourt.domain.model.dish.Dish;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDish {
	
	private Dish dish;
	private Integer quantity;
	
}
