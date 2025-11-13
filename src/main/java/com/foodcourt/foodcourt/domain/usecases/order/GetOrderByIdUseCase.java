package com.foodcourt.foodcourt.domain.usecases.order;

import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.order.OrderNotFoundException;
import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.exception.auth.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.ports.order.GetOrderByIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.OrderErrorMessage.ORDER_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.OrderValidationMessage.INVALID_ORDER_ID;
import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.RestaurantValidationMessage.INVALID_RESTAURANT_ID;
import static com.foodcourt.foodcourt.domain.constants.AuthErrorMessage.UNAUTHORIZED_ACTION;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class GetOrderByIdUseCase implements GetOrderByIdPort {
	
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	private final OrderRepositoryGateway orderRepositoryGateway;
	
	@Override
	public Order execute(Long idOrder, UserClaims userClaims) {
		validateIdOrder(idOrder);
		validateRestaurantEmployee(userClaims);
		
		Order existingOrder = orderRepositoryGateway.findById(idOrder);
		if (isNull(existingOrder)) {
			throw new OrderNotFoundException(ORDER_NOT_FOUND);
		}
		return existingOrder;
	}
	
	private void validateIdOrder(Long idOrder) {
		log.trace("Validating order ID: {}", idOrder);
		if (isNull(idOrder) || idOrder <= 0)
			throw new InvalidOrderException(INVALID_ORDER_ID);
	}
	
	private void validateRestaurantEmployee(UserClaims userClaims) {
		Long idRestaurant = getIdRestaurant(userClaims);
		if (isNull(idRestaurant) || idRestaurant <= 0) throw new InvalidRestaurantException(INVALID_RESTAURANT_ID);
		if (!restaurantRepositoryGateway.existsById(idRestaurant))
			throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
	}
	
	private Long getIdRestaurant(UserClaims userClaims) {
		if (isNull(userClaims)) throw new InvalidUserException(UNAUTHORIZED_ACTION);
		return userClaims.idRestaurant();
	}
	
}
