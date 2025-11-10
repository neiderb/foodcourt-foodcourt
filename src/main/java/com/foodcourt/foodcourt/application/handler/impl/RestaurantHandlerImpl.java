package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.application.dto.response.RestaurantResponse;
import com.foodcourt.foodcourt.application.handler.RestaurantHandler;
import com.foodcourt.foodcourt.application.mappers.CreateRestaurantRequestMapper;
import com.foodcourt.foodcourt.application.mappers.RestaurantResponseMapper;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.GetAllRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.GetRestaurantByIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements RestaurantHandler {
	
	private final CreateRestaurantPort createRestaurantPort;
	private final GetRestaurantByIdPort getRestaurantByIdPort;
	private final GetAllRestaurantPort getAllRestaurantPort;
	
	@Override
	public CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request) {
		Restaurant restaurantToSave = CreateRestaurantRequestMapper.INSTANCE.toDomain(request);
		log.trace("Creating restaurant");
		Restaurant savedRestaurant = createRestaurantPort.execute(restaurantToSave);
		log.debug("Created restaurant with ID: {}", savedRestaurant.getId());
		return new CreateRestaurantResponse(savedRestaurant.getId(), savedRestaurant.getName());
	}
	
	@Override
	public RestaurantResponse getRestaurantById(Long id) {
		log.trace("Getting restaurant by ID: {}", id);
		return RestaurantResponseMapper.INSTANCE.toResponse(
			getRestaurantByIdPort.execute(id)
		);
	}
	
	@Override
	public PaginationResponse<RestaurantSummary> getAllRestaurants(GetAllRestaurantRequest request) {
		log.trace("Getting all restaurants with pagination - Page: {}, Size: {}",
			request.page(), request.size());
		var filter = RestaurantPaginationFilter.builder()
			.page(request.page())
			.size(request.size())
			.sortBy(request.sortBy())
			.sortDirection(request.sortDirection())
			.build();
		
		return getAllRestaurantPort.execute(filter);
	}
}
