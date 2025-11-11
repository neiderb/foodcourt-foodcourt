package com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.impl;

import com.foodcourt.foodcourt.domain.model.PaginationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaginationFilterMapperImplTest {
	
	@InjectMocks
	private PaginationFilterMapperImpl paginationFilterMapper;
	
	@Mock
	private PaginationFilter paginationFilter;
	
	@Test
	void toPageable_shouldMapAscending() {
		when(paginationFilter.getPage()).thenReturn(1);
		when(paginationFilter.getSize()).thenReturn(20);
		when(paginationFilter.isAscending()).thenReturn(true);
		when(paginationFilter.getSortBy()).thenReturn("name");
		
		Pageable pageable = paginationFilterMapper.toPageable(paginationFilter);
		
		assertEquals(1, pageable.getPageNumber());
		assertEquals(20, pageable.getPageSize());
		assertTrue(pageable.getSort().isSorted());
		Sort.Order order = pageable.getSort().getOrderFor("name");
		assertNotNull(order);
		assertEquals(Sort.Direction.ASC, order.getDirection());
	}
	
	@Test
	void toPageable_shouldMapDescending() {
		when(paginationFilter.getPage()).thenReturn(0);
		when(paginationFilter.getSize()).thenReturn(10);
		when(paginationFilter.isAscending()).thenReturn(false);
		when(paginationFilter.getSortBy()).thenReturn("name");
		
		Pageable pageable = paginationFilterMapper.toPageable(paginationFilter);
		
		assertEquals(0, pageable.getPageNumber());
		assertEquals(10, pageable.getPageSize());
		assertTrue(pageable.getSort().isSorted());
		Sort.Order order = pageable.getSort().getOrderFor("name");
		assertNotNull(order);
		assertEquals(Sort.Direction.DESC, order.getDirection());
	}
	
}
