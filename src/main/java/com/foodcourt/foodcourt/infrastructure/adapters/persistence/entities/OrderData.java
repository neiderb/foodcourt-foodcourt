package com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "foodcourt_order")
public class OrderData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_client")
	private Long idClient;
	
	@Column(name = "order_date")
	private LocalDateTime orderDate;
	
	private String status;
	
	@Column(name = "id_chef")
	private Long idChef;
	
	@Column(name = "id_restaurant")
	private Long idRestaurant;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderDishData> items;
	
	@Override
	public String toString() {
		return "OrderData{" + "id=" + id + ", idClient=" + idClient + ", orderDate=" + orderDate + ", status='" + status + '\'' + ", idChef=" + idChef + ", idRestaurant=" +
			idRestaurant + ", items=" + items + '}';
	}
}
