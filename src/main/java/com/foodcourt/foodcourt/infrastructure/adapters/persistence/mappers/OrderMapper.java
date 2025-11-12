package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers;

import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderDish;
import com.foodcourt.foodcourt.domain.model.order.OrderStatus;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderDishData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Mapper(uses = { DishMapper.class })
public interface OrderMapper {
	
	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
	
	@Mapping(target = "order.id", source = "idOrder")
	OrderDishData toData(OrderDish orderDish);
	
	@Mapping(target = "idOrder", source = "order.id")
	OrderDish toDomain(OrderDishData orderDishData);
	
	@Mapping(target = "status", ignore = true)
	Order toDomain(OrderData orderData);
	
	OrderData toData(Order order);
	
	@AfterMapping
	default void mapOrderStatus(OrderData orderData, @MappingTarget Order order) {
		order.setStatus(OrderStatus.of(orderData.getStatus()));
	}
	
	@AfterMapping
	default void mapReferences(@MappingTarget OrderData orderData) {
		List<OrderDishData> items = orderData.getItems();
		if (!CollectionUtils.isEmpty(items)) {
			items.forEach(item -> item.setOrder(orderData));
		}
	}
	
}
