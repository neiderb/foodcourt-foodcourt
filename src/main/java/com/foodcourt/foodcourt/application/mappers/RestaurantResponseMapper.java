package com.foodcourt.foodcourt.application.mappers;

import com.foodcourt.foodcourt.application.dto.response.RestaurantResponse;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantResponseMapper {
	
	RestaurantResponseMapper INSTANCE = Mappers.getMapper(RestaurantResponseMapper.class);
	
	RestaurantResponse toResponse(Restaurant restaurant);
	
}
