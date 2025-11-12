package com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class OrderDishKey {
	
	private Long idOrder;
	private Long idDish;
	
}
