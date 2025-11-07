package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateDishUseCaseTest {
	
	@InjectMocks
	private CreateDishUseCase createDishUseCase;
	
	@Mock
	private DishRepositoryGateway dishRepositoryGateway;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Test
	void shouldCreateDishSuccessfully() {
		final Long idDish = 1L;
		final Long idUserCreator = 2L;
		Dish dishToCreate = validDish();
		Dish expectedDish = validDish();
		expectedDish.setId(idDish);
		expectedDish.setIsAvailable(true);
		
		Restaurant existingRestaurant = Restaurant.builder()
			.id(dishToCreate.getIdRestaurant())
			.ownerId(idUserCreator)
			.build();
		
		when(restaurantRepositoryGateway.findById(any(Long.class))).thenReturn(existingRestaurant);
		when(dishRepositoryGateway.save(any(Dish.class))).thenReturn(expectedDish);
		
		Dish dishCreated = createDishUseCase.execute(dishToCreate, idUserCreator);
		
		assertNotNull(dishCreated);
		assertEquals(idDish, dishCreated.getId());
		assertEquals(expectedDish.getName(), dishCreated.getName());
		assertEquals(expectedDish.getIdCategory(), dishCreated.getIdCategory());
		assertEquals(expectedDish.getDescription(), dishCreated.getDescription());
		assertEquals(expectedDish.getPrice(), dishCreated.getPrice());
		assertEquals(expectedDish.getIdRestaurant(), dishCreated.getIdRestaurant());
		assertEquals(expectedDish.getImageUrl(), dishCreated.getImageUrl());
		assertNotNull(dishCreated.getIsAvailable());
		assertTrue(dishCreated.getIsAvailable());
		
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantNotFound() {
		Dish dishToCreate = validDish();
		
		when(restaurantRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(RestaurantNotFoundException.class, () -> createDishUseCase.execute(dishToCreate, 1L));
	}
	
	private Dish validDish() {
		return Dish.builder()
			.name("Pizza Margherita")
			.idCategory(10L)
			.description("Classic pizza with tomato sauce, mozzarella, and fresh basil.")
			.price(13_000L)
			.idRestaurant(20L)
			.imageUrl("https://example.com/images/pizza_margherita.jpg")
			.build();
	}
}
