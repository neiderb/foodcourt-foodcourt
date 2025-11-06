package com.foodcourt.foodcourt.application.handler.impl;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.application.dto.request.UpdateDishRequest;
import com.foodcourt.foodcourt.application.dto.response.CreateDishResponse;
import com.foodcourt.foodcourt.application.handler.DishHandler;
import com.foodcourt.foodcourt.application.mappers.CreateDishRequestMapper;
import com.foodcourt.foodcourt.application.mappers.UpdateDishRequestMapper;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.domain.ports.CreateDishPort;
import com.foodcourt.foodcourt.domain.ports.UpdateDishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements DishHandler {
	
	private final CreateDishPort createDishPort;
	private final UpdateDishPort updateDishPort;
	
	@Override
	public CreateDishResponse createDish(CreateDishRequest request) {
		Long idUserCreator = 1L; // TODO: get from security context
		Dish dishToSave = CreateDishRequestMapper.INSTANCE.toDomain(request);
		Dish savedDish = createDishPort.execute(dishToSave, idUserCreator);
		return new CreateDishResponse(savedDish.getId(), savedDish.getName());
	}
	
	@Override
	public void updateDish(UpdateDishRequest request) {
		Long idUserCreator = 1L; // TODO: get from security context
		Dish dishToUpdate = UpdateDishRequestMapper.INSTANCE.toDomain(request);
		updateDishPort.execute(dishToUpdate, idUserCreator);
	}
}
