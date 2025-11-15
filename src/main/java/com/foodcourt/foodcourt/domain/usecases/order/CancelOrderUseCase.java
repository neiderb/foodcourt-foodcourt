package com.foodcourt.foodcourt.domain.usecases.order;

import com.foodcourt.foodcourt.domain.exception.auth.InvalidUserException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderStatusException;
import com.foodcourt.foodcourt.domain.exception.order.OrderNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.TraceServiceGateway;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderTrace;
import com.foodcourt.foodcourt.domain.ports.order.CancelOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.foodcourt.foodcourt.domain.constants.AuthErrorMessage.UNAUTHORIZED_ACTION;
import static com.foodcourt.foodcourt.domain.constants.OrderErrorMessage.ORDER_MUST_BE_PENDING_TO_CANCEL;
import static com.foodcourt.foodcourt.domain.constants.OrderErrorMessage.ORDER_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.OrderValidationMessage.INVALID_ORDER_ID;
import static com.foodcourt.foodcourt.domain.model.auth.enums.UserRole.CLIENT;
import static com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus.CANCELLED;
import static com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus.PENDING;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class CancelOrderUseCase implements CancelOrderPort {
	
	private final OrderRepositoryGateway orderRepositoryGateway;
	private final TraceServiceGateway traceServiceGateway;
	
	@Override
	public void execute(Long idOrder, UserClaims userClaims) {
		Long idUser = getIdUser(userClaims);
		Order existingOrder = validateOrder(idOrder, idUser);
		existingOrder.setStatus(CANCELLED);
		orderRepositoryGateway.save(existingOrder);
		log.debug("Order ID: {} has been cancelled by user ID: {}", idOrder, idUser);
		saveOrderTrace(existingOrder, userClaims);
	}
	
	private Long getIdUser(UserClaims userClaims) {
		log.trace("Getting user ID from user claims");
		if (isNull(userClaims)) throw new InvalidUserException(UNAUTHORIZED_ACTION);
		if (!CLIENT.equals(userClaims.role())) throw new InvalidUserException(UNAUTHORIZED_ACTION);
		return userClaims.id();
	}
	
	private Order validateOrder(Long idOrder, Long idUser) {
		log.trace("Validating order ID: {}", idOrder);
		if (isNull(idOrder) || idOrder <= 0)
			throw new InvalidOrderException(INVALID_ORDER_ID);
		
		Order existingOrder = orderRepositoryGateway.findById(idOrder);
		if (isNull(existingOrder))
			throw new OrderNotFoundException(ORDER_NOT_FOUND);
		if (!existingOrder.getIdClient().equals(idUser))
			throw new InvalidUserException(UNAUTHORIZED_ACTION);
		if (!PENDING.equals(existingOrder.getStatus()))
			throw new InvalidOrderStatusException(ORDER_MUST_BE_PENDING_TO_CANCEL);
		
		return existingOrder;
	}
	
	private void saveOrderTrace(Order order, UserClaims userClaims) {
		traceServiceGateway.saveOrderTrace(new OrderTrace(
			order.getId(),
			order.getIdClient(),
			userClaims.email(),
			PENDING,
			CANCELLED,
			null,
			null
		));
	}
	
}
