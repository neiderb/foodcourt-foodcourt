package com.foodcourt.foodcourt.application.mappers;

import com.foodcourt.foodcourt.application.dto.request.CreateDishRequest;
import com.foodcourt.foodcourt.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateDishRequestMapper {
	
	CreateDishRequestMapper INSTANCE = Mappers.getMapper(CreateDishRequestMapper.class);
	
	Dish toDomain(CreateDishRequest request);
	
}
