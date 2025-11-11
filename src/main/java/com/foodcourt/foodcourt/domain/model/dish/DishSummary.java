package com.foodcourt.foodcourt.domain.model.dish;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishSummary {
	
	private Long id;
	private String name;
	private Long price;
	private String categoryName;
	private String imageUrl;
	
}
