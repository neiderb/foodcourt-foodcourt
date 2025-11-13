package com.foodcourt.foodcourt.domain.usecases.restaurant;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.auth.InvalidRoleException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.ports.restaurant.CreateRestaurantPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.Regex.JUST_NUMBERS;
import static com.foodcourt.foodcourt.domain.constants.RestaurantValidationMessage.RESTAURANT_NAME_CANNOT_BE_ONLY_NUMBERS;
import static com.foodcourt.foodcourt.domain.constants.AuthErrorMessage.USER_HAS_NO_VALID_ROLE;

@Slf4j
@RequiredArgsConstructor
public class CreateRestaurantUseCase implements CreateRestaurantPort {
	
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	private final UserServiceGateway userServiceGateway;
	
	@Override
	public Restaurant execute(Restaurant restaurant) {
		validateName(restaurant.getName());
		validateUser(restaurant.getOwnerId());
		return restaurantRepositoryGateway.save(restaurant);
	}
	
	private void validateName(String name) {
		log.trace("Validating restaurant name: {}", name);
		if (name.matches(JUST_NUMBERS)) throw new InvalidRestaurantException(RESTAURANT_NAME_CANNOT_BE_ONLY_NUMBERS);
	}
	
	private void validateUser(Long userId) {
		log.trace("Validating role owner with user ID: {}", userId);
		if (!userServiceGateway.isOwner(userId)) throw new InvalidRoleException(USER_HAS_NO_VALID_ROLE);
	}
	
}
