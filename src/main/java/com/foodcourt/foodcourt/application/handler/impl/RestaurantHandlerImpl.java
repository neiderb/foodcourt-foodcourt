package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.application.handler.RestaurantHandler;
import com.foodcourt.foodcourt.application.mappers.CreateRestaurantRequestMapper;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements RestaurantHandler {
	
	private final CreateRestaurantPort createRestaurantPort;
	
	@Override
	public CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request) {
		Restaurant restaurantToSave = CreateRestaurantRequestMapper.INSTANCE.toDomain(request);
		log.trace("Creating restaurant: {}", restaurantToSave);
		Restaurant savedRestaurant = createRestaurantPort.execute(restaurantToSave);
		log.debug("Created restaurant with ID: {}", savedRestaurant.getId());
		return new CreateRestaurantResponse(savedRestaurant.getId(), savedRestaurant.getName());
	}
	
}
