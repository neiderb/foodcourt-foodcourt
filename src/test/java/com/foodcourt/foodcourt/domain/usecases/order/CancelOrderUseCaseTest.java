package com.foodcourt.foodcourt.domain.usecases.order;

import com.foodcourt.foodcourt.domain.exception.auth.InvalidUserException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderStatusException;
import com.foodcourt.foodcourt.domain.exception.order.OrderNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus;
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
class CancelOrderUseCaseTest {
	
	@InjectMocks
	private CancelOrderUseCase cancelOrderUseCase;
	
	@Mock
	private OrderRepositoryGateway orderRepositoryGateway;
	
	@Test
	void shouldCancelOrderSuccessfully() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.idClient(userClaims.id())
			.status(OrderStatus.PENDING)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		cancelOrderUseCase.execute(idOrder, userClaims);
		
		verify(orderRepositoryGateway).save(assertArg(orderArg -> {
			assertEquals(existingOrder.getId(), orderArg.getId());
			assertEquals(OrderStatus.CANCELLED, orderArg.getStatus());
		}));
	}
	
	@Test
	void shouldThrowExceptionWhenUserClaimsIsNull() {
		final Long idOrder = 1L;
		assertThrows(InvalidUserException.class, () -> cancelOrderUseCase.execute(idOrder, null));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIdIsNull() {
		UserClaims userClaims = mockClaims();
		assertAll(
			() -> assertThrows(InvalidOrderException.class, () -> cancelOrderUseCase.execute(null, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> cancelOrderUseCase.execute(0L, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> cancelOrderUseCase.execute(-5L, userClaims))
		);
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotClient() {
		final Long idOrder = 1L;
		final UserClaims userClaims = new UserClaims(
			10L,
			"test.example@mail.com",
			UserRole.EMPLOYEE,
			5L
		);
		assertThrows(InvalidUserException.class, () -> cancelOrderUseCase.execute(idOrder, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderNotFound() {
		final Long idOrder = 1L;
		UserClaims userClaims = mockClaims();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(OrderNotFoundException.class, () -> cancelOrderUseCase.execute(idOrder, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIsNotPending() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.idClient(userClaims.id())
			.status(OrderStatus.PROCESSING)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertThrows(InvalidOrderStatusException.class, () -> cancelOrderUseCase.execute(idOrder, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotOrderClient() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.idClient(99L)
			.status(OrderStatus.PENDING)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertThrows(InvalidUserException.class, () -> cancelOrderUseCase.execute(idOrder, userClaims));
	}
	
	private UserClaims mockClaims() {
		return new UserClaims(
			10L,
			"test.example@mail.com",
			UserRole.CLIENT,
			5L
		);
	}
	
}
