package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.order.InvalidClientException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderDish;
import com.foodcourt.foodcourt.domain.model.order.OrderStatus;
import com.foodcourt.foodcourt.domain.ports.CreateOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.foodcourt.foodcourt.domain.constants.OrderValidationMessage.*;
import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NOT_FOUND;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderPort {
	
	private final OrderRepositoryGateway orderRepositoryGateway;
	private final UserServiceGateway userServiceGateway;
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	private final DishRepositoryGateway dishRepositoryGateway;
	
	private final Set<OrderStatus> activeStatus = Set.of(
		OrderStatus.PENDING,
		OrderStatus.PROCESSING,
		OrderStatus.COMPLETED
	);
	
	@Override
	public Order execute(Order order) {
		validateOrder(order);
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);
		return orderRepositoryGateway.save(order);
	}
	
	private void validateOrder(Order order) {
		if (isNull(order)) throw new InvalidOrderException(INVALID_ORDER);
		validateClient(order.getIdClient());
		validateRestaurant(order.getIdRestaurant());
		validateItems(order.getItems(), order.getIdRestaurant());
	}
	
	private void validateClient(Long idClient) {
		if (isNull(idClient)) throw new InvalidClientException(INVALID_CLIENT);
		if (!userServiceGateway.isClient(idClient)) throw new InvalidClientException(INVALID_CLIENT);
		if (orderRepositoryGateway.existActiveOrderByClientId(idClient, activeStatus))
			throw new InvalidOrderException(CLIENT_HAS_ACTIVE_ORDER);
	}
	
	private void validateRestaurant(Long idRestaurant) {
		if (isNull(idRestaurant)) throw new InvalidOrderException(RESTAURANT_IS_REQUIRED);
		if (!restaurantRepositoryGateway.existById(idRestaurant))
			throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
	}
	
	private void validateItems(List<OrderDish> items, Long idRestaurant) {
		if (CollectionUtils.isEmpty(items)) throw new InvalidOrderException(ORDER_MUST_HAVE_AT_LEAST_ONE_ITEM);
		
		Set<Long> distinctDishIds = items.stream()
			.map(item -> item.getDish().getId())
			.collect(Collectors.toSet());
		
		if (distinctDishIds.size() != items.size()) {
			throw new InvalidOrderException(ORDER_CANNOT_HAVE_DUPLICATE_DISHES);
		}
		
		boolean invalidQuantity = items.stream()
			.anyMatch(item -> isNull(item.getQuantity()) || item.getQuantity() <= 0);
		if (invalidQuantity) throw new InvalidOrderException(ITEM_QUANTITY_MUST_BE_GREATER_THAN_ZERO);
		
		boolean allDishesAvailable = dishRepositoryGateway.existAllByIdsInAndRestaurantId(
			distinctDishIds,
			idRestaurant
		);
		if (!allDishesAvailable) throw new InvalidOrderException(DISH_NOT_AVAILABLE);
	}
	
}
