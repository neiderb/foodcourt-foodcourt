package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerImplTest {
	
	@InjectMocks
	private RestaurantHandlerImpl restaurantHandlerImpl;
	
	@Mock
	private CreateRestaurantPort createRestaurantPort;
	
	private static final Long RESTAURANT_ID = 1L;
	private static final String VALID_RESTAURANT_NAME = "Pizza Place";
	private static final String VALID_NIT = "1234567890";
	private static final String VALID_ADDRESS = "123 Main St";
	private static final String VALID_PHONE_NUMBER = "+573301234567";
	private static final String VALID_URL_LOGO = "http://logo.url/pizza.png";
	private static final Long VALID_OWNER_ID = 1L;
	
	@Test
	void shouldCreateRestaurantSuccessfully() {
		CreateRestaurantRequest validRequest = new CreateRestaurantRequest(
			VALID_RESTAURANT_NAME,
			VALID_NIT,
			VALID_ADDRESS,
			VALID_PHONE_NUMBER,
			VALID_URL_LOGO,
			VALID_OWNER_ID
		);
		
		CreateRestaurantResponse expectedResponse = new CreateRestaurantResponse(
			RESTAURANT_ID,
			VALID_RESTAURANT_NAME
		);
		
		when(createRestaurantPort.execute(any(Restaurant.class))).thenReturn(validRestaurant());
		
		CreateRestaurantResponse actualResponse = restaurantHandlerImpl.createRestaurant(validRequest);
		
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.id(), actualResponse.id());
		assertEquals(expectedResponse.name(), actualResponse.name());
		
	}
	
	private Restaurant validRestaurant() {
		return Restaurant.builder()
			.id(RESTAURANT_ID)
			.name(VALID_RESTAURANT_NAME)
			.nit(VALID_NIT)
			.address(VALID_ADDRESS)
			.phoneNumber(VALID_PHONE_NUMBER)
			.urlLogo(VALID_URL_LOGO)
			.ownerId(VALID_OWNER_ID)
			.build();
	}
	
}
