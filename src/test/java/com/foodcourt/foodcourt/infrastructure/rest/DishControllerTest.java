package com.foodcourt.foodcourt.infrastructure.rest;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.application.handler.DishHandler;
import com.foodcourt.foodcourt.infrastructure.rest.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.DishPath.BASE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
@TestPropertySource(properties = {
	"server.port=0"
})
@Import(SecurityConfig.class)
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
}
