package com.foodcourt.foodcourt.infrastructure.rest.feature.restaurant;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateRestaurantResponse;
import com.foodcourt.foodcourt.application.dto.response.RestaurantResponse;
import com.foodcourt.foodcourt.application.handler.RestaurantHandler;
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

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.RestaurantPath.BASE;
import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.RestaurantPath.FIND_BY_ID;
import static com.foodcourt.foodcourt.infrastructure.rest.docapi.RestaurantDocApi.*;

@Slf4j
@Tag(name = TAG_RESTAURANT)
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class RestaurantController {
	
	private final RestaurantHandler restaurantHandler;
	
	@Operation(summary = CREATE_RESTAURANT_SUMMARY)
	@ApiResponse(
		responseCode = "201",
		description = CREATE_RESTAURANT_DESCRIPTION,
		content = @Content(
			schema = @Schema(
				implementation = CreateRestaurantResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	@PostMapping
	ResponseEntity<CreateRestaurantResponse> createUser(@RequestBody @Valid CreateRestaurantRequest createRestaurantRequest) {
		log.trace("createRestaurant: {}", createRestaurantRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(restaurantHandler.createRestaurant(createRestaurantRequest));
	}
	
	@Operation(summary = FIND_RESTAURANT_BY_ID_SUMMARY)
	@ApiResponse(
		responseCode = "200",
		description = FIND_RESTAURANT_BY_ID_DESCRIPTION,
		content = @Content(
			schema = @Schema(
				implementation = RestaurantResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	@GetMapping(FIND_BY_ID)
	ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
		log.trace("getById: {}", id);
		return ResponseEntity.ok(restaurantHandler.getRestaurantById(id));
	}
	
}
