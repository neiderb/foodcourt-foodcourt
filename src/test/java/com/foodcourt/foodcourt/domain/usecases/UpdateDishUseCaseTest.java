package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.dish.DishNotFoundException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateDishUseCaseTest {
	
	@InjectMocks
	private UpdateDishUseCase updateDishUseCase;
	
	@Mock
	private DishRepositoryGateway dishRepositoryGateway;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Test
	void shouldUpdateDishSuccessfully() {
		final Long idDish = 1L;
		final Long idUserCreator = 2L;
		Dish dishToUpdate = validDishUpdatable(idDish);
		Dish existingDish = Dish.builder()
			.id(dishToUpdate.getId())
			.description("Old description")
			.price(1200L)
			.build();
		
		Restaurant existingRestaurant = Restaurant.builder()
			.id(dishToUpdate.getIdRestaurant())
			.ownerId(idUserCreator)
			.build();
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(existingDish);
		when(restaurantRepositoryGateway.findById(existingDish.getIdRestaurant())).thenReturn(existingRestaurant);
		updateDishUseCase.execute(dishToUpdate, idUserCreator);
		
		verify(dishRepositoryGateway).save(argThat(dish ->
			dish.getDescription().equals(dishToUpdate.getDescription()) &&
			dish.getPrice().equals(dishToUpdate.getPrice())
        ));
	}
	
	@Test
	void shouldThrowExceptionWhenDishNotFound() {
		final Long idDish = 1L;
		Dish dishToUpdate = validDishUpdatable(idDish);
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(null);
	
		assertThrows(DishNotFoundException.class, () -> updateDishUseCase.execute(dishToUpdate, 10L));
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantNotFound() {
		final Long idDish = 1L;
		Dish dishToUpdate = validDishUpdatable(idDish);
		Dish existingDish = Dish.builder()
			.id(dishToUpdate.getId())
			.description("Old description")
			.price(1200L)
			.idRestaurant(5L)
			.build();
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(existingDish);
		when(restaurantRepositoryGateway.findById(existingDish.getIdRestaurant())).thenReturn(null);
	
		assertThrows(RestaurantNotFoundException.class, () -> updateDishUseCase.execute(dishToUpdate, 10L));
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotOwnerOfRestaurant() {
		final Long idDish = 1L;
		final Long idUserCreator = 2L;
		Dish dishToUpdate = validDishUpdatable(idDish);
		Dish existingDish = Dish.builder()
			.id(dishToUpdate.getId())
			.description("Old description")
			.price(1200L)
			.idRestaurant(5L)
			.build();
		
		Restaurant existingRestaurant = Restaurant.builder()
			.id(existingDish.getIdRestaurant())
			.ownerId(99L)
			.build();
		
		when(dishRepositoryGateway.findById(any(Long.class))).thenReturn(existingDish);
		when(restaurantRepositoryGateway.findById(existingDish.getIdRestaurant())).thenReturn(existingRestaurant);
	
		assertThrows(InvalidUserException.class, () -> updateDishUseCase.execute(dishToUpdate, idUserCreator));
	}
	
	private Dish validDishUpdatable(Long idExist) {
		return Dish.builder()
			.id(idExist)
			.description("Updated description")
			.price(1500L)
			.build();
	}
	
}
