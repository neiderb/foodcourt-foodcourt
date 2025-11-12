package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.order.Order;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.OrderJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryGateway {
	
	private final OrderJpaRepository orderJpaRepository;
	
	@Override
	public Order save(Order order) {
		log.trace("Saving order for customer ID: {}", order.getIdClient());
		return OrderMapper.INSTANCE.toDomain(
			orderJpaRepository.save(OrderMapper.INSTANCE.toData(order))
		);
	}
	
}
