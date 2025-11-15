package com.foodcourt.foodcourt.domain.usecases.order;

import com.foodcourt.foodcourt.domain.exception.auth.InvalidUserException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderStatusException;
import com.foodcourt.foodcourt.domain.exception.order.OrderNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.NotificationServiceGateway;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.auth.User;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteOrderUseCaseTest {
	
	@InjectMocks
	private CompleteOrderUseCase completeOrderUseCase;
	
	@Mock
	private OrderRepositoryGateway orderRepositoryGateway;
	
	@Mock
	private UserServiceGateway userServiceGateway;
	
	@Mock
	private NotificationServiceGateway notificationServiceGateway;
	
	private static final String PHONE_NUMBER = "+1234567890";
	
	@Test
	void shouldCompleteOrderSuccessfully() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		final OrderStatus status = OrderStatus.PROCESSING;
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(status)
			.idClient(20L)
			.idChef(userClaims.id())
			.build();
		Order orderToBeCompleted = Order.builder()
			.id(idOrder)
			.status(OrderStatus.COMPLETED)
			.idClient(20L)
			.idChef(userClaims.id())
			.build();
		User user = User.builder()
			.id(existingOrder.getIdClient())
			.phone("4444444444")
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		when(userServiceGateway.getUserById(any(Long.class))).thenReturn(user);
		
		completeOrderUseCase.execute(idOrder, userClaims);
		
		verify(orderRepositoryGateway).findById(idOrder);
		verify(userServiceGateway).getUserById(existingOrder.getIdClient());
		verify(orderRepositoryGateway).save(assertArg(orderArg -> {
			assertEquals(orderToBeCompleted.getId(), orderArg.getId());
			assertEquals(orderToBeCompleted.getStatus(), orderArg.getStatus());
			assertEquals(orderToBeCompleted.getIdClient(), orderArg.getIdClient());
			assertEquals(orderToBeCompleted.getIdChef(), orderArg.getIdChef());
			assertNotNull(orderArg.getSecurePin());
		}));
		verify(notificationServiceGateway).sendOrderCompletedNotification(
			eq(PHONE_NUMBER),
			assertArg(pinArg -> assertTrue(StringUtils.hasText(pinArg)))
		);
	}
	
	@Test
	void shouldThrowExceptionWhenUserClaimsIsNull() {
		final Long idOrder = 1L;
		assertThrows(InvalidUserException.class, () -> completeOrderUseCase.execute(idOrder, null));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIdIsNull() {
		UserClaims userClaims = mockClaims();
		assertAll(
			() -> assertThrows(InvalidOrderException.class, () -> completeOrderUseCase.execute(null, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> completeOrderUseCase.execute(0L, userClaims)),
			() -> assertThrows(InvalidOrderException.class, () -> completeOrderUseCase.execute(-5L, userClaims))
		);
	}
	
	@Test
	void shouldThrowExceptionWhenOrderNotFound() {
		final Long idOrder = 1L;
		UserClaims userClaims = mockClaims();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(null);
		
		assertThrows(OrderNotFoundException.class, () -> completeOrderUseCase.execute(idOrder, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderIsNotProcessing() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(OrderStatus.PENDING)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertThrows(InvalidOrderStatusException.class, () -> completeOrderUseCase.execute(idOrder, userClaims));
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotAssignedChef() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(OrderStatus.PROCESSING)
			.idChef(99L)
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		
		assertThrows(InvalidUserException.class, () -> completeOrderUseCase.execute(idOrder, userClaims));
	}
	
	@Test
	void shouldPropagateExceptionWhenNotificationFails() {
		final Long idOrder = 1L;
		final UserClaims userClaims = mockClaims();
		final OrderStatus status = OrderStatus.PROCESSING;
		Order existingOrder = Order.builder()
			.id(idOrder)
			.status(status)
			.idClient(20L)
			.idChef(userClaims.id())
			.build();
		User user = User.builder()
			.id(existingOrder.getIdClient())
			.phone("4444444444")
			.build();
		
		when(orderRepositoryGateway.findById(any(Long.class))).thenReturn(existingOrder);
		when(userServiceGateway.getUserById(any(Long.class))).thenReturn(user);
		doThrow(new RuntimeException("Notification service error"))
			.when(notificationServiceGateway)
			.sendOrderCompletedNotification(anyString(), anyString());
		
		assertThrows(RuntimeException.class, () -> completeOrderUseCase.execute(idOrder, userClaims));
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
