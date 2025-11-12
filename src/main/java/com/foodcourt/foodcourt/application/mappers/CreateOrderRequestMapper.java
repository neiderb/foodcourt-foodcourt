package com.foodcourt.foodcourt.application.mappers;

import com.foodcourt.foodcourt.application.dto.request.CreateOrderDishRequest;
import com.foodcourt.foodcourt.application.dto.request.CreateOrderRequest;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateOrderRequestMapper {
	
	CreateOrderRequestMapper INSTANCE = Mappers.getMapper(CreateOrderRequestMapper.class);
	
	@Mapping(target = "idOrder", ignore = true)
	@Mapping(target = "dish.id", source = "idDish")
	OrderDish toDomain(CreateOrderDishRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "orderDate", ignore = true)
	@Mapping(target = "idChef", ignore = true)
	Order toDomain(CreateOrderRequest request);
	
}
