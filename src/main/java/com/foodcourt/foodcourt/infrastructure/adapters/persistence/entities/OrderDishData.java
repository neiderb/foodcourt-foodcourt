package com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_dish")
public class OrderDishData {
	
	@EmbeddedId
	private OrderDishKey id;
	
	@ManyToOne
	@MapsId("idOrder")
	@JoinColumn(name = "id_order")
	private OrderData order;
	
	@ManyToOne
	@MapsId("idDish")
	@JoinColumn(name = "id_dish")
	private DishData dish;
	
	private Integer quantity;
	
}
