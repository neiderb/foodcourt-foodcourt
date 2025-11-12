package com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa;

import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.OrderSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface OrderJpaRepository extends JpaRepository<OrderData, Long> {
	
	boolean existsByIdClientAndStatusIn(Long idClient, Set<String> status);
	
	@Query(nativeQuery = true, value = """
		SELECT
		    o.id AS irOrder,
		    o.order_date AS orderDate,
		    o.status AS status
		FROM foodcourt_order o
		WHERE o.id_restaurant = :idRestaurant
		""")
	Page<OrderSummaryProjection> findOrderPaginatedBy(Pageable pageable, Long idRestaurant);
}
