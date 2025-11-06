package com.foodcourt.foodcourt.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.foodcourt.foodcourt.domain.constants.DishValidationMessage.*;

public record CreateDishRequest(
	@NotNull(message = NAME_REQUIRED)
	@NotBlank(message = NAME_REQUIRED)
	String name,
	
	@NotNull(message = CATEGORY_ID_REQUIRED)
	@Positive(message = CATEGORY_ID_MUST_BE_MORE_THAN_ZERO)
	Long idCategory,
	
	@NotNull(message = DESCRIPTION_REQUIRED)
	@NotBlank(message = DESCRIPTION_REQUIRED)
	String description,
	
	@NotNull(message = PRICE_REQUIRED)
	@Positive(message = PRICE_MUST_BE_MORE_THAN_ZERO)
	Long price,
	
	@NotNull(message = RESTAURANT_ID_REQUIRED)
	@Positive(message = RESTAURANT_ID_MUST_BE_MORE_THAN_ZERO)
	Long idRestaurant,
	
	@NotNull(message = IMAGE_URL_REQUIRED)
	@NotBlank(message = IMAGE_URL_REQUIRED)
	String imageUrl,
	
	Boolean isAvailable
) {}
