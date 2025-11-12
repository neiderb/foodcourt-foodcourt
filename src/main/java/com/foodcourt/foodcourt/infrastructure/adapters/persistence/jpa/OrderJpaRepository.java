package com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa;

import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderData, Long> {
}
