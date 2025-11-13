package com.foodcourt.foodcourt.domain.usecases.dish;

import com.foodcourt.foodcourt.domain.exception.InvalidPaginationFilterException;
import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllDishByRestaurantIdUseCaseTest {
	
	@InjectMocks
	private GetAllDishByRestaurantIdUseCase getAllDishByRestaurantIdUseCase;
	
	@Mock
	private DishRepositoryGateway dishRepositoryGateway;
	
	@Test
	void shouldReturnAllDishSummariesWhenPaginationFilterIsValid() {
		final int page = 0;
		final int size = 10;
		final long idCategory = 100L;
		final long idRestaurant = 1L;
		
		var filter = DishPaginationFilter.builder()
			.page(page)
			.size(size)
			.idCategory(idCategory)
			.build();
		var expectedResponse = PaginationResponse.<DishSummary>builder()
			.pageNumber(page)
			.pageSize(size)
			.totalElements(0L)
			.totalPages(0)
			.content(Collections.emptyList())
			.build();
		
		when(dishRepositoryGateway.getDishesSummaryByIdRestaurant(
			eq(idRestaurant),
			any(DishPaginationFilter.class))
		).thenReturn(expectedResponse);
		
		var actualResponse = getAllDishByRestaurantIdUseCase.execute(idRestaurant, filter);
		
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.getPageNumber(), actualResponse.getPageNumber());
		assertEquals(expectedResponse.getPageSize(), actualResponse.getPageSize());
		assertEquals(expectedResponse.getTotalElements(), actualResponse.getTotalElements());
		assertEquals(expectedResponse.getTotalPages(), actualResponse.getTotalPages());
		assertEquals(expectedResponse.getContent().size(), actualResponse.getContent().size());
		
		verify(dishRepositoryGateway).getDishesSummaryByIdRestaurant(
			assertArg(actualIdRestaurant -> assertEquals(idRestaurant, actualIdRestaurant)),
			assertArg(filterArg -> {
				assertEquals(page, filterArg.getPage());
				assertEquals(size, filterArg.getSize());
			})
		);
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantIdIsInvalid() {
		final DishPaginationFilter filter = DishPaginationFilter.builder()
			.page(0)
			.size(10)
			.build();
		
		assertAll(
			() -> assertThrows(InvalidRestaurantException.class,
				() -> getAllDishByRestaurantIdUseCase.execute(null, filter)),
			() -> assertThrows(InvalidRestaurantException.class,
				() -> getAllDishByRestaurantIdUseCase.execute(0L, filter)),
			() -> assertThrows(InvalidRestaurantException.class,
				() -> getAllDishByRestaurantIdUseCase.execute(-1L, filter))
		);
	}
	
	@Test
	void shouldThrowExceptionWhenPaginationFilterIsNull() {
		final long idRestaurant = 1L;
		assertThrows(InvalidPaginationFilterException.class, () -> getAllDishByRestaurantIdUseCase.execute(idRestaurant, null));
	}
	
}
