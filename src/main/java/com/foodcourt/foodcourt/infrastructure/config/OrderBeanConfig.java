package com.foodcourt.foodcourt.infrastructure.config;

import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.ports.CreateOrderPort;
import com.foodcourt.foodcourt.domain.ports.GetAllOrderByRestaurantIdPort;
import com.foodcourt.foodcourt.domain.ports.GetOrderByIdPort;
import com.foodcourt.foodcourt.domain.usecases.CreateOrderUseCase;
import com.foodcourt.foodcourt.domain.usecases.GetAllOrderByRestaurantIdUseCase;
import com.foodcourt.foodcourt.domain.usecases.GetOrderByIdUseCase;
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
	
}
