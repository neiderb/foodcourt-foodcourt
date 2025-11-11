package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.impl;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.PaginationFilterMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationFilterMapperImpl implements PaginationFilterMapper {
	
	@Override
	public Pageable toPageable(PaginationFilter filter) {
		return PageRequest.of(
			filter.getPage(),
			filter.getSize(),
			filter.isAscending()
				? Sort.by(filter.getSortBy()).ascending()
				: Sort.by(filter.getSortBy()).descending()
		);
	}
	
}
