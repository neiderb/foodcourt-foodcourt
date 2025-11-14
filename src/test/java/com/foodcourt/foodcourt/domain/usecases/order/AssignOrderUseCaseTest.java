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
class AssignOrderUseCaseTest {
	
	@InjectMocks
	private AssignOrderUseCase assignOrderUseCase;
	
	@Mock
	private OrderRepositoryGateway orderRepositoryGateway;
	
	@Test
	void shouldAssignOrderToEmployee() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		final OrderStatus status = OrderStatus.PENDING;
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(status)
			.build();
		Order orderToBeAssigned = Order.builder()
			.id(idOrder)
			.status(OrderStatus.PROCESSING)
			.idChef(userClaims.id())
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assignOrderUseCase.execute(idOrder, userClaims);
		
		verify(orderRepositoryGateway).save(assertArg(orderArg -> {
			assertEquals(orderToBeAssigned.getId(), orderArg.getId());
			assertEquals(orderToBeAssigned.getIdChef(), orderArg.getIdChef());
			assertEquals(orderToBeAssigned.getStatus(), orderArg.getStatus());
		}));
	}
	
	@Test
	void shouldThrowExceptionWhenUserClaimsIsNull() {
		final Long idOrder = 1L;
		assertThrows(InvalidUserException.class, () -> assignOrderUseCase.execute(idOrder, null));
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotEmployee() {
		final Long idOrder = 1L;
		UserClaims claims = new UserClaims(
			10L,
			"test-123@mail.com",
			UserRole.OWNER,
			null
		);
		
		assertThrows(InvalidUserException.class, () -> assignOrderUseCase.execute(idOrder, claims));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIdIsNull() {
		UserClaims userClaims = mockClaims();
		assertAll(
			() -> assertThrows(InvalidOrderException.class, () -> assignOrderUseCase.execute(null, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> assignOrderUseCase.execute(0L, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> assignOrderUseCase.execute(-5L, userClaims))
		);
	}
	
	@Test
	void shouldThrowExceptionWhenOrderNotFound() {
		final Long idOrder = 1L;
		UserClaims userClaims = mockClaims();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(OrderNotFoundException.class, () -> assignOrderUseCase.execute(idOrder, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIsNotPending() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(OrderStatus.CANCELLED)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertThrows(InvalidOrderStatusException.class, () -> assignOrderUseCase.execute(idOrder, userClaims));
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
