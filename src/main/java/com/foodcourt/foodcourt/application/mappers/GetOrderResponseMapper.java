package com.foodcourt.foodcourt.application.mappers;

import com.foodcourt.foodcourt.application.dto.response.GetOrderDishResponse;
import com.foodcourt.foodcourt.application.dto.response.GetOrderResponse;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GetOrderResponseMapper {
	
	GetOrderResponseMapper INSTANCE = Mappers.getMapper(GetOrderResponseMapper.class);
	
	@Mapping(target = "idDish", source = "dish.id")
	@Mapping(target = "dishName", source = "dish.name")
	GetOrderDishResponse toResponse(OrderDish orderDish);
	
	@Mapping(target = "idOrder", source = "id")
	@Mapping(target = "dishes", source = "items")
	GetOrderResponse toResponse(Order order);
	
}
