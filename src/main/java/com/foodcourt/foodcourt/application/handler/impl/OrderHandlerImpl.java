package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllOrderByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateOrderResponse;
import com.foodcourt.foodcourt.application.dto.response.GetOrderResponse;
import com.foodcourt.foodcourt.application.handler.OrderHandler;
import com.foodcourt.foodcourt.application.mappers.CreateOrderRequestMapper;
import com.foodcourt.foodcourt.application.mappers.GetOrderResponseMapper;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderStatusException;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderPaginationFilter;
import com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus;
import com.foodcourt.foodcourt.domain.model.order.OrderSummary;
import com.foodcourt.foodcourt.domain.ports.order.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHandlerImpl implements OrderHandler {
	
	private final CreateOrderPort createOrderPort;
	private final GetAllOrderByRestaurantIdPort getAllOrderByRestaurantIdPort;
	private final GetOrderByIdPort getOrderByIdPort;
	private final AssignOrderPort assignOrderPort;
	private final CompleteOrderPort completeOrderPort;
	
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest request) {
		Order orderToCreate = CreateOrderRequestMapper.INSTANCE.toDomain(request);
		orderToCreate.setIdClient(getIdClient());
		log.trace("Creating order {}", orderToCreate);
		Order orderCreated = createOrderPort.execute(orderToCreate);
		log.debug("Order created with ID: {}", orderCreated.getId());
		return new CreateOrderResponse(orderCreated.getId());
	}
	
	@Override
	public PaginationResponse<OrderSummary> getAllOrdersByRestaurantId(GetAllOrderByRestaurantIdRequest request) {
		log.trace("Getting all orders for restaurant with filter: {}", request);
		var filter = OrderPaginationFilter.builder()
			.page(request.page())
			.size(request.size())
			.sortBy(request.sortBy())
			.sortDirection(request.sortDirection())
			.status(sanitizeStatus(request.status()))
			.build();
		
		return getAllOrderByRestaurantIdPort.execute(getIdRestaurant(), filter);
	}
	
	@Override
	public GetOrderResponse getOrderById(Long idOrder) {
		log.trace("Getting order by ID: {}", idOrder);
		return GetOrderResponseMapper.INSTANCE.toResponse(
			getOrderByIdPort.execute(idOrder, getUserClaims())
		);
	}
	
	@Override
	public void assignOrder(Long idOrder) {
		log.trace("Assigning order by ID: {}", idOrder);
		assignOrderPort.execute(idOrder, getUserClaims());
	}
	
	@Override
	@Transactional
	public void completeOrder(Long idOrder) {
		log.trace("Completing order by ID: {}", idOrder);
		completeOrderPort.execute(idOrder, getUserClaims());
	}
	
	private Long getIdClient() {
		Long idClient = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserClaims userClaims) {
			idClient = userClaims.id();
		}
		return idClient;
	}
	
	private Long getIdRestaurant() {
		Long idRestaurant = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserClaims userClaims) {
			idRestaurant = userClaims.idRestaurant();
		}
		return idRestaurant;
	}
	
	private UserClaims getUserClaims() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserClaims userClaims) {
			return userClaims;
		}
		return null;
	}
	
	private OrderStatus sanitizeStatus(String status) {
		try {
			return OrderStatus.of(status);
		} catch (InvalidOrderStatusException e) {
			return null;
		}
	}
	
}
