package com.foodcourt.foodcourt.application.mappers;

import com.foodcourt.foodcourt.application.dto.request.CreateRestaurantRequest;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateRestaurantRequestMapper {
	
	CreateRestaurantRequestMapper INSTANCE = Mappers.getMapper(CreateRestaurantRequestMapper.class);
	
	Restaurant toDomain(CreateRestaurantRequest request);
	
}
