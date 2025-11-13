package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.gateways.OrderRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.order.*;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.dto.OrderFilter;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.enumerators.OrderSummaryColumn;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.OrderJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.OrderMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.PaginationFilterMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.OrderSummaryProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryGateway {

	private final OrderJpaRepository orderJpaRepository;
	private final PaginationFilterMapper paginationFilterMapper;

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
	
	@Override
	public PaginationResponse<OrderSummary> findAllByRestaurantId(Long idRestaurant, OrderPaginationFilter filter) {
		log.trace("Finding orders for restaurant ID: {} with filter: {}", idRestaurant, filter);
		filter.sanitizeSortBy(this::mapColumn);
		OrderFilter orderFilter = new OrderFilter(
			idRestaurant,
			nonNull(filter.getStatus()) ? filter.getStatus().name() : null
		);
		
		Pageable pageable = paginationFilterMapper.toPageable(filter);
		Page<OrderSummaryProjection> resultPage = orderJpaRepository.findOrderPaginatedBy(pageable, orderFilter);
		log.debug("Retrieved {} orders for restaurant ID: {}", resultPage.getTotalElements(), idRestaurant);
		return mapToPaginationResponse(resultPage);
	}
	
	private String mapColumn(String sortBy) {
		OrderSortBy sortColumn = OrderSortBy.of(sortBy);
		OrderSummaryColumn column = OrderSummaryColumn.of(sortColumn);
		return column.getColumnName();
	}
	
	private PaginationResponse<OrderSummary> mapToPaginationResponse(Page<OrderSummaryProjection> page) {
		return PaginationResponse.<OrderSummary>builder()
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.pageNumber(page.getNumber())
			.pageSize(page.getSize())
			.content(
				page.map(projection -> OrderSummary.builder()
					.idOrder(projection.getIdOrder())
					.orderDate(projection.getOrderDate())
					.status(projection.getStatus())
					.build()
				).getContent()
			)
			.build();
	}
	
}
