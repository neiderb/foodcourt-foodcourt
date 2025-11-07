package com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa;

import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantData, Long> {
	
	boolean existsByNameIgnoreCaseOrNit(String name, String nit);
	
}
