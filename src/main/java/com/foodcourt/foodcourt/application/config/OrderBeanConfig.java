package com.foodcourt.foodcourt.application.config;

import com.foodcourt.foodcourt.domain.gateways.*;
import com.foodcourt.foodcourt.domain.ports.order.*;
import com.foodcourt.foodcourt.domain.usecases.order.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBeanConfig {
	
	@Bean
	public CreateOrderPort createOrderPort(
		OrderRepositoryGateway orderRepositoryGateway,
		UserServiceGateway userServiceGateway,
		RestaurantRepositoryGateway restaurantRepositoryGateway,
		DishRepositoryGateway dishRepositoryGateway
	) {
		return new CreateOrderUseCase(
			orderRepositoryGateway,
			userServiceGateway,
			restaurantRepositoryGateway,
			dishRepositoryGateway
		);
	}
	
	@Bean
	public GetAllOrderByRestaurantIdPort getAllOrderByRestaurantIdPort(
		RestaurantRepositoryGateway restaurantRepositoryGateway,
		OrderRepositoryGateway orderRepositoryGateway
	) {
		return new GetAllOrderByRestaurantIdUseCase(
			restaurantRepositoryGateway,
			orderRepositoryGateway
		);
	}
	
	@Bean
	public GetOrderByIdPort getOrderByIdPort(
		RestaurantRepositoryGateway restaurantRepositoryGateway,
		OrderRepositoryGateway orderRepositoryGateway
	) {
		return new GetOrderByIdUseCase(
			restaurantRepositoryGateway,
			orderRepositoryGateway
		);
	}
	
	@Bean
	public AssignOrderPort asignOrderPort(
		OrderRepositoryGateway orderRepositoryGateway
	) {
		return new AssignOrderUseCase(
			orderRepositoryGateway
		);
	}
	
	@Bean
	public CompleteOrderPort completeOrderPort(
		OrderRepositoryGateway orderRepositoryGateway,
		UserServiceGateway userServiceGateway,
		NotificationServiceGateway notificationServiceGateway
	) {
		return new CompleteOrderUseCase(
			orderRepositoryGateway,
			userServiceGateway,
			notificationServiceGateway
		);
	}
	
	@Bean
	public DeliverOrderPort deliverOrderPort(
		OrderRepositoryGateway orderRepositoryGateway
	) {
		return new DeliverOrderUseCase(
			orderRepositoryGateway
		);
	}
	
	@Bean
	public CancelOrderPort cancelOrderPort(
		OrderRepositoryGateway orderRepositoryGateway
	) {
		return new CancelOrderUseCase(
			orderRepositoryGateway
		);
	}
	
}
