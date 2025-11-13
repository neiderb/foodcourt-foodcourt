package com.foodcourt.foodcourt.domain.usecases.order;

import com.foodcourt.foodcourt.domain.exception.order.InvalidClientException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderDish;
import com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {
	
	@InjectMocks
	private CreateOrderUseCase createOrderUseCase;
	
	@Mock
	private OrderRepositoryGateway orderRepositoryGateway;
	
	@Mock
	private UserServiceGateway userServiceGateway;
	
	@Mock
	private RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Mock
	private DishRepositoryGateway dishRepositoryGateway;
	
	@Test
	void shouldCreateOrderSuccessfully() {
		final Long idOrderCreated = 1L;
		final OrderStatus expectedStatus = OrderStatus.PENDING;
		final LocalDateTime now = LocalDateTime.now();
		Order orderToCreate = validCreatingOrder();
		Order expectedOrder = validCreatingOrder();
		expectedOrder.setId(idOrderCreated);
		expectedOrder.setStatus(expectedStatus);
		expectedOrder.setOrderDate(now);
		
		when(userServiceGateway.isClient(any(Long.class))).thenReturn(true);
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		when(dishRepositoryGateway.existsAllByIdsInAndRestaurantId(
			any(),
			any(Long.class))
		).thenReturn(true);
		when(orderRepositoryGateway.existActiveOrderByClientId(
			any(Long.class),
			any()
		)).thenReturn(false);
		when(orderRepositoryGateway.save(any(Order.class))).thenReturn(expectedOrder);
		
		Order actualOrder = createOrderUseCase.execute(orderToCreate);
		
		assertNotNull(actualOrder);
		assertEquals(idOrderCreated, actualOrder.getId());
		assertEquals(expectedOrder.getIdClient(), actualOrder.getIdClient());
		assertEquals(now, actualOrder.getOrderDate());
		assertEquals(expectedStatus, actualOrder.getStatus());
		assertNull(actualOrder.getIdChef());
		assertEquals(expectedOrder.getIdRestaurant(), actualOrder.getIdRestaurant());
		assertEquals(expectedOrder.getItems().size(), actualOrder.getItems().size());
	}
	
	@Test
	void shouldThrowExceptionWhenCreatingNullOrder() {
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(null));
	}
	
	@Test
	void shouldThrowExceptionWhenClientIdIsInvalid() {
		Order orderToCreate = validCreatingOrder();
		orderToCreate.setIdClient(null);
		assertThrows(InvalidClientException.class, () -> createOrderUseCase.execute(orderToCreate));
		
		orderToCreate.setIdClient(0L);
		assertThrows(InvalidClientException.class, () -> createOrderUseCase.execute(orderToCreate));
		
		orderToCreate.setIdClient(-5L);
		assertThrows(InvalidClientException.class, () -> createOrderUseCase.execute(orderToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenClientHaveActiveOrder() {
		Order orderToCreate = validCreatingOrder();
		when(userServiceGateway.isClient(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.existActiveOrderByClientId(
			any(Long.class),
			any()
		)).thenReturn(true);
		
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(orderToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantDoesNotExist() {
		Order orderToCreate = validCreatingOrder();
		when(userServiceGateway.isClient(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.existActiveOrderByClientId(
			any(Long.class),
			any()
		)).thenReturn(false);
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(false);
		
		assertThrows(RestaurantNotFoundException.class, () -> createOrderUseCase.execute(orderToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenOrderHasNoItems() {
		Order orderToCreate = validCreatingOrder();
		orderToCreate.setItems(List.of());
		when(userServiceGateway.isClient(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.existActiveOrderByClientId(
			any(Long.class),
			any()
		)).thenReturn(false);
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(orderToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenItemIsDuplicated() {
		Order orderToCreate = validCreatingOrder();
		orderToCreate.setItems(List.of(
			OrderDish.builder()
				.dish(Dish.builder().id(20L).build())
				.quantity(2)
				.build(),
			OrderDish.builder()
				.dish(Dish.builder().id(20L).build())
				.quantity(3)
				.build()
		));
		when(userServiceGateway.isClient(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.existActiveOrderByClientId(
			any(Long.class),
			any()
		)).thenReturn(false);
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(orderToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenDishesDoNotBelongToRestaurant() {
		Order orderToCreate = validCreatingOrder();
		when(userServiceGateway.isClient(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.existActiveOrderByClientId(
			any(Long.class),
			any()
		)).thenReturn(false);
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		when(dishRepositoryGateway.existsAllByIdsInAndRestaurantId(
			any(),
			any(Long.class))
		).thenReturn(false);
		
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(orderToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenItemHasInvalidQuantity() {
		Order orderToCreate = validCreatingOrder();
		orderToCreate.setItems(List.of(
			OrderDish.builder()
				.dish(Dish.builder().id(20L).build())
				.quantity(0)
				.build(),
			OrderDish.builder()
				.dish(Dish.builder().id(30L).build())
				.quantity(3)
				.build()
		));
		when(userServiceGateway.isClient(any(Long.class))).thenReturn(true);
		when(orderRepositoryGateway.existActiveOrderByClientId(
			any(Long.class),
			any()
		)).thenReturn(false);
		when(restaurantRepositoryGateway.existsById(any(Long.class))).thenReturn(true);
		
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(orderToCreate));
		
		orderToCreate.getItems().getFirst().setQuantity(-2);
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(orderToCreate));
		
		orderToCreate.getItems().getFirst().setQuantity(null);
		assertThrows(InvalidOrderException.class, () -> createOrderUseCase.execute(orderToCreate));
	}
	
	private Order validCreatingOrder() {
		return Order.builder()
			.idRestaurant(10L)
			.idClient(100L)
			.items(List.of(
				OrderDish.builder()
					.dish(Dish.builder().id(20L).build())
					.quantity(2)
					.build(),
				OrderDish.builder()
					.dish(Dish.builder().id(30L).build())
					.quantity(3)
					.build()
			))
			.build();
	}
	
}
