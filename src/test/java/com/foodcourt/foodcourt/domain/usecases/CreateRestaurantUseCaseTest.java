package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidRoleException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {
	
	@InjectMocks
	private CreateRestaurantUseCase createRestaurantUseCase;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Mock
	private UserServiceGateway userServiceGateway;
	
	@Test
	void shouldCreateRestaurantSuccessfully() {
		Restaurant restaurantToCreate = validRestaurant();
		Restaurant expectedRestaurant = validRestaurant();
		expectedRestaurant.setId(1L);
		
		when(userServiceGateway.isOwner(restaurantToCreate.getOwnerId())).thenReturn(true);
		when(restaurantRepositoryGateway.save(restaurantToCreate)).thenReturn(expectedRestaurant);
		
		Restaurant restaurantCreated = createRestaurantUseCase.execute(restaurantToCreate);
		
		assertEquals(expectedRestaurant.getName(), restaurantCreated.getName());
		assertEquals(expectedRestaurant.getNit(), restaurantCreated.getNit());
		assertEquals(expectedRestaurant.getAddress(), restaurantCreated.getAddress());
		assertEquals(expectedRestaurant.getPhoneNumber(), restaurantCreated.getPhoneNumber());
		assertEquals(expectedRestaurant.getUrlLogo(), restaurantCreated.getUrlLogo());
		assertEquals(expectedRestaurant.getOwnerId(), restaurantCreated.getOwnerId());
	}
	
	@Test
	void shouldThrowExceptionWhenNameIsOnlyNumbers() {
		Restaurant restaurantToCreate = validRestaurant();
		restaurantToCreate.setName("123456");
		
		assertThrows(InvalidRestaurantException.class, () -> createRestaurantUseCase.execute(restaurantToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenOwnerIsNotValid() {
		Restaurant restaurantToCreate = validRestaurant();
		
		when(userServiceGateway.isOwner(restaurantToCreate.getOwnerId())).thenReturn(false);
		
		assertThrows(InvalidRoleException.class, () -> createRestaurantUseCase.execute(restaurantToCreate));
	}
	
	private Restaurant validRestaurant() {
		return Restaurant.builder()
			.name("Gourmet Bites")
			.nit("99556633")
			.address("123 Food St, Flavor Town")
			.phoneNumber("+1234567890")
			.urlLogo("http://example.com/logo.png")
			.ownerId(1L)
			.build();
	}
	
}
