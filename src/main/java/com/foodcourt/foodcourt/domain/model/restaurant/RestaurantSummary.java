package com.foodcourt.foodcourt.domain.model.restaurant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantSummary {

	private Long idRestaurant;
	private String name;
	private String urlLogo;

}
