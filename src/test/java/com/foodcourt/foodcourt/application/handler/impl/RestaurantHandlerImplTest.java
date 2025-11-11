package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSortBy;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.GetAllRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.GetRestaurantByIdPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerImplTest {
	
	@InjectMocks
	private RestaurantHandlerImpl restaurantHandlerImpl;
	
	@Mock
	private CreateRestaurantPort createRestaurantPort;
	
	@Mock
	private GetRestaurantByIdPort getRestaurantByIdPort;
	
	@Mock
	private GetAllRestaurantPort getAllRestaurantPort;
	
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
	
	@Test
	void shouldReturnRestaurantWhenGetRestaurantById() {
		final Long idRestaurant = 20L;
		Restaurant expectedRestaurant = validRestaurant();
		expectedRestaurant.setId(idRestaurant);
		
		when(getRestaurantByIdPort.execute(any(Long.class))).thenReturn(expectedRestaurant);
		
		var actualResponse = restaurantHandlerImpl.getRestaurantById(idRestaurant);
		
		assertNotNull(actualResponse);
		assertEquals(idRestaurant, actualResponse.id());
		assertEquals(expectedRestaurant.getName(), actualResponse.name());
		assertEquals(expectedRestaurant.getNit(), actualResponse.nit());
		assertEquals(expectedRestaurant.getAddress(), actualResponse.address());
		assertEquals(expectedRestaurant.getPhoneNumber(), actualResponse.phoneNumber());
		assertEquals(expectedRestaurant.getUrlLogo(), actualResponse.urlLogo());
		assertEquals(expectedRestaurant.getOwnerId(), actualResponse.ownerId());
		
	}
	
	@Test
	void shouldReturnPaginationWhenGetAllRestaurants() {
		final int page = 0;
		final int size = 10;
		var expectedReq = new GetAllRestaurantRequest(
			page,
			size,
			"name",
			"asc"
		);
		
		var expectedResponse = PaginationResponse.<RestaurantSummary>builder()
			.pageSize(size)
			.pageNumber(page)
			.content(Collections.emptyList())
			.totalElements(0L)
			.totalPages(0)
			.build();
		
		when(getAllRestaurantPort.execute(any(RestaurantPaginationFilter.class)))
			.thenReturn(expectedResponse);
		
		var actualResponse = restaurantHandlerImpl.getAllRestaurants(expectedReq);
		
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.getPageNumber(), actualResponse.getPageNumber());
		assertEquals(expectedResponse.getPageSize(), actualResponse.getPageSize());
		assertEquals(expectedResponse.getTotalElements(), actualResponse.getTotalElements());
		assertEquals(expectedResponse.getTotalPages(), actualResponse.getTotalPages());
		assertEquals(expectedResponse.getContent().size(), actualResponse.getContent().size());
		
		verify(getAllRestaurantPort).execute(assertArg(filter -> {
			assertEquals(expectedReq.page(), filter.getPage());
			assertEquals(expectedReq.size(), filter.getSize());
			assertEquals(RestaurantSortBy.of(expectedReq.sortBy()).getValue(), filter.getSortBy());
			assertTrue(filter.isAscending());
		}));
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
