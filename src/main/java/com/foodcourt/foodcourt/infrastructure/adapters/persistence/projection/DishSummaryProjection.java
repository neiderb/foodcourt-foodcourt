package com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection;

public interface DishSummaryProjection {
	
	Long getId();
	String getName();
	Long getPrice();
	String getCategoryName();
	String getImageUrl();
	
}
