package com.foodcourt.foodcourt.domain.usecases;

import com.foodcourt.foodcourt.domain.exception.InvalidPaginationFilterException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;
import com.foodcourt.foodcourt.domain.ports.GetAllRestaurantPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.PaginationErrorMessage.FILTER_CANNOT_BE_NULL;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class GetAllRestaurantUseCase implements GetAllRestaurantPort {
	
	private final RestaurantRepositoryGateway restaurantRepositoryGateway;
	
	@Override
	public PaginationResponse<RestaurantSummary> execute(RestaurantPaginationFilter filter) {
		validatePaginationFilter(filter);
		return restaurantRepositoryGateway.getAllRestaurantSummaries(filter);
	}
	
	private void validatePaginationFilter(RestaurantPaginationFilter filter) {
		log.trace("Validating pagination filter");
		if (isNull(filter)) throw new InvalidPaginationFilterException(FILTER_CANNOT_BE_NULL);
		filter.validateFilter();
	}
	
}
