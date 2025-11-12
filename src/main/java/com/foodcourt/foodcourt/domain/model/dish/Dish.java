package com.foodcourt.foodcourt.domain.model.dish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
	
	private Long id;
	private String name;
	private Long idCategory;
	private String description;
	private Long price;
	private Long idRestaurant;
	private String imageUrl;
	private Boolean isAvailable;
	
}
