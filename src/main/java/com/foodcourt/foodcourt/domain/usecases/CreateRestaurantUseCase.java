package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidRoleException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.model.auth.User;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.Regex.JUST_NUMBERS;
import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NAME_CANNOT_BE_ONLY_NUMBERS;
import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.USER_HAS_NO_VALID_ROLE;
import static com.foodcourt.foodcourt.domain.model.auth.UserRole.OWNER;

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
		User user = userServiceGateway.findById(userId);
		
		if (!OWNER.equals(user.getRole())) throw new InvalidRoleException(USER_HAS_NO_VALID_ROLE);
	}
	
}
