package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.request.GetAllDishByRestaurantIdRequest;
import com.foodcourt.foodcourt.application.dto.request.UpdateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.application.handler.DishHandler;
import com.foodcourt.foodcourt.application.mappers.CreateDishRequestMapper;
import com.foodcourt.foodcourt.application.mappers.UpdateDishRequestMapper;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;
import com.foodcourt.foodcourt.domain.ports.CreateDishPort;
import com.foodcourt.foodcourt.domain.ports.GetAllDishByRestaurantIdPort;
import com.foodcourt.foodcourt.domain.ports.ToggleDishAvailabilityPort;
import com.foodcourt.foodcourt.domain.ports.UpdateDishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements DishHandler {
	
	private final CreateDishPort createDishPort;
	private final UpdateDishPort updateDishPort;
	private final ToggleDishAvailabilityPort toggleDishAvailabilityPort;
	private final GetAllDishByRestaurantIdPort getAllDishByRestaurantIdPort;
	
	@Override
	public CreateDishResponse createDish(CreateDishRequest request) {
		log.trace("Creating dish with name: {}", request.name());
		Dish dishToSave = CreateDishRequestMapper.INSTANCE.toDomain(request);
		Dish savedDish = createDishPort.execute(dishToSave, getIdUserCreator());
		log.debug("Created dish with ID: {}", savedDish.getId());
		return new CreateDishResponse(savedDish.getId(), savedDish.getName());
	}
	
	@Override
	public void updateDish(UpdateDishRequest request) {
		log.trace("Updating dish with ID: {}", request.id());
		Dish dishToUpdate = UpdateDishRequestMapper.INSTANCE.toDomain(request);
		updateDishPort.execute(dishToUpdate, getIdUserCreator());
		log.debug("Updated dish with ID: {}", dishToUpdate.getId());
	}
	
	@Override
	public void toggleDishAvailability(Long idDish) {
		log.trace("Toggling availability for dish with ID: {}", idDish);
		toggleDishAvailabilityPort.execute(idDish, getIdUserCreator());
		log.debug("Toggled availability for dish with ID: {}", idDish);
	}
	
	@Override
	public PaginationResponse<DishSummary> getDishesByIdRestaurant(Long idRestaurant, GetAllDishByRestaurantIdRequest request) {
		log.trace("Getting dishes for restaurant ID {} - Page: {}, Size: {}",
			idRestaurant, request.page(), request.size()
		);
		var filter = DishPaginationFilter.builder()
			.page(request.page())
			.size(request.size())
			.sortBy(request.sortBy())
			.sortDirection(request.sortDirection())
			.idCategory(request.idCategory())
			.build();
		
		return getAllDishByRestaurantIdPort.execute(idRestaurant, filter);
	}
	
	private Long getIdUserCreator() {
		Long idUserCreator = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserClaims userClaims) {
			idUserCreator = userClaims.id();
		}
		return idUserCreator;
	}
}
