package com.foodcourt.foodcourt.application.config;

import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.ports.dish.CreateDishPort;
import com.foodcourt.foodcourt.domain.ports.dish.GetAllDishByRestaurantIdPort;
import com.foodcourt.foodcourt.domain.ports.dish.ToggleDishAvailabilityPort;
import com.foodcourt.foodcourt.domain.ports.dish.UpdateDishPort;
import com.foodcourt.foodcourt.domain.usecases.dish.CreateDishUseCase;
import com.foodcourt.foodcourt.domain.usecases.dish.GetAllDishByRestaurantIdUseCase;
import com.foodcourt.foodcourt.domain.usecases.dish.ToggleDishAvailabilityUseCase;
import com.foodcourt.foodcourt.domain.usecases.dish.UpdateDishUseCase;
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
	
	@Bean
	public UpdateDishPort updateDishPort(DishRepositoryGateway dishRepositoryGateway, RestaurantRepositoryGateway restaurantRepositoryGateway) {
		return new UpdateDishUseCase(
			dishRepositoryGateway,
			restaurantRepositoryGateway
		);
	}
	
	@Bean
	public ToggleDishAvailabilityPort toggleDishAvailabilityPort(
		DishRepositoryGateway dishRepositoryGateway,
		RestaurantRepositoryGateway restaurantRepositoryGateway
	) {
		return new ToggleDishAvailabilityUseCase(
			dishRepositoryGateway,
			restaurantRepositoryGateway
		);
	}
	
	@Bean
	public GetAllDishByRestaurantIdPort getAllDishByRestaurantIdPort(DishRepositoryGateway dishRepositoryGateway) {
		return new GetAllDishByRestaurantIdUseCase(dishRepositoryGateway);
	}
	
}
