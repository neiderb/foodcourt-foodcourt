package com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa;

import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.RestaurantSummaryProjection;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantData, Long> {
	
	boolean existsByNameIgnoreCaseOrNit(String name, String nit);
	
	@Query(
		nativeQuery = true,
		value = """
			SELECT
			    r.id AS id,
			    r.name AS name,
			    r.url_logo AS urlLogo
			FROM restaurant r
			"""
	)
	Page<RestaurantSummaryProjection> findRestaurantPaginatedBy(Pageable pageable);
	
}
