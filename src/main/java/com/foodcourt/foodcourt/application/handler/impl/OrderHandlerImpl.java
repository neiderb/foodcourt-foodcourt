package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateOrderResponse;
import com.foodcourt.foodcourt.application.handler.OrderHandler;
import com.foodcourt.foodcourt.application.mappers.CreateOrderRequestMapper;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.ports.CreateOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHandlerImpl implements OrderHandler {
	
	private final CreateOrderPort createOrderPort;
	
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest request) {
		Order orderToCreate = CreateOrderRequestMapper.INSTANCE.toDomain(request);
		orderToCreate.setIdClient(getIdClient());
		log.trace("Creating order {}", orderToCreate);
		Order orderCreated = createOrderPort.execute(orderToCreate);
		log.debug("Order created with ID: {}", orderCreated.getId());
		return new CreateOrderResponse(orderCreated.getId());
	}
	
	private Long getIdClient() {
		Long idClient = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserClaims userClaims) {
			idClient = userClaims.id();
		}
		return idClient;
	}
	
}
