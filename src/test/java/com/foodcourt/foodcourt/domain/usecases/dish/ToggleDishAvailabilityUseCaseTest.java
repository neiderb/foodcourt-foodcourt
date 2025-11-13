package com.foodcourt.foodcourt.domain.usecases.dish;

import com.foodcourt.foodcourt.domain.exception.dish.DishNotFoundException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToggleDishAvailabilityUseCaseTest {
	
	@InjectMocks
	private ToggleDishAvailabilityUseCase toggleDishAvailabilityUseCase;
	
	@Mock
	private DishRepositoryGateway dishRepositoryGateway;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Test
	void shouldUnavailableDishAvailabilityWhenUserIsOwner() {
		final boolean initialAvailability = true;
		final Long idUserCreator = 99L;
		var existingDish = existingDish(initialAvailability);
		final Long idRestaurantOwner = existingDish.getIdRestaurant();
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(existingDish);
		when(restaurantRepositoryGateway.isRestaurantOwner(idRestaurantOwner, idUserCreator)).thenReturn(true);
		
		toggleDishAvailabilityUseCase.execute(existingDish.getId(), idUserCreator);
		
		verify(dishRepositoryGateway).save(assertArg(dish -> assertNotEquals(initialAvailability, dish.getIsAvailable())));
	}
	
	@Test
	void shouldAvailableDishAvailabilityWhenUserIsOwner() {
		final boolean initialAvailability = false;
		final Long idUserCreator = 99L;
		var existingDish = existingDish(initialAvailability);
		final Long idRestaurantOwner = existingDish.getIdRestaurant();
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(existingDish);
		when(restaurantRepositoryGateway.isRestaurantOwner(idRestaurantOwner, idUserCreator)).thenReturn(true);
		
		toggleDishAvailabilityUseCase.execute(existingDish.getId(), idUserCreator);
		
		verify(dishRepositoryGateway).save(assertArg(dish -> assertNotEquals(initialAvailability, dish.getIsAvailable())));
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotOwner() {
		final boolean initialAvailability = false;
		final Long idUserCreator = 100L;
		final Long idDish = 1L;
		var existingDish = Dish.builder()
			.id(idDish)
			.idRestaurant(50L)
			.isAvailable(initialAvailability)
			.build();
		final Long idRestaurantOwner = existingDish.getIdRestaurant();
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(existingDish);
		when(restaurantRepositoryGateway.isRestaurantOwner(idRestaurantOwner, idUserCreator)).thenReturn(false);
		
		assertThrows(InvalidUserException.class, () -> toggleDishAvailabilityUseCase.execute(idDish, idUserCreator));
	}
	
	@Test
	void shouldThrowExceptionWhenDishNotFound() {
		final Long idUserCreator = 100L;
		final Long idDish = 1L;
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(DishNotFoundException.class, () -> toggleDishAvailabilityUseCase.execute(idDish, idUserCreator));
	}
	
	private Dish existingDish(boolean isAvailable) {
		return Dish.builder()
				.id(1L)
				.isAvailable(isAvailable)
				.idRestaurant(1L)
				.build();
	}
}
