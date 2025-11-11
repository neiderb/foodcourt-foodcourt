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
import com.foodcourt.foodcourt.domain.ports.CreateOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
	
	@Override
	public Order execute(Order order) {
		validateOrder(order);
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
	}
	
	private void validateRestaurant(Long idRestaurant) {
		if (isNull(idRestaurant)) throw new InvalidOrderException(RESTAURANT_IS_REQUIRED);
		if (!restaurantRepositoryGateway.existById(idRestaurant))
			throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
	}
	
	private void validateItems(List<OrderDish> items, Long idRestaurant) {
		if (CollectionUtils.isEmpty(items)) throw new InvalidOrderException(ORDER_MUST_HAVE_AT_LEAST_ONE_ITEM);
		boolean allDishesAvailable = dishRepositoryGateway.existAllByIdsInAndRestaurantId(
			items.stream().map(item -> item.getDish().getId()).toList(),
			idRestaurant
		);
		if (!allDishesAvailable) throw new InvalidOrderException(DISH_NOT_AVAILABLE);
	}
	
}
