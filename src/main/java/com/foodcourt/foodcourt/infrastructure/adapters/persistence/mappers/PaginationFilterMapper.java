package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component
public class PaginationFilterMapper {
	
	public Pageable toPageable(PaginationFilter filter, UnaryOperator<String> sanitizeSortBy) {
		return PageRequest.of(
			filter.getPage(),
			filter.getSize(),
			filter.isAscending()
				? Sort.by(sanitizeSortBy.apply(filter.getSortBy())).ascending()
				: Sort.by(sanitizeSortBy.apply(filter.getSortBy())).descending()
		);
	}
	
}
