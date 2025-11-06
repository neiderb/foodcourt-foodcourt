package com.foodcourt.foodcourt.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.foodcourt.foodcourt.domain.constants.Regex.JUST_NUMBERS;
import static com.foodcourt.foodcourt.domain.constants.Regex.JUST_NUMBERS_AND_SYMBOL_PLUS;
import static com.foodcourt.foodcourt.domain.constants.RestaurantValidationMessage.*;

public record CreateRestaurantRequest(
	@NotNull(message = NAME_REQUIRED)
	@NotBlank(message = NAME_REQUIRED)
	String name,
	
	@NotNull(message = NIT_REQUIRED)
	@NotBlank(message = NIT_REQUIRED)
	@Pattern(regexp = JUST_NUMBERS, message = NIT_JUST_NUMBERS)
	String nit,
	
	@NotNull(message = ADDRESS_REQUIRED)
	@NotBlank(message = ADDRESS_REQUIRED)
	String address,
	
	@NotNull(message = PHONE_NUMBER_REQUIRED)
	@NotBlank(message = PHONE_NUMBER_REQUIRED)
	@Pattern(regexp = JUST_NUMBERS_AND_SYMBOL_PLUS, message = PHONE_NUMBER_JUST_NUMBERS_AND_SYMBOL_PLUS_IS_PERMITTED)
	String phoneNumber,
	
	@NotNull(message = URL_LOGO_REQUIRED)
	@NotBlank(message = URL_LOGO_REQUIRED)
	String urlLogo,
	
	@NotNull(message = OWNER_ID_REQUIRED)
	Long ownerId
) {}
