package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.dish.DishNotFoundException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.domain.ports.UpdateDishPort;
import lombok.RequiredArgsConstructor;

import static com.foodcourt.foodcourt.domain.constants.DishErrorMessage.DISH_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.UNAUTHORIZED_ACTION;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class UpdateDishUseCase implements UpdateDishPort {
	
	private final DishRepositoryGateway dishRepositoryGateway;
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public void execute(Dish newDish, Long idUserCreator) {
		Dish existingDish = dishRepositoryGateway.findById(newDish.getId());
		if (isNull(existingDish)) throw new DishNotFoundException(DISH_NOT_FOUND);
		
		validateRestaurant(existingDish.getIdRestaurant(), idUserCreator);
		
		existingDish.setPrice(newDish.getPrice());
		existingDish.setDescription(newDish.getDescription());
		dishRepositoryGateway.save(existingDish);
	}
	
	private void validateRestaurant(Long idRestaurant, Long idUserCreator) {
		Restaurant restaurant = restaurantRepositoryGateway.findById(idRestaurant);
		if (isNull(restaurant)) throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
		if (!restaurant.getOwnerId().equals(idUserCreator)) {
			throw new InvalidUserException(UNAUTHORIZED_ACTION);
		}
	}
	
}
