package com.foodcourt.foodcourt.infrastructure.rest.feature.dish;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllDishByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.application.handler.DishHandler;
import com.foodcourt.foodcourt.domain.exception.BusinessException;
import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;
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

import java.util.Collections;

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.DishPath.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	
	@Test
	void shouldReturnPaginationWhenGetAllDishesByRestaurant() throws Exception {
		final Long restaurantId = 3L;
		final int page = 0;
		final int size = 10;
		final String sortBy = "name";
		final String sortDirection = "asc";
		final Long idCategory = 2L;
		
		var testReq = new GetAllDishByRestaurantIdRequest(
			page,
			size,
			sortBy,
			sortDirection,
			idCategory
		);
		var expectedResponse = PaginationResponse.<DishSummary>builder()
			.pageNumber(page)
			.pageSize(size)
			.totalElements(0L)
			.totalPages(0)
			.content(Collections.emptyList())
			.build();
		
		when(dishHandler.getDishesByIdRestaurant(any(Long.class), any(GetAllDishByRestaurantIdRequest.class)))
			.thenReturn(expectedResponse);
		
		mockMvc.perform(get(BASE.concat(BY_RESTAURANT), restaurantId)
			.param("page", String.valueOf(page))
			.param("size",  String.valueOf(size))
			.param("sortBy", sortBy)
			.param("sortDirection", sortDirection)
			.param("idCategory", String.valueOf(idCategory))
			.contentType(MediaType.APPLICATION_JSON.toString())
		)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.pageNumber").value(page))
		.andExpect(jsonPath("$.pageSize").value(size))
		.andExpect(jsonPath("$.totalElements").value(0))
		.andExpect(jsonPath("$.totalPages").value(0))
		.andExpect(jsonPath("$.content").isArray())
		.andExpect(jsonPath("$.content").isEmpty());
		
		verify(dishHandler).getDishesByIdRestaurant(
			eq(restaurantId),
			assertArg(reqArg -> {
				assertEquals(testReq.page(), reqArg.page());
				assertEquals(testReq.size(), reqArg.size());
				assertEquals(testReq.sortBy(), reqArg.sortBy());
				assertEquals(testReq.sortDirection(), reqArg.sortDirection());
				assertEquals(testReq.idCategory(), reqArg.idCategory());
			})
		);
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
