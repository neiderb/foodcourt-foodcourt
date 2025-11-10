package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.domain.ports.CreateDishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.UNAUTHORIZED_ACTION;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class CreateDishUseCase implements CreateDishPort {
	
	private final DishRepositoryGateway dishRepositoryGateway;
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public Dish execute(Dish dish, Long idUserCreator) {
		if (isNull(dish.getIsAvailable())) dish.setIsAvailable(true);
		isRestaurantOwner(dish.getIdRestaurant(), idUserCreator);
		return dishRepositoryGateway.save(dish);
	}
	
	private void isRestaurantOwner(Long idRestaurant, Long idUserCreator) {
		log.trace("Validating if user with ID: {} is owner of restaurant with ID: {}", idUserCreator, idRestaurant);
		if (!restaurantRepositoryGateway.isRestaurantOwner(idRestaurant, idUserCreator))
			throw new InvalidUserException(UNAUTHORIZED_ACTION);
	}
	
}
