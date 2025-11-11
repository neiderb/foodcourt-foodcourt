package com.foodcourt.foodcourt.infrastructure.config;

import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.ports.CreateDishPort;
import com.foodcourt.foodcourt.domain.ports.GetAllDishByRestaurantIdPort;
import com.foodcourt.foodcourt.domain.ports.ToggleDishAvailabilityPort;
import com.foodcourt.foodcourt.domain.ports.UpdateDishPort;
import com.foodcourt.foodcourt.domain.usecases.CreateDishUseCase;
import com.foodcourt.foodcourt.domain.usecases.GetAllDishByRestaurantIdUseCase;
import com.foodcourt.foodcourt.domain.usecases.ToggleDishAvailabilityUseCase;
import com.foodcourt.foodcourt.domain.usecases.UpdateDishUseCase;
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
