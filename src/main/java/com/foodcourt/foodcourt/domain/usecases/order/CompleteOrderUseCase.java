package com.foodcourt.foodcourt.domain.usecases.order;

import com.foodcourt.foodcourt.domain.exception.auth.InvalidUserException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderException;
import com.foodcourt.foodcourt.domain.exception.order.InvalidOrderStatusException;
import com.foodcourt.foodcourt.domain.exception.order.OrderNotFoundException;
import com.foodcourt.foodcourt.domain.gateways.NotificationServiceGateway;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.TraceServiceGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.auth.User;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderTrace;
import com.foodcourt.foodcourt.domain.ports.order.CompleteOrderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

import static com.foodcourt.foodcourt.domain.constants.AuthErrorMessage.UNAUTHORIZED_ACTION;
import static com.foodcourt.foodcourt.domain.constants.OrderErrorMessage.ORDER_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.constants.OrderErrorMessage.ORDER_STATUS_MUST_BE_PROCESSING;
import static com.foodcourt.foodcourt.domain.constants.OrderValidationMessage.INVALID_ORDER_ID;
import static com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus.COMPLETED;
import static com.foodcourt.foodcourt.domain.model.order.enums.OrderStatus.PROCESSING;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class CompleteOrderUseCase implements CompleteOrderPort {
	
	private final OrderRepositoryGateway orderRepositoryGateway;
	private final UserServiceGateway userServiceGateway;
	private final NotificationServiceGateway notificationServiceGateway;
	private final TraceServiceGateway traceServiceGateway;
	
	@Override
	public void execute(Long idOrder, UserClaims userClaims) {
		Long idUser = getIdUser(userClaims);
		Order existingOrder = validateOrder(idOrder, idUser);
		existingOrder.setStatus(COMPLETED);
		
		String securePin = generateSecurePin();
		existingOrder.setSecurePin(securePin);
		
		log.trace("Updating order status to COMPLETED for order ID: {}", idOrder);
		orderRepositoryGateway.save(existingOrder);
		
		User client = userServiceGateway.getUserById(existingOrder.getIdClient());
		log.trace("Notifying client at phone number: {}", client.getPhone());
		notificationServiceGateway.sendOrderCompletedNotification(client.getPhone(), securePin);
		saveOrderTrace(existingOrder, client, userClaims);
	}
	
	private Long getIdUser(UserClaims userClaims) {
		log.trace("Getting user ID from user claims");
		if (isNull(userClaims)) throw new InvalidUserException(UNAUTHORIZED_ACTION);
		return userClaims.id();
	}
	
	private Order validateOrder(Long idOrder, Long idUser) {
		log.trace("Validating order ID: {}", idOrder);
		if (isNull(idOrder) || idOrder <= 0)
			throw new InvalidOrderException(INVALID_ORDER_ID);
		
		Order existingOrder = orderRepositoryGateway.findById(idOrder);
		if (isNull(existingOrder))
			throw new OrderNotFoundException(ORDER_NOT_FOUND);
		if (!PROCESSING.equals(existingOrder.getStatus()))
			throw new InvalidOrderStatusException(ORDER_STATUS_MUST_BE_PROCESSING);
		if (!existingOrder.getIdChef().equals(idUser))
			throw new InvalidUserException(UNAUTHORIZED_ACTION);
		
		return existingOrder;
	}
	
	private String generateSecurePin() {
		SecureRandom secureRandom = new SecureRandom();
		int min = 100_000;
		int max = 1_000_000;
		int pin = secureRandom.nextInt(min, max) + min;
		return String.valueOf(pin);
	}
	
	private void saveOrderTrace(Order order, User client, UserClaims userClaims) {
		traceServiceGateway.saveOrderTrace(new OrderTrace(
			order.getId(),
			order.getIdClient(),
			client.getEmail(),
			PROCESSING,
			COMPLETED,
			userClaims.id(),
			userClaims.email(),
			order.getIdRestaurant()
		));
	}
	
}
