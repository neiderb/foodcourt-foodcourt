package com.foodcourt.foodcourt.domain.usecases.order;

import com.foodcourt.foodcourt.domain.exception.InvalidPaginationFilterException;
import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.order.OrderPaginationFilter;
import com.foodcourt.foodcourt.domain.model.order.OrderSummary;
import com.foodcourt.foodcourt.domain.ports.order.GetAllOrderByRestaurantIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.PaginationErrorMessage.FILTER_CANNOT_BE_NULL;
import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.RestaurantValidationMessage.INVALID_RESTAURANT_ID;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class GetAllOrderByRestaurantIdUseCase implements GetAllOrderByRestaurantIdPort {
	
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	private final OrderRepositoryGateway orderRepositoryGateway;
	
	@Override
	public PaginationResponse<OrderSummary> execute(Long idRestaurant, OrderPaginationFilter filter) {
		validateRestaurantId(idRestaurant);
		validatePaginationFilter(filter);
		return orderRepositoryGateway.findAllByRestaurantId(idRestaurant, filter);
	}
	
	private void validateRestaurantId(Long idRestaurant) {
		log.trace("Validating restaurant ID: {}", idRestaurant);
		if (isNull(idRestaurant) || idRestaurant <= 0)
			throw new InvalidRestaurantException(INVALID_RESTAURANT_ID);
		if (!restaurantRepositoryGateway.existsById(idRestaurant))
			throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
	}
	
	private void validatePaginationFilter(OrderPaginationFilter filter) {
		log.trace("Validating pagination filter");
		if (isNull(filter)) throw new InvalidPaginationFilterException(FILTER_CANNOT_BE_NULL);
		filter.validateFilter();
	}
	
}
