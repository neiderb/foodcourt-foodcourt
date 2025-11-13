package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.order.OrderNotFoundException;
import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;
import com.foodcourt.foodcourt.domain.model.order.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderByIdUseCaseTest {
	
	@InjectMocks
	private GetOrderByIdUseCase getOrderByIdUseCase;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Mock
	private OrderRepositoryGateway orderRepositoryGateway;
	
	@Test
	void shouldReturnOrderSuccessfully() {
		final Long idOrder = 1L;
		UserClaims userClaims = mockClaims();
		Order expectedOrder = Order.builder()
			.id(idOrder)
			.build();
		
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(expectedOrder);
		
		Order actualOrder = getOrderByIdUseCase.execute(idOrder, userClaims);
		
		assertNotNull(actualOrder);
		assertEquals(expectedOrder.getId(), actualOrder.getId());
		
		verify(restaurantRepositoryGateway).existsById(assertArg(argId -> assertEquals(userClaims.idRestaurant(), argId)));
		verify(orderRepositoryGateway).findById(assertArg(argId -> assertEquals(idOrder, argId)));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderNotFound() {
		final Long idOrder = 1L;
		UserClaims userClaims = mockClaims();
		
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(OrderNotFoundException.class, () -> getOrderByIdUseCase.execute(idOrder, userClaims));
		
		verify(restaurantRepositoryGateway).existsById(assertArg(argId -> assertEquals(userClaims.idRestaurant(), argId)));
		verify(orderRepositoryGateway).findById(assertArg(argId -> assertEquals(idOrder, argId)));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIdIsInvalid() {
		UserClaims userClaims = mockClaims();
		
		assertAll(
			() -> assertThrows(InvalidOrderException.class, () -> getOrderByIdUseCase.execute(null, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> getOrderByIdUseCase.execute(0L, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> getOrderByIdUseCase.execute(-1L, userClaims))
		);
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantIdIsInvalid() {
		final Long idOrder = 1L;
		UserClaims invalidUserClaims = new UserClaims(
			10L,
			"test.example@mail.com",
			UserRole.EMPLOYEE,
			null
		);
		
		assertThrows(InvalidRestaurantException.class, () -> getOrderByIdUseCase.execute(idOrder, invalidUserClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantNotFound() {
		final Long idOrder = 1L;
		UserClaims userClaims = mockClaims();
		
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(false);
		
		assertThrows(RestaurantNotFoundException.class, () -> getOrderByIdUseCase.execute(idOrder, userClaims));
		
		verify(restaurantRepositoryGateway).existsById(assertArg(argId -> assertEquals(userClaims.idRestaurant(), argId)));
	}
	
	private UserClaims mockClaims() {
		return new UserClaims(
			10L,
			"test.example@mail.com",
			UserRole.EMPLOYEE,
			5L
		);
	}
}
