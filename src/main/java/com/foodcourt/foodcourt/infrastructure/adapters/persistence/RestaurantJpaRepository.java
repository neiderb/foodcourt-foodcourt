package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantData, Long> {
	
	RestaurantData findByName(String name);
	
}
