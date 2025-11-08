package com.foodcourt.foodcourt.infrastructure.rest.feature.dish;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.application.handler.DishHandler;
import com.foodcourt.foodcourt.domain.exception.BusinessException;
import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.infrastructure.rest.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.DishPath.BASE;
import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.DishPath.TOGGLE_AVAILABILITY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
	"server.port=0"
})
@Import(TestSecurityConfig.class)
class DishControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private DishHandler dishHandler;
	
	@Test
	void shouldCreateDish() throws Exception {
		final Long idDishCreated = 1L;
		final String nameDishCreated = "Spaghetti Carbonara";
		String jsonBody = String.format("""
				{
					"name": "%s",
					"idCategory": 2,
				    "description": "Classic Italian pasta dish with eggs, cheese, pancetta, and pepper.",
				    "price": 12000,
				    "idRestaurant": 3,
				    "imageUrl": "http://example.com/dish.png"
				}
			""", nameDishCreated);
		CreateDishResponse expectedResponse = new CreateDishResponse(
			idDishCreated,
			nameDishCreated
		);
		
		when(dishHandler.createDish(any(CreateDishRequest.class))).thenReturn(expectedResponse);
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(idDishCreated))
		.andExpect(jsonPath("$.name").value(nameDishCreated));
	}
	
	@Test
	void shouldReturnBadRequestWhenCreatingDishWithInvalidData() throws Exception {
		String jsonBody = """
				{
					"name": "",
					"idCategory": -1,
				    "description": "",
				    "price": -5000,
				    "idRestaurant": -2,
				    "imageUrl": ""
				}
			""";
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldUpdateDish() throws Exception {
		String jsonBody = """
				{
					"id": 1,
				    "description": "Updated description.",
				    "price": 15000
				}
			""";
		
		mockMvc.perform(patch(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isNoContent());
	}
	
	@Test
	void shouldReturnBadRequestWhenUpdatingDishWithInvalidData() throws Exception {
		String jsonBody = """
				{
					"id": -1,
				    "description": "",
				    "price": -1000
				}
			""";
		
		mockMvc.perform(patch(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnBadRequestWhenHandlerThrowsBusinessException() throws Exception {
		String jsonBody = validJsonDishRequest();
		
		when(dishHandler.createDish(any(CreateDishRequest.class)))
			.thenThrow(new BusinessException("Business exception occurred"));
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnInternalServerErrorWhenHandlerThrowsUnexpectedException() throws Exception {
		String jsonBody = validJsonDishRequest();
		
		when(dishHandler.createDish(any(CreateDishRequest.class)))
			.thenThrow(new RuntimeException("Unexpected error"));
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isInternalServerError());
	}
	
	@Test
	void shouldReturnInternalServerErrorWhenHandlerThrowsTechnicalException() throws Exception {
		String jsonBody = validJsonDishRequest();
		
		when(dishHandler.createDish(any(CreateDishRequest.class)))
			.thenThrow(new TechnicalException("Technical error"));
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody)
		)
		.andExpect(status().isInternalServerError());
	}
	
	@Test
	void shouldReturnNotFoundWhenPathIsInvalid() throws Exception {
		mockMvc.perform(post("/invalid-path")
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(validJsonDishRequest())
		)
		.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnNoContentWhenTogglingDishAvailability() throws Exception {
		final Long dishIdToToggle = 1L;
		
		mockMvc.perform(patch(BASE.concat(TOGGLE_AVAILABILITY), dishIdToToggle))
		.andExpect(status().isNoContent());
	}
	
	private String validJsonDishRequest() {
		return """
				{
					"name": "Spaghetti Carbonara",
					"idCategory": 2,
				    "description": "Classic Italian pasta dish with eggs, cheese, pancetta, and pepper.",
				    "price": 12000,
				    "idRestaurant": 3,
				    "imageUrl": "http://example.com/dish.png"
				}
			""";
	}
}
