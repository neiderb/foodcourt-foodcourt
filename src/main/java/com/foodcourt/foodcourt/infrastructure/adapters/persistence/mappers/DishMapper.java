package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers;

import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.DishData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DishMapper {
	
	DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);
	
	@Mapping(target = "idCategory", source = "category.id")
	Dish toDomain(DishData dishData);
	
	@Mapping(target = "category.id", source = "idCategory")
	DishData toData(Dish dish);
	
}
