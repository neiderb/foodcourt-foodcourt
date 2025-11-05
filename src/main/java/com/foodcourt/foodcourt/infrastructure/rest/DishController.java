package com.foodcourt.foodcourt.infrastructure.rest;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.application.handler.DishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.DishPath.BASE;
import static com.foodcourt.foodcourt.infrastructure.rest.docapi.DishDocApi.*;

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
		return ResponseEntity.status(HttpStatus.CREATED).body(dishHandler.createDish(createDishRequest));
	}
	
}
