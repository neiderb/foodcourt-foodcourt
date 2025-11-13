package com.foodcourt.foodcourt.domain.usecases.dish;

import com.foodcourt.foodcourt.domain.exception.dish.DishNotFoundException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.ports.dish.ToggleDishAvailabilityPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.DishErrorMessage.DISH_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.UNAUTHORIZED_ACTION;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class ToggleDishAvailabilityUseCase implements ToggleDishAvailabilityPort {
	
	private final DishRepositoryGateway dishRepositoryGateway;
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public void execute(Long idDish, Long idUserCreator) {
		var existingDish = dishRepositoryGateway.findById(idDish);
		if (isNull(existingDish)) throw new DishNotFoundException(DISH_NOT_FOUND);
		log.trace("Dish found with ID {} and availability {}", existingDish.getId(), existingDish.getIsAvailable());
		validateRestaurantOwner(existingDish.getIdRestaurant(), idUserCreator);
		existingDish.setIsAvailable(!existingDish.getIsAvailable());
		dishRepositoryGateway.save(existingDish);
	}
	
	private void validateRestaurantOwner(Long idRestaurant, Long idUserCreator) {
		log.trace("Validating restaurant ownership for user ID: {} on restaurant ID: {}", idUserCreator, idRestaurant);
		if (!restaurantRepositoryGateway.isRestaurantOwner(idRestaurant, idUserCreator))
			throw new InvalidUserException(UNAUTHORIZED_ACTION);
	}
	
}
