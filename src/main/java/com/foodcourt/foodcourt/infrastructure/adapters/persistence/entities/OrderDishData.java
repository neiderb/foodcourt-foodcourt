package com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_dish")
public class OrderDishData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_order")
	private OrderData order;
	
	@ManyToOne
	@JoinColumn(name = "id_dish")
	private DishData dish;
	
	private Integer quantity;
	
	@Override
	public String toString() {
		return "OrderDishData{" + "id=" + id + ", order=" + order.getId() + ", dish=" + dish + ", quantity=" + quantity + '}';
	}
}
