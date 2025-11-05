package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.application.handler.RestaurantHandler;
import com.foodcourt.foodcourt.application.mappers.CreateRestaurantRequestMapper;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements RestaurantHandler {
	
	private final CreateRestaurantPort createRestaurantPort;
	
	@Override
	public CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request) {
		Restaurant restaurantToSave = CreateRestaurantRequestMapper.INSTANCE.toDomain(request);
		Restaurant savedRestaurant = createRestaurantPort.execute(restaurantToSave);
		return new CreateRestaurantResponse(savedRestaurant.getId(), savedRestaurant.getName());
	}
	
}
