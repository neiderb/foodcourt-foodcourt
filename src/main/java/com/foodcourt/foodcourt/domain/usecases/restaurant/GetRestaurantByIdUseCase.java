package com.foodcourt.foodcourt.domain.usecases.restaurant;

import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.ports.restaurant.GetRestaurantByIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NOT_FOUND;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class GetRestaurantByIdUseCase implements GetRestaurantByIdPort {
	
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public Restaurant execute(Long id) {
		Restaurant existingRestaurant = restaurantRepositoryGateway.findById(id);
		if (isNull(existingRestaurant)) throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
		log.debug("Found restaurant with ID: {}", id);
		return existingRestaurant;
	}
	
}
