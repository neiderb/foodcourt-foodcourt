package com.foodcourt.foodcourt.domain.model.order;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {
	
	private Long id;
	private Long idClient;
	private LocalDateTime orderDate;
	private OrderStatus status;
	private Long idChef;
	private Long idRestaurant;
	private List<OrderDish> items;
	
}
