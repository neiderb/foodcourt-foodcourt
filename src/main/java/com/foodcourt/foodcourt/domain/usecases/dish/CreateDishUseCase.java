package com.foodcourt.foodcourt.domain.usecases.dish;

import com.foodcourt.foodcourt.domain.exception.dish.InvalidCategoryException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.ports.dish.CreateDishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.DishValidationMessage.CATEGORY_ID_MUST_BE_MORE_THAN_ZERO;
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
		validateDish(dish);
		isRestaurantOwner(dish.getIdRestaurant(), idUserCreator);
		return dishRepositoryGateway.save(dish);
	}
	
	private void isRestaurantOwner(Long idRestaurant, Long idUserCreator) {
		log.trace("Validating if user with ID: {} is owner of restaurant with ID: {}", idUserCreator, idRestaurant);
		if (!restaurantRepositoryGateway.isRestaurantOwner(idRestaurant, idUserCreator))
			throw new InvalidUserException(UNAUTHORIZED_ACTION);
	}
	
	private void validateDish(Dish dish) {
		log.trace("Validating dish data");
		validateCategory(dish.getIdCategory());
	}
	
	private void validateCategory(Long idCategory) {
		log.trace("Validating category ID: {}", idCategory);
		if (isNull(idCategory) || idCategory <= 0)
			throw new InvalidCategoryException(CATEGORY_ID_MUST_BE_MORE_THAN_ZERO);
	}
	
}
