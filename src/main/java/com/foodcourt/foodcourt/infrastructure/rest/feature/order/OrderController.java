package com.foodcourt.foodcourt.infrastructure.rest.feature.order;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateOrderResponse;
import com.foodcourt.foodcourt.application.handler.OrderHandler;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.OrderPath.BASE;
import static com.foodcourt.foodcourt.infrastructure.rest.docapi.OrderDocApi.*;


@Slf4j
@Tag(name = TAG_ORDER)
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class OrderController {
	
	private final OrderHandler orderHandler;
	
	@Operation(summary = CREATE_ORDER_SUMMARY)
	@ApiResponse(
		responseCode = "201",
		description = CREATE_ORDER_DESCRIPTION,
		content = @Content(
			schema = @Schema(
				implementation = CreateOrderResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	@PostMapping
	ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
		log.trace("createOrder: {}", createOrderRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(orderHandler.createOrder(createOrderRequest));
	}
	
}
