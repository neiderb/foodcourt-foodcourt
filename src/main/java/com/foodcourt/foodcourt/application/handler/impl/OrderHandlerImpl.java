package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateOrderResponse;
import com.foodcourt.foodcourt.application.handler.OrderHandler;
import com.foodcourt.foodcourt.application.mappers.CreateOrderRequestMapper;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.ports.CreateOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHandlerImpl implements OrderHandler {
	
	private final CreateOrderPort createOrderPort;
	
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest request) {
		Order orderToCreate = CreateOrderRequestMapper.INSTANCE.toDomain(request);
		log.trace("Creating order for client ID: {}", request.idClient());
		Order orderCreated = createOrderPort.execute(orderToCreate);
		log.debug("Order created with ID: {}", orderCreated.getId());
		return new CreateOrderResponse(orderCreated.getId());
	}
	
}
