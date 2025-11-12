package com.foodcourt.foodcourt.infrastructure.config;

import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.ports.CreateOrderPort;
import com.foodcourt.foodcourt.domain.usecases.CreateOrderUseCase;
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
	
}
