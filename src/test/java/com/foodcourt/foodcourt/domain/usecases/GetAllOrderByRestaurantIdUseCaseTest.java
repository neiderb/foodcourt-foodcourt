package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.InvalidPaginationFilterException;
import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.order.OrderPaginationFilter;
import com.foodcourt.foodcourt.domain.model.order.OrderSummary;
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
class GetAllOrderByRestaurantIdUseCaseTest {
	
	@InjectMocks
	private GetAllOrderByRestaurantIdUseCase getAllOrderByRestaurantIdUseCase;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Mock
	private OrderRepositoryGateway orderRepositoryGateway;
	
	@Test
	void shouldReturnAllOrderSummariesWhenPaginationFilterIsValid() {
		final int page = 0;
		final int size = 10;
		final long idRestaurant = 1L;
		
		var filter = OrderPaginationFilter.builder()
			.page(page)
			.size(size)
			.build();
		var expectedResponse = PaginationResponse.<OrderSummary>builder()
			.pageNumber(page)
			.pageSize(size)
			.totalElements(0L)
			.totalPages(0)
			.content(Collections.emptyList())
			.build();
		
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.findAllByRestaurantId(
			eq(idRestaurant),
			any(OrderPaginationFilter.class))
		).thenReturn(expectedResponse);
		
		var actualResponse = getAllOrderByRestaurantIdUseCase.execute(idRestaurant, filter);
		
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.getPageNumber(), actualResponse.getPageNumber());
		assertEquals(expectedResponse.getPageSize(), actualResponse.getPageSize());
		assertEquals(expectedResponse.getTotalElements(), actualResponse.getTotalElements());
		assertEquals(expectedResponse.getTotalPages(), actualResponse.getTotalPages());
		assertEquals(expectedResponse.getContent().size(), actualResponse.getContent().size());
		
		verify(orderRepositoryGateway).findAllByRestaurantId(
			assertArg(actualIdRestaurant -> assertEquals(idRestaurant, actualIdRestaurant)),
			assertArg(filterArg -> {
				assertEquals(page, filterArg.getPage());
				assertEquals(size, filterArg.getSize());
			})
		);
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantIdIsInvalid() {
		final OrderPaginationFilter filter = OrderPaginationFilter.builder()
			.page(0)
			.size(10)
			.build();
		
		assertAll(
			() -> assertThrows(InvalidRestaurantException.class,
				() -> getAllOrderByRestaurantIdUseCase.execute(null, filter)
			),
			() -> assertThrows(InvalidRestaurantException.class,
				() -> getAllOrderByRestaurantIdUseCase.execute(-1L, filter)
			),
			() -> assertThrows(InvalidRestaurantException.class,
				() -> getAllOrderByRestaurantIdUseCase.execute(0L, filter)
			)
		);
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantDoesNotExist() {
		final long idRestaurant = 1L;
		final OrderPaginationFilter filter = OrderPaginationFilter.builder()
			.page(0)
			.size(10)
			.build();
		
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(false);
		
		assertThrows(RestaurantNotFoundException.class,
			() -> getAllOrderByRestaurantIdUseCase.execute(idRestaurant, filter)
		);
	}
	
	@Test
	void shouldThrowExceptionWhenPaginationFilterIsNull() {
		final long idRestaurant = 1L;
		
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		
		assertThrows(
			InvalidPaginationFilterException.class,
			() -> getAllOrderByRestaurantIdUseCase.execute(idRestaurant, null)
		);
	}
	
}
