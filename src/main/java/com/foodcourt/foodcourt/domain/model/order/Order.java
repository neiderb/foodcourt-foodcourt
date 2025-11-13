package com.foodcourt.foodcourt.domain.model.order;

import com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	
	private Long id;
	private Long idClient;
	private LocalDateTime orderDate;
	private OrderStatus status;
	private Long idChef;
	private Long idRestaurant;
	private List<OrderDish> items;
	
}
