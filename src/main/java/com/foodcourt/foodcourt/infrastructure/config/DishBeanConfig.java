package com.foodcourt.foodcourt.infrastructure.config;

import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.ports.CreateDishPort;
import com.foodcourt.foodcourt.domain.usecases.CreateDishUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DishBeanConfig {
	
	@Bean
	public CreateDishPort createDishPort(DishRepositoryGateway dishRepositoryGateway, RestaurantRepositoryGateway restaurantRepositoryGateway) {
		return new CreateDishUseCase(
			dishRepositoryGateway,
			restaurantRepositoryGateway
		);
	}
}
