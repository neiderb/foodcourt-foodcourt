package com.foodcourt.foodcourt.domain.model.order;

import com.foodcourt.foodcourt.domain.model.dish.Dish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDish {
	
	private Long id;
	private Long idOrder;
	private Dish dish;
	private Integer quantity;
	
}
