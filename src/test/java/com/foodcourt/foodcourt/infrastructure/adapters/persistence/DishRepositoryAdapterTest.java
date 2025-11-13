package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.dto.DishFilter;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.CategoryData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.DishData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.CategoryJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.DishJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.PaginationFilterMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.DishSummaryProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.foodcourt.foodcourt.domain.model.enums.SortDirection.DESC;
import static com.foodcourt.foodcourt.domain.model.dish.enums.DishSortBy.NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishRepositoryAdapterTest {
	
	@InjectMocks
	private DishRepositoryAdapter dishRepositoryAdapter;
	
	@Mock
	private DishJpaRepository dishJpaRepository;
	
	@Mock
	private CategoryJpaRepository categoryJpaRepository;
	
	@Mock
	private PaginationFilterMapper paginationFilterMapper;
	
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
		
		when(categoryJpaRepository.existsById(any(Long.class))).thenReturn(true);
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
		
		verify(categoryJpaRepository).existsById(assertArg(idCategory -> assertEquals(DISH_ID_CATEGORY, idCategory)));
	}
	
	@Test
	void shouldThrowExceptionWhenSavingDishWithInvalidCategory() {
		Dish dishToSave = validDishToCreate();
		
		when(categoryJpaRepository.existsById(any(Long.class))).thenReturn(false);
		
		assertThrows(TechnicalException.class, () -> dishRepositoryAdapter.save(dishToSave));
	}
	
	@Test
	void shouldFindDishByIdSuccessfully() {
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
		
		when(dishJpaRepository.findById(any(Long.class))).thenReturn(Optional.of(expectedDishData));
		
		Dish foundDish = dishRepositoryAdapter.findById(DISH_ID);
		
		assertNotNull(foundDish);
		assertEquals(expectedDishData.getId(), foundDish.getId());
		assertEquals(expectedDishData.getName(), foundDish.getName());
		assertEquals(expectedDishData.getCategory().getId(), foundDish.getIdCategory());
		assertEquals(expectedDishData.getDescription(), foundDish.getDescription());
		assertEquals(expectedDishData.getPrice(), foundDish.getPrice());
		assertEquals(expectedDishData.getIdRestaurant(), foundDish.getIdRestaurant());
		assertEquals(expectedDishData.getImageUrl(), foundDish.getImageUrl());
		assertEquals(expectedDishData.getIsAvailable(), foundDish.getIsAvailable());
		
		verify(dishJpaRepository).findById(assertArg(idDish -> assertEquals(DISH_ID, idDish)));
	}
	
	@Test
	void shouldReturnNullWhenDishNotFoundById() {
		when(dishJpaRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		
		Dish foundDish = dishRepositoryAdapter.findById(DISH_ID);
		
		assertNull(foundDish);
		
		verify(dishJpaRepository).findById(assertArg(idDish -> assertEquals(DISH_ID, idDish)));
	}
	
	@Test
	void shouldReturnPaginatedDishSummaries() {
		final Long idRestaurant = 100L;
		List<DishSummaryProjection> projections = mockSummaryResults();
		final Long idFirst = projections.getFirst().getId();
		final String nameFirst = projections.getFirst().getName();
		final String url = projections.getFirst().getImageUrl();
		DishPaginationFilter filter = DishPaginationFilter.builder()
			.page(0)
			.size(10)
			.idCategory(null)
			.sortDirection("desc")
			.build();
		var dishFilter = new DishFilter(idRestaurant, 0L);
		
		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
		
		PageImpl<DishSummaryProjection> page = new PageImpl<>(projections, pageable, projections.size());
		when(dishJpaRepository.findDishPaginatedBy(any(Pageable.class), any(DishFilter.class))).thenReturn(page);
		when(paginationFilterMapper.toPageable(any(DishPaginationFilter.class))).thenReturn(pageable);
		
		var response = dishRepositoryAdapter.getDishesSummaryByIdRestaurant(idRestaurant, filter);
		
		assertNotNull(response);
		assertEquals(page.getNumber(), response.getPageNumber());
		assertEquals(page.getSize(), response.getPageSize());
		assertEquals(page.getTotalElements(), response.getTotalElements());
		assertEquals(page.getTotalPages(), response.getTotalPages());
		assertEquals(projections.size(), response.getContent().size());
		assertEquals(idFirst, response.getContent().getFirst().getId());
		assertEquals(nameFirst, response.getContent().getFirst().getName());
		assertEquals(url, response.getContent().getFirst().getImageUrl());
		
		verify(paginationFilterMapper).toPageable(assertArg(filterArg -> {
			assertEquals(NAME.getValue(), filterArg.getSortBy());
			assertEquals(DESC, filterArg.getSortDirection());
		}));
		verify(dishJpaRepository).findDishPaginatedBy(
			any(Pageable.class),
			assertArg(filterArg -> {
				assertEquals(dishFilter.getIdRestaurant(), filterArg.getIdRestaurant());
				assertEquals(dishFilter.getIdCategory(), filterArg.getIdCategory());
			})
		);
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
	
	private List<DishSummaryProjection> mockSummaryResults() {
		DishSummaryProjection p1 = mock(DishSummaryProjection.class);
		when(p1.getId()).thenReturn(1L);
		when(p1.getName()).thenReturn("Dish 1");
		when(p1.getPrice()).thenReturn(1000L);
		when(p1.getCategoryName()).thenReturn("Category 1");
		when(p1.getImageUrl()).thenReturn("http://example.com/dish1.jpg");
		
		DishSummaryProjection p2 = mock(DishSummaryProjection.class);
		when(p2.getId()).thenReturn(2L);
		when(p2.getName()).thenReturn("Dish 2");
		when(p2.getPrice()).thenReturn(2000L);
		when(p2.getCategoryName()).thenReturn("Category 2");
		when(p2.getImageUrl()).thenReturn("http://example.com/dish2.jpg");
		
		return List.of(p1, p2);
	}
	
}
