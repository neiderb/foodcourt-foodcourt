package com.foodcourt.foodcourt.domain.usecases.restaurant;

import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRestaurantByIdUseCaseTest {
	
	@InjectMocks
	private GetRestaurantByIdUseCase getRestaurantByIdUseCase;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Test
	void shouldGetRestaurantById() {
		final Long idRestaurant = 1L;
		Restaurant expectedRestaurant = Restaurant.builder()
			.id(idRestaurant)
			.build();
		
		when(restaurantRepositoryGateway.findById(any(Long.class))).thenReturn(expectedRestaurant);
		
		Restaurant actualRestaurant = getRestaurantByIdUseCase.execute(idRestaurant);
		
		assertNotNull(actualRestaurant);
		assertEquals(idRestaurant, actualRestaurant.getId());
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantNotFound() {
		final Long idRestaurant = 1L;
		
		when(restaurantRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(RestaurantNotFoundException.class, () -> getRestaurantByIdUseCase.execute(idRestaurant));
	}
	
}
