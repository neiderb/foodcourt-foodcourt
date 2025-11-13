package com.foodcourt.foodcourt.application.config;

import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.ports.restaurant.CreateRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.restaurant.GetAllRestaurantPort;
import com.foodcourt.foodcourt.domain.ports.restaurant.GetRestaurantByIdPort;
import com.foodcourt.foodcourt.domain.usecases.restaurant.CreateRestaurantUseCase;
import com.foodcourt.foodcourt.domain.usecases.restaurant.GetAllRestaurantUseCase;
import com.foodcourt.foodcourt.domain.usecases.restaurant.GetRestaurantByIdUseCase;
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
