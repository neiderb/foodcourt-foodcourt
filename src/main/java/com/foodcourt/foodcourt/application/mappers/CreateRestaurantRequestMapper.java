package com.foodcourt.foodcourt.application.mappers;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateRestaurantRequestMapper {
	
	CreateRestaurantRequestMapper INSTANCE = Mappers.getMapper(CreateRestaurantRequestMapper.class);
	
	@Mapping(target = "id", ignore = true)
	Restaurant toDomain(CreateRestaurantRequest request);
	
}
