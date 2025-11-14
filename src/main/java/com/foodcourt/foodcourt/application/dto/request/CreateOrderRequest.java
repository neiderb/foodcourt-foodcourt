package com.foodcourt.foodcourt.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

import static com.foodcourt.foodcourt.domain.constants.OrderValidationMessage.*;

public record CreateOrderRequest(
	@NotNull(message = RESTAURANT_IS_REQUIRED)
	@Positive(message = INVALID_RESTAURANT_ID)
	Long idRestaurant,
	
	@Valid
	@NotEmpty(message = ORDER_MUST_HAVE_AT_LEAST_ONE_ITEM)
	List<CreateOrderDishRequest> items
) {}
