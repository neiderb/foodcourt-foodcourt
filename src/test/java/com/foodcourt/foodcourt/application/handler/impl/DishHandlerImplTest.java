package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllDishByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.request.UpdateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.domain.model.dish.enums.DishSortBy;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;
import com.foodcourt.foodcourt.domain.ports.dish.CreateDishPort;
import com.foodcourt.foodcourt.domain.ports.dish.GetAllDishByRestaurantIdPort;
import com.foodcourt.foodcourt.domain.ports.dish.ToggleDishAvailabilityPort;
import com.foodcourt.foodcourt.domain.ports.dish.UpdateDishPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishHandlerImplTest {
	
	@InjectMocks
	private DishHandlerImpl dishHandlerImpl;
	
	@Mock
	private CreateDishPort createDishPort;
	
	@Mock
	private UpdateDishPort updateDishPort;
	
	@Mock
	private ToggleDishAvailabilityPort toggleDishAvailabilityPort;
	
	@Mock
	private GetAllDishByRestaurantIdPort getAllDishByRestaurantIdPort;
	
	@BeforeEach
	void setUp() {
		initSecurityContext();
	}
	
	@Test
	void shouldCreateDishSuccessfully() {
		final Long expectedDishId = 2L;
		final String expectedDishName = "Dish Name";
		final Long expectedIdCategory = 3L;
		final String expectedDescription = "Delicious dish description";
		final Long expectedPrice = 1500L;
		final Long expectedIdRestaurant = 4L;
		final String expectedImageUrl = "http://image.url/dish.jpg";
		
		CreateDishRequest req = new CreateDishRequest(
			expectedDishName,
			expectedIdCategory,
			expectedDescription,
			expectedPrice,
			expectedIdRestaurant,
			expectedImageUrl,
			null
		);
		
		Dish expectedDish = Dish.builder()
			.id(expectedDishId)
			.name(expectedDishName)
			.idCategory(expectedIdCategory)
			.description(expectedDescription)
			.price(expectedPrice)
			.idRestaurant(expectedIdRestaurant)
			.imageUrl(expectedImageUrl)
			.isAvailable(true)
			.build();
		
		when(createDishPort.execute(any(Dish.class), any(Long.class))).thenReturn(expectedDish);
		
		CreateDishResponse response = dishHandlerImpl.createDish(req);
		
		assertNotNull(response);
		assertEquals(expectedDish.getId(), response.id());
		assertEquals(expectedDish.getName(), response.name());
	}
	
	@Test
	void shouldUpdateDishSuccessfully() {
		final Long dishIdToUpdate = 5L;
		final Long updatedPrice = 2000L;
		final String updatedDescription = "Updated delicious dish description";
		
		UpdateDishRequest updateRequest = new UpdateDishRequest(
			dishIdToUpdate,
			updatedPrice,
			updatedDescription
		);
		
		dishHandlerImpl.updateDish(updateRequest);
		
		verify(updateDishPort).execute(assertArg(dish -> {
			assertEquals(dishIdToUpdate, dish.getId());
			assertEquals(updatedPrice, dish.getPrice());
			assertEquals(updatedDescription, dish.getDescription());
		}), any(Long.class));
	}
	
	@Test
	void shouldToggleDishAvailabilitySuccessfully() {
		final Long dishIdToToggle = 7L;
		dishHandlerImpl.toggleDishAvailability(dishIdToToggle);
		verify(toggleDishAvailabilityPort).execute(assertArg(id -> assertEquals(dishIdToToggle, id)), any(Long.class));
	}
	
	@Test
	void shouldReturnPaginationWhenGetAllDishesByRestaurantId() {
		final Long restaurantId = 10L;
		final int page = 0;
		final int size = 10;
		var testReq = new GetAllDishByRestaurantIdRequest(
			page,
			size,
			"name",
			"asc",
			null
		);
		var expectedResponse = PaginationResponse.<DishSummary>builder()
			.pageSize(size)
			.pageNumber(page)
			.content(Collections.emptyList())
			.totalElements(0L)
			.totalPages(0)
			.build();
		
		when(getAllDishByRestaurantIdPort.execute(any(Long.class), any(DishPaginationFilter.class)))
			.thenReturn(expectedResponse);
		
		var actualResponse = dishHandlerImpl.getDishesByIdRestaurant(restaurantId, testReq);
		
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.getPageNumber(), actualResponse.getPageNumber());
		assertEquals(expectedResponse.getPageSize(), actualResponse.getPageSize());
		assertEquals(expectedResponse.getTotalElements(), actualResponse.getTotalElements());
		assertEquals(expectedResponse.getTotalPages(), actualResponse.getTotalPages());
		assertEquals(expectedResponse.getContent().size(), actualResponse.getContent().size());
		
		verify(getAllDishByRestaurantIdPort).execute(
			assertArg(idRestaurant -> assertEquals(restaurantId, idRestaurant)),
			assertArg(filter -> {
				assertEquals(expectedResponse.getPageNumber(), filter.getPage());
				assertEquals(expectedResponse.getPageSize(), filter.getSize());
				assertEquals(DishSortBy.of(testReq.sortBy()).getValue(), filter.getSortBy());
				assertTrue(filter.isAscending());
				assertNull(filter.getIdCategory());
			})
		);
	}
	
	private void initSecurityContext() {
		UserClaims userClaims = new UserClaims(
			999L,
			"test.user@mail.com",
			UserRole.OWNER,
			null
		);
		Authentication authentication = mock(Authentication.class);
		lenient().when(authentication.getPrincipal()).thenReturn(userClaims);
		
		SecurityContext securityContext = mock(SecurityContext.class);
		lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
		
		SecurityContextHolder.setContext(securityContext);
	}
	
}
