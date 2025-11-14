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
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus.COMPLETED;
import static com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus.DELIVERED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliverOrderUseCaseTest {
	
	@InjectMocks
	private DeliverOrderUseCase deliverOrderUseCase;
	
	@Mock
	private OrderRepositoryGateway orderRepositoryGateway;
	
	private static final String VALID_SECURE_PIN = "123456";
	
	@Test
	void shouldDeliverOrderSuccessfully() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(COMPLETED)
			.idChef(userClaims.id())
			.securePin(VALID_SECURE_PIN)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		deliverOrderUseCase.execute(idOrder, VALID_SECURE_PIN, userClaims);
		
		verify(orderRepositoryGateway).save(assertArg(orderArg -> {
			assertEquals(idOrder, orderArg.getId());
			assertEquals(DELIVERED, orderArg.getStatus());
		}));
	}
	
	@Test
	void shouldThrowExceptionWhenUserClaimsIsNull() {
		final Long idOrder = 1L;
		assertThrows(InvalidUserException.class, () -> deliverOrderUseCase.execute(idOrder, VALID_SECURE_PIN, null));
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
		
		assertThrows(InvalidUserException.class, () -> deliverOrderUseCase.execute(idOrder, VALID_SECURE_PIN, claims));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIdIsNull() {
		UserClaims userClaims = mockClaims();
		assertAll(
			() -> assertThrows(InvalidOrderException.class, () -> deliverOrderUseCase.execute(null, VALID_SECURE_PIN, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> deliverOrderUseCase.execute(0L, VALID_SECURE_PIN, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> deliverOrderUseCase.execute(-5L, VALID_SECURE_PIN, userClaims))
		);
	}
	
	@Test
	void shouldThrowExceptionWhenOrderNotFound() {
		final Long idOrder = 1L;
		UserClaims userClaims = mockClaims();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(OrderNotFoundException.class, () -> deliverOrderUseCase.execute(idOrder, VALID_SECURE_PIN, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIsNotCompleted() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(OrderStatus.CANCELLED)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertThrows(InvalidOrderStatusException.class, () -> deliverOrderUseCase.execute(idOrder, VALID_SECURE_PIN, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotAssignedChef() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(COMPLETED)
			.idChef(99L)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertThrows(InvalidUserException.class, () -> deliverOrderUseCase.execute(idOrder, VALID_SECURE_PIN, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenSecurePinIsInvalid() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(COMPLETED)
			.idChef(userClaims.id())
			.securePin(VALID_SECURE_PIN)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertAll(
			() -> assertThrows(InvalidOrderException.class, () -> deliverOrderUseCase.execute(idOrder, "wrong-pin", userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> deliverOrderUseCase.execute(idOrder, null, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> deliverOrderUseCase.execute(idOrder, StringUtils.EMPTY, userClaims))
		);
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
