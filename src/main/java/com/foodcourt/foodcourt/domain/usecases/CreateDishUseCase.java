package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.domain.ports.CreateDishPort;
import lombok.RequiredArgsConstructor;

import static com.foodcourt.foodcourt.domain.constants.ErrorMessage.RESTAURANT_NOT_FOUND;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class CreateDishUseCase implements CreateDishPort {
	
	private final DishRepositoryGateway dishRepositoryGateway;
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public Dish execute(Dish dish, Long idUserCreator) {
		if (isNull(dish.getIsAvailable())) dish.setIsAvailable(true);
		validateRestaurant(dish.getIdRestaurant(), idUserCreator);
		return dishRepositoryGateway.save(dish);
	}
	
	private void validateRestaurant(Long idRestaurant, Long ignoredIdUserCreator) {
		Restaurant restaurant = restaurantRepositoryGateway.findById(idRestaurant);
		if (isNull(restaurant)) throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
		// TODO: Validar que el usuario que crea el plato sea el propietario del restaurante
	}
	
}
