package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.domain.model.order.OrderStatus;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.OrderJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryGateway {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Order save(Order order) {
		OrderData data = OrderMapper.INSTANCE.toData(order);
		log.trace("Saving order: {}", data);
		return OrderMapper.INSTANCE.toDomain(
			orderJpaRepository.save(data)
		);
	}
	
	@Override
	public boolean existActiveOrderByClientId(Long idClient, Set<OrderStatus> activeStatus) {
		return orderJpaRepository.existsByIdClientAndStatusIn(
			idClient,
			activeStatus.stream().map(OrderStatus::name).collect(Collectors.toSet())
		);
	}
}
