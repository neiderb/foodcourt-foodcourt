package com.foodcourt.foodcourt.domain.ports.restaurant;

import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;

public interface GetAllRestaurantPort {
	
	PaginationResponse<RestaurantSummary> execute(RestaurantPaginationFilter filter);
	
}
