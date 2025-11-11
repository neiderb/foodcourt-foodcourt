package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import org.springframework.data.domain.Pageable;

public interface PaginationFilterMapper {
	
	Pageable toPageable(PaginationFilter filter);
	
}
