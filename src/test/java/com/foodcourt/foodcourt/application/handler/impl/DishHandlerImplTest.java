package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.domain.ports.CreateDishPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishHandlerImplTest {
	
	@InjectMocks
	private DishHandlerImpl dishHandlerImpl;
	
	@Mock
	private CreateDishPort createDishPort;
	
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
}
