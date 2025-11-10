package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers;

import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {
	
	RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);
	
	@Mapping(target = "phoneNumber", source = "phone")
	Restaurant toDomain(RestaurantData restaurantData);
	
	@Mapping(target = "phone", source = "phoneNumber")
	RestaurantData toData(Restaurant restaurant);
	
}
