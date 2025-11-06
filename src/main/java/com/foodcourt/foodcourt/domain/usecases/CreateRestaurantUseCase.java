package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidRoleException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.domain.model.User;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import lombok.RequiredArgsConstructor;

import static com.foodcourt.foodcourt.domain.constants.Regex.JUST_NUMBERS;
import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NAME_CANNOT_BE_ONLY_NUMBERS;
import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.INVALID_ROLE;
import static com.foodcourt.foodcourt.domain.model.UserRole.OWNER;

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
		if (name.matches(JUST_NUMBERS)) throw new InvalidRestaurantException(RESTAURANT_NAME_CANNOT_BE_ONLY_NUMBERS);
	}
	
	private void validateUser(Long userId) {
		User user = userServiceGateway.findById(userId);
		
		if (!OWNER.equals(user.getRole())) throw new InvalidRoleException(INVALID_ROLE);
	}
	
}
