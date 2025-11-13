package com.foodcourt.foodcourt.infrastructure.rest.feature.order;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllOrderByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateOrderResponse;
import com.foodcourt.foodcourt.application.dto.response.GetOrderResponse;
import com.foodcourt.foodcourt.application.handler.OrderHandler;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.order.OrderSummary;
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

import static com.foodcourt.foodcourt.infrastructure.rest.constants.paths.OrderPath.*;
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
	
	@Operation(summary = LIST_ORDER_BY_RESTAURANT_SUMMARY)
	@ApiResponse(
		responseCode = "200",
		description = LIST_ORDER_BY_RESTAURANT_DESCRIPTION
	)
	@GetMapping(BASE)
	ResponseEntity<PaginationResponse<OrderSummary>> getAll(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) String sortDirection,
		@RequestParam(required = false) String status
	) {
		log.trace("getAllRestaurants");
		var request = new GetAllOrderByRestaurantIdRequest(
			page,
			size,
			sortBy,
			sortDirection,
			status
		);
		return ResponseEntity.ok(orderHandler.getAllOrdersByRestaurantId(request));
	}
	
	@Operation(summary = GET_ORDER_BY_ID_SUMMARY)
	@ApiResponse(
		responseCode = "200",
		description = GET_ORDER_BY_ID_DESCRIPTION
	)
	@GetMapping(ORDER_BY_ID)
	ResponseEntity<GetOrderResponse> getById(@PathVariable Long idOrder) {
		log.trace("getById: {}", idOrder);
		return ResponseEntity.ok(orderHandler.getOrderById(idOrder));
	}
	
	@Operation(summary = ASSIGN_ORDER_BY_ID_SUMMARY)
	@ApiResponse(
		responseCode = "204",
		description = ASSIGN_ORDER_BY_ID_DESCRIPTION
	)
	@PatchMapping(ASSIGN_ORDER_BY_ID)
	ResponseEntity<Void> assignById(@PathVariable Long idOrder) {
		log.trace("assignById: {}", idOrder);
		orderHandler.assignOrder(idOrder);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = COMPLETE_ORDER_BY_ID_SUMMARY)
	@ApiResponse(
		responseCode = "204",
		description = COMPLETE_ORDER_BY_ID_DESCRIPTION
	)
	@PatchMapping(COMPLETE_ORDER_BY_ID)
	ResponseEntity<Void> completeById(@PathVariable Long idOrder) {
		log.trace("completeById: {}", idOrder);
		orderHandler.completeOrder(idOrder);
		return ResponseEntity.noContent().build();
	}
	
}
