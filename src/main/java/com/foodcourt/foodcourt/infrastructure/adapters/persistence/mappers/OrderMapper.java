package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers;

import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderDish;
import com.foodcourt.foodcourt.domain.model.order.OrderStatus;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderDishData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { DishMapper.class }, builder = @Builder(disableBuilder = true))
public interface OrderMapper {
	
	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
	
	@Mapping(target = "order", ignore = true)
	@Mapping(target = "dish", ignore = true)
	@Mapping(target = "id.idOrder", source = "idOrder")
	@Mapping(target = "id.idDish", source = "dish.id")
	OrderDishData toData(OrderDish orderDish);
	
	@Mapping(target = "idOrder", source = "id.idOrder")
	OrderDish toDomain(OrderDishData orderDishData);
	
	@Mapping(target = "status", ignore = true)
	Order toDomain(OrderData orderData);
	
	OrderData toData(Order order);
	
	@AfterMapping
	default void mapOrderStatus(OrderData orderData, @MappingTarget Order order) {
		order.setStatus(OrderStatus.of(orderData.getStatus()));
	}
	
}
