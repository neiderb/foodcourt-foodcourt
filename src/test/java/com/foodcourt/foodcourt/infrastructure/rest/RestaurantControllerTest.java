package com.foodcourt.foodcourt.infrastructure.rest;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.application.handler.RestaurantHandler;
import com.foodcourt.foodcourt.infrastructure.rest.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.RestaurantPath.BASE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
@TestPropertySource(properties = {
	"server.port=0"
})
@Import(SecurityConfig.class)
class RestaurantControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private RestaurantHandler restaurantHandler;
	
	@Test
	void shouldCreateRestaurant() throws Exception {
		CreateRestaurantResponse mockResponse = new CreateRestaurantResponse(
			1L,
			"Pizza Place"
		);
		
		String jsonBody = """
				{
					"name": "SYSBURGER",
				    "nit": "3333333331",
				    "address": "123 Main St",
				    "phoneNumber": "+573001234567",
				    "urlLogo": "http://example.com/logo.png",
				    "ownerId": 1
				}
			""";
		
		when(restaurantHandler.createRestaurant(any(CreateRestaurantRequest.class))).thenReturn(mockResponse);
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(mockResponse.id()))
		.andExpect(jsonPath("$.name").value(mockResponse.name()));
	}
	
	@Test
	void shouldReturnBadRequestWhenCreatingRestaurantWithInvalidData() throws Exception {
		String jsonBody = """
				{
					"name": "",
				    "nit": "invalid_nit",
				    "address": "",
				    "phoneNumber": "12345",
				    "urlLogo": "not_a_url",
				    "ownerId": -1
				}
			""";
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isBadRequest());
	}
	
}
