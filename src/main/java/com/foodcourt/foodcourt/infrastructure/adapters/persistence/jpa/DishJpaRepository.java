package com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa;

import com.foodcourt.foodcourt.infrastructure.adapters.persistence.dto.DishFilter;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.DishData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.DishSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DishJpaRepository extends JpaRepository<DishData, Long> {
	
	@Query(nativeQuery = true, value = """
		SELECT d.id AS id,
		    d.name AS name,
		    d.description AS description,
		    d.price AS price,
		    d.image_url AS urlImage,
		    c.name AS categoryName
		FROM dish d
		JOIN category c ON d.id_category = c.id
		JOIN restaurant r ON r.id = d.id_restaurant
		WHERE r.id = :#{#filter.idRestaurant}
		    AND (:#{#filter.idCategory} = 0 OR c.id = :#{#filter.idCategory})
		""")
	Page<DishSummaryProjection> findDishPaginatedBy(Pageable pageable, DishFilter filter);
	
}
