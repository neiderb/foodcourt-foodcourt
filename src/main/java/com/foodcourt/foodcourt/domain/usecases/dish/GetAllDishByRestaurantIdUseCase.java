package com.foodcourt.foodcourt.domain.usecases.dish;

import com.foodcourt.foodcourt.domain.exception.InvalidPaginationFilterException;
import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;
import com.foodcourt.foodcourt.domain.ports.dish.GetAllDishByRestaurantIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.PaginationErrorMessage.FILTER_CANNOT_BE_NULL;
import static com.foodcourt.foodcourt.domain.constants.RestaurantValidationMessage.INVALID_RESTAURANT_ID;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class GetAllDishByRestaurantIdUseCase implements GetAllDishByRestaurantIdPort {
	
	private final DishRepositoryGateway dishRepositoryGateway;
	
	@Override
	public PaginationResponse<DishSummary> execute(Long idRestaurant, DishPaginationFilter filter) {
		validateRestaurantId(idRestaurant);
		validatePaginationFilter(filter);
		return dishRepositoryGateway.getDishesSummaryByIdRestaurant(idRestaurant, filter);
	}
	
	private void validateRestaurantId(Long idRestaurant) {
		log.trace("Validating restaurant ID: {}", idRestaurant);
		if (isNull(idRestaurant) || idRestaurant <= 0)
			throw new InvalidRestaurantException(INVALID_RESTAURANT_ID);
	}
	
	private void validatePaginationFilter(DishPaginationFilter filter) {
		log.trace("Validating pagination filter");
		if (isNull(filter)) throw new InvalidPaginationFilterException(FILTER_CANNOT_BE_NULL);
		filter.validateFilter();
	}
	
}
