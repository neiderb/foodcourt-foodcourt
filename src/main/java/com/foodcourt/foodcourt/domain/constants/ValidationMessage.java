package com.foodcourt.foodcourt.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessage {
	
	public static final String NAME_REQUIRED = "Name is required";
	public static final String NIT_REQUIRED = "Nit is required";
	public static final String ADDRESS_REQUIRED = "Address is required";
	public static final String PHONE_NUMBER_REQUIRED = "Phone number is required";
	public static final String URL_LOGO_REQUIRED = "Logo's url is required";
	public static final String OWNER_ID_REQUIRED = "Owner is required";
	
	public static final String NIT_JUST_NUMBERS = "Nit must contain just numbers";
	public static final String PHONE_NUMBER_JUST_NUMBERS_AND_SYMBOL_PLUS_IS_PERMITTED = "Phone number must contain just numbers and the symbol + is permitted";
	
}
