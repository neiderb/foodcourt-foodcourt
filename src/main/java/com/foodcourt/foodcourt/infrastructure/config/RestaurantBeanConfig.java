package com.foodcourt.foodcourt.infrastructure.config;

import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.ports.CreateRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.GetAllRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.GetRestaurantByIdPort;
import com.foodcourt.foodcourt.domain.usecases.CreateRestaurantUseCase;
import com.foodcourt.foodcourt.domain.usecases.GetAllRestaurantUseCase;
import com.foodcourt.foodcourt.domain.usecases.GetRestaurantByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantBeanConfig {
	
	@Bean
	public CreateRestaurantPort createRestaurantPort(RestaurantRepositoryGateway restaurantRepositoryGateway, UserServiceGateway userServiceGateway) {
		return new CreateRestaurantUseCase(
			restaurantRepositoryGateway,
			userServiceGateway
		);
	}
	
	@Bean
	public GetRestaurantByIdPort getRestaurantByIdPort(RestaurantRepositoryGateway restaurantRepositoryGateway) {
		return new GetRestaurantByIdUseCase(
			restaurantRepositoryGateway
		);
	}
	
	@Bean
	public GetAllRestaurantPort getAllRestaurantPort(RestaurantRepositoryGateway restaurantRepositoryGateway) {
		return new GetAllRestaurantUseCase(
			restaurantRepositoryGateway
		);
	}
	
}
