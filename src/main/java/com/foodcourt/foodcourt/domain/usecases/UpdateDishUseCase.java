package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.dish.DishNotFoundException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.ports.UpdateDishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.DishErrorMessage.DISH_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.UNAUTHORIZED_ACTION;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class UpdateDishUseCase implements UpdateDishPort {
	
	private final DishRepositoryGateway dishRepositoryGateway;
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public void execute(Dish newDish, Long idUserCreator) {
		log.trace("Updating dish with ID: {}", newDish.getId());
		Dish existingDish = dishRepositoryGateway.findById(newDish.getId());
		if (isNull(existingDish)) throw new DishNotFoundException(DISH_NOT_FOUND);
		
		validateRestaurantOwner(existingDish.getIdRestaurant(), idUserCreator);
		
		existingDish.setPrice(newDish.getPrice());
		existingDish.setDescription(newDish.getDescription());
		dishRepositoryGateway.save(existingDish);
	}
	
	private void validateRestaurantOwner(Long idRestaurant, Long idUserCreator) {
		log.trace("Validating if user with ID: {} is owner of restaurant with ID: {}", idUserCreator, idRestaurant);
		if (!restaurantRepositoryGateway.isRestaurantOwner(idRestaurant, idUserCreator))
			throw new InvalidUserException(UNAUTHORIZED_ACTION);
	}
	
}
