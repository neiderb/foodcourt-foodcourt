package com.foodcourt.foodcourt.domain.usecases.restaurant;

import com.foodcourt.foodcourt.domain.exception.InvalidPaginationFilterException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;
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
class GetAllRestaurantUseCaseTest {
	
	@InjectMocks
	private GetAllRestaurantUseCase getAllRestaurantUseCase;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Test
	void shouldReturnAllRestaurantSummariesWhenPaginationFilterIsValid() {
		final int page = 0;
		final int size = 10;
		RestaurantPaginationFilter filter = RestaurantPaginationFilter.builder()
			.page(page)
			.size(size)
			.build();
		var expectedResponse = PaginationResponse.<RestaurantSummary>builder()
			.pageNumber(page)
			.pageSize(size)
			.totalElements(0L)
			.totalPages(0)
			.content(Collections.emptyList())
			.build();
		
		when(restaurantRepositoryGateway.getAllRestaurantSummaries(any(RestaurantPaginationFilter.class)))
			.thenReturn(expectedResponse);
		
		var actualResponse = getAllRestaurantUseCase.execute(filter);
		
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.getPageNumber(), actualResponse.getPageNumber());
		assertEquals(expectedResponse.getPageSize(), actualResponse.getPageSize());
		assertEquals(expectedResponse.getTotalElements(), actualResponse.getTotalElements());
		assertEquals(expectedResponse.getTotalPages(), actualResponse.getTotalPages());
		assertEquals(expectedResponse.getContent().size(), actualResponse.getContent().size());
		
		verify(restaurantRepositoryGateway).getAllRestaurantSummaries(assertArg(filterArg -> {
			assertEquals(page, filterArg.getPage());
			assertEquals(size, filterArg.getSize());
		}));
	}
	
	@Test
	void shouldThrowExceptionWhenPaginationFilterIsNull() {
		assertThrows(InvalidPaginationFilterException.class, () -> getAllRestaurantUseCase.execute(null));
	}
	
	@Test
	void shouldThrowExceptionWhenPageIsNegative() {
		RestaurantPaginationFilter filter = RestaurantPaginationFilter.builder()
			.page(-1)
			.size(10)
			.build();
		
		assertThrows(InvalidPaginationFilterException.class, () -> getAllRestaurantUseCase.execute(filter));
	}
	
	@Test
	void shouldThrowExceptionWhenSizeIsNonPositive() {
		RestaurantPaginationFilter filter = RestaurantPaginationFilter.builder()
			.page(0)
			.size(0)
			.build();
		
		assertThrows(InvalidPaginationFilterException.class, () -> getAllRestaurantUseCase.execute(filter));
	}
	
}
