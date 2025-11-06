package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.CategoryData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.DishData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.CategoryJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.DishJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishRepositoryAdapterTest {
	
	@InjectMocks
	private DishRepositoryAdapter dishRepositoryAdapter;
	
	@Mock
	private DishJpaRepository dishJpaRepository;
	
	@Mock
	private CategoryJpaRepository categoryJpaRepository;
	
	private static final Long DISH_ID = 1L;
	private static final String DISH_NAME = "Pasta Primavera";
	private static final Long DISH_ID_CATEGORY = 2L;
	private static final String DISH_DESCRIPTION = "A classic Italian pasta dish with fresh vegetables.";
	private static final Long DISH_PRICE = 1500L;
	private static final Long DISH_ID_RESTAURANT = 3L;
	private static final String DISH_IMAGE_URL = "http://example.com/pasta.jpg";
	private static final Boolean DISH_IS_AVAILABLE = true;
	
	@Test
	void shouldSaveDishSuccessfully() {
		Dish dishToSave = validDishToCreate();
		DishData expectedDishData = DishData.builder()
			.id(DISH_ID)
			.name(DISH_NAME)
			.category(CategoryData.builder().id(DISH_ID_CATEGORY).build())
			.description(DISH_DESCRIPTION)
			.price(DISH_PRICE)
			.idRestaurant(DISH_ID_RESTAURANT)
			.imageUrl(DISH_IMAGE_URL)
			.isAvailable(DISH_IS_AVAILABLE)
			.build();
		
		when(categoryJpaRepository.existsById(DISH_ID_CATEGORY)).thenReturn(true);
		when(dishJpaRepository.save(any(DishData.class))).thenReturn(expectedDishData);
		
		Dish savedDish = dishRepositoryAdapter.save(dishToSave);
		
		assertNotNull(savedDish);
		assertEquals(expectedDishData.getId(), savedDish.getId());
		assertEquals(expectedDishData.getName(), savedDish.getName());
		assertEquals(expectedDishData.getCategory().getId(), savedDish.getIdCategory());
		assertEquals(expectedDishData.getDescription(), savedDish.getDescription());
		assertEquals(expectedDishData.getPrice(), savedDish.getPrice());
		assertEquals(expectedDishData.getIdRestaurant(), savedDish.getIdRestaurant());
		assertEquals(expectedDishData.getImageUrl(), savedDish.getImageUrl());
		assertEquals(expectedDishData.getIsAvailable(), savedDish.getIsAvailable());
	}
	
	@Test
	void shouldThrowExceptionWhenSavingDishWithInvalidCategory() {
		Dish dishToSave = validDishToCreate();
		
		when(categoryJpaRepository.existsById(any(Long.class))).thenReturn(false);
		
		assertThrows(TechnicalException.class, () -> dishRepositoryAdapter.save(dishToSave));
	}
	
	private Dish validDishToCreate() {
		return Dish.builder()
			.name(DISH_NAME)
			.idCategory(DISH_ID_CATEGORY)
			.description(DISH_DESCRIPTION)
			.price(DISH_PRICE)
			.idRestaurant(DISH_ID_RESTAURANT)
			.imageUrl(DISH_IMAGE_URL)
			.isAvailable(DISH_IS_AVAILABLE)
			.build();
	}
	
}
