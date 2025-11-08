package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.domain.ports.GetRestaurantByIdPort;
import lombok.RequiredArgsConstructor;

import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NOT_FOUND;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class GetRestaurantByIdUseCase implements GetRestaurantByIdPort {
	
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public Restaurant execute(Long id) {
		Restaurant existingRestaurant = restaurantRepositoryGateway.findById(id);
		if (isNull(existingRestaurant)) throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
		return existingRestaurant;
	}
	
}
