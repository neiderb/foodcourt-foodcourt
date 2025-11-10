package com.foodcourt.foodcourt.application.mappers;

import com.foodcourt.foodcourt.application.dto.request.UpdateDishRequest;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdateDishRequestMapper {
	
	UpdateDishRequestMapper INSTANCE = Mappers.getMapper(UpdateDishRequestMapper.class);
	
	@Mapping(target = "name", ignore = true)
	@Mapping(target = "idCategory", ignore = true)
	@Mapping(target = "idRestaurant", ignore = true)
	@Mapping(target = "imageUrl", ignore = true)
	@Mapping(target = "isAvailable", ignore = true)
	Dish toDomain(UpdateDishRequest request);
	
}
