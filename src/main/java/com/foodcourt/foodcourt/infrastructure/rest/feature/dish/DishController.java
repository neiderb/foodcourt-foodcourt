package com.foodcourt.foodcourt.infrastructure.rest.feature.dish;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllDishByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.request.UpdateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.application.handler.DishHandler;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.DishPath.*;
import static com.foodcourt.foodcourt.infrastructure.rest.docapi.DishDocApi.*;

@Slf4j
@Tag(name = TAG_DISH)
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class DishController {
	
	private final DishHandler dishHandler;
	
	@Operation(summary = CREATE_DISH_SUMMARY)
	@ApiResponse(
		responseCode = "201",
		description = CREATE_DISH_DESCRIPTION,
		content = @Content(
			schema = @Schema(
				implementation = CreateDishResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	@PostMapping
	ResponseEntity<CreateDishResponse> createDish(@RequestBody @Valid CreateDishRequest createDishRequest) {
		log.trace("createDish called with: {}", createDishRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(dishHandler.createDish(createDishRequest));
	}
	
	@Operation(summary = UPDATE_DISH_SUMMARY)
	@ApiResponse(
		responseCode = "204",
		description = UPDATE_DISH_DESCRIPTION
	)
	@PatchMapping
	ResponseEntity<Void> updateDish(@RequestBody @Valid UpdateDishRequest updateDishRequest) {
		log.trace("updateDish called with: {}", updateDishRequest);
		dishHandler.updateDish(updateDishRequest);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = TOGGLE_DISH_AVAILABILITY_SUMMARY)
	@ApiResponse(
		responseCode = "204",
		description = TOGGLE_DISH_AVAILABILITY_DESCRIPTION
	)
	@PatchMapping(TOGGLE_AVAILABILITY)
	ResponseEntity<Void> toggleAvailability(@PathVariable Long idDish) {
		log.trace("toggleAvailability called with idDish: {}", idDish);
		dishHandler.toggleDishAvailability(idDish);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = LIST_DISH_BY_RESTAURANT_SUMMARY)
	@ApiResponse(
		responseCode = "200",
		description = LIST_DISH_BY_RESTAURANT_DESCRIPTION
	)
	@GetMapping(BY_RESTAURANT)
	ResponseEntity<PaginationResponse<DishSummary>> getAll(
		@PathVariable Long idRestaurant,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) String sortDirection,
		@RequestParam(required = false) Long idCategory
	) {
		log.trace("getAllRestaurants");
		var request = new GetAllDishByRestaurantIdRequest(
			page,
			size,
			sortBy,
			sortDirection,
			idCategory
		);
		return ResponseEntity.ok(dishHandler.getDishesByIdRestaurant(idRestaurant, request));
	}
	
}
