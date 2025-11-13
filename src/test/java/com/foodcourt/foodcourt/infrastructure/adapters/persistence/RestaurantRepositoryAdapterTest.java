package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.RestaurantJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.PaginationFilterMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.RestaurantSummaryProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.foodcourt.foodcourt.domain.model.restaurant.enums.RestaurantSortBy.NAME;
import static com.foodcourt.foodcourt.domain.model.restaurant.enums.RestaurantSortBy.URL_LOGO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryAdapterTest {
	
	@InjectMocks
	private RestaurantRepositoryAdapter restaurantRepositoryAdapter;
	
	@Mock
	private RestaurantJpaRepository restaurantJpaRepository;
	
	@Mock
	private PaginationFilterMapper paginationFilterMapper;

	private static final Long RESTAURANT_ID = 1L;
	private static final String RESTAURANT_NAME = "Gourmet Bites";
	private static final String RESTAURANT_NIT = "987654321";
	private static final String RESTAURANT_ADDRESS = "123 Food St, Flavor Town";
	private static final String RESTAURANT_PHONE_NUMBER = "+1234567890";
	private static final String RESTAURANT_URL_LOGO = "http://example.com/logo.png";
	private static final Long RESTAURANT_OWNER_ID = 10L;
	
	@Test
	void shouldSaveRestaurantSuccessfully() {
		Restaurant restaurantToSave = validRestaurant();
		restaurantToSave.setId(null);
		RestaurantData testRestaurantData = validRestaurantData();
		
		when(restaurantJpaRepository.existsByNameIgnoreCaseOrNit(any(String.class), any(String.class))).thenReturn(false);
		when(restaurantJpaRepository.save(any(RestaurantData.class))).thenReturn(testRestaurantData);
		
		Restaurant savedRestaurant = restaurantRepositoryAdapter.save(restaurantToSave);
		
		assertNotNull(savedRestaurant);
		assertNotNull(savedRestaurant.getId());
		assertEquals(savedRestaurant.getName(), restaurantToSave.getName());
		assertEquals(savedRestaurant.getNit(), restaurantToSave.getNit());
		assertEquals(savedRestaurant.getAddress(), restaurantToSave.getAddress());
		assertEquals(savedRestaurant.getPhoneNumber(), restaurantToSave.getPhoneNumber());
		assertEquals(savedRestaurant.getUrlLogo(), restaurantToSave.getUrlLogo());
		assertEquals(savedRestaurant.getOwnerId(), restaurantToSave.getOwnerId());
	}
	
	@Test
	void shouldThrowExceptionWhenSavingRestaurantWithExistingName() {
		Restaurant restaurantToSave = validRestaurant();
		
		when(restaurantJpaRepository.existsByNameIgnoreCaseOrNit(any(String.class), any(String.class))).thenReturn(true);
		
		assertThrows(InvalidRestaurantException.class, () -> restaurantRepositoryAdapter.save(restaurantToSave));
	}
	
	@Test
	void shouldFindRestaurantByIdSuccessfully() {
		RestaurantData testRestaurantData = validRestaurantData();
		
		when(restaurantJpaRepository.findById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurantData));
		
		Restaurant foundRestaurant = restaurantRepositoryAdapter.findById(RESTAURANT_ID);
		
		assertNotNull(foundRestaurant);
		assertEquals(foundRestaurant.getId(), testRestaurantData.getId());
		assertEquals(foundRestaurant.getName(), testRestaurantData.getName());
		assertEquals(foundRestaurant.getNit(), testRestaurantData.getNit());
		assertEquals(foundRestaurant.getAddress(), testRestaurantData.getAddress());
		assertEquals(foundRestaurant.getPhoneNumber(), testRestaurantData.getPhone());
		assertEquals(foundRestaurant.getUrlLogo(), testRestaurantData.getUrlLogo());
		assertEquals(foundRestaurant.getOwnerId(), testRestaurantData.getOwnerId());
	}
	
	@Test
	void shouldReturnNullWhenRestaurantNotFoundById() {
		when(restaurantJpaRepository.findById(999L)).thenReturn(Optional.empty());
		
		Restaurant foundRestaurant = restaurantRepositoryAdapter.findById(999L);
		
		assertNull(foundRestaurant);
	}
	
	@Test
	void shouldVerifyRestaurantOwnershipSuccessfully() {
		RestaurantData testRestaurantData = validRestaurantData();
		
		when(restaurantJpaRepository.findById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurantData));
		
		boolean isOwner = restaurantRepositoryAdapter.isRestaurantOwner(RESTAURANT_ID, RESTAURANT_OWNER_ID);
		
		assertTrue(isOwner);
	}
	
	@Test
	void shouldReturnFalseWhenRestaurantNotFound() {
		final Long idRestaurant = RESTAURANT_ID;
		when(restaurantJpaRepository.findById(idRestaurant)).thenReturn(Optional.empty());

		boolean isOwner = restaurantRepositoryAdapter.isRestaurantOwner(idRestaurant, 999L);

		assertFalse(isOwner);
	}

	@Test
	void shouldReturnPaginatedRestaurantSummaries() {
		RestaurantPaginationFilter filter = RestaurantPaginationFilter.builder()
			.page(0)
			.size(2)
			.build();
		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
		
		List<RestaurantSummaryProjection> projections = mockSummaryResults();
		PageImpl<RestaurantSummaryProjection> page = new PageImpl<>(projections, pageable, projections.size());
		when(restaurantJpaRepository.findRestaurantPaginatedBy(any(Pageable.class))).thenReturn(page);
		when(paginationFilterMapper.toPageable(any(RestaurantPaginationFilter.class))).thenReturn(pageable);
		
		PaginationResponse<RestaurantSummary> response = restaurantRepositoryAdapter.getAllRestaurantSummaries(filter);
		
		assertNotNull(response);
		assertEquals(page.getNumber(), response.getPageNumber());
		assertEquals(page.getSize(), response.getPageSize());
		assertEquals(page.getTotalElements(), response.getTotalElements());
		assertEquals(page.getTotalPages(), response.getTotalPages());
		assertEquals(projections.size(), response.getContent().size());
		assertEquals(1L, response.getContent().getFirst().getIdRestaurant());
		assertEquals("Resto 1", response.getContent().getFirst().getName());
	}
	
	@Test
	void shouldReturnPaginatedRestaurantSummariesWithDifferentOrder() {
		RestaurantPaginationFilter filter = RestaurantPaginationFilter.builder()
			.page(0)
			.size(2)
			.sortBy("order-property")
			.sortDirection("desc")
			.build();
		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
		
		List<RestaurantSummaryProjection> projections = mockSummaryResults().reversed();
		PageImpl<RestaurantSummaryProjection> page = new PageImpl<>(projections, pageable, projections.size());
		when(restaurantJpaRepository.findRestaurantPaginatedBy(any(Pageable.class))).thenReturn(page);
		when(paginationFilterMapper.toPageable(any(RestaurantPaginationFilter.class))).thenReturn(pageable);
		
		PaginationResponse<RestaurantSummary> response = restaurantRepositoryAdapter.getAllRestaurantSummaries(filter);
		
		assertNotNull(response);
		assertEquals(page.getNumber(), response.getPageNumber());
		assertEquals(page.getSize(), response.getPageSize());
		assertEquals(page.getTotalElements(), response.getTotalElements());
		assertEquals(page.getTotalPages(), response.getTotalPages());
		assertEquals(projections.size(), response.getContent().size());
		assertEquals(2L, response.getContent().getFirst().getIdRestaurant());
		assertEquals("Resto 2", response.getContent().getFirst().getName());
		assertEquals("url2", response.getContent().getFirst().getUrlLogo());
		
		verify(paginationFilterMapper).toPageable(assertArg(filterArg -> assertEquals(NAME.getValue(), filterArg.getSortBy())));
	}

	@Test
	void shouldReturnEmptyPaginationWhenNoRestaurants() {
		RestaurantPaginationFilter filter = RestaurantPaginationFilter.builder()
			.page(0)
			.size(5)
			.sortBy("urlLogo")
			.build();
		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
		
		PageImpl<RestaurantSummaryProjection> emptyPage = new PageImpl<>(List.of(), pageable, 0);
		when(restaurantJpaRepository.findRestaurantPaginatedBy(any(Pageable.class))).thenReturn(emptyPage);
		when(paginationFilterMapper.toPageable(any(RestaurantPaginationFilter.class))).thenReturn(pageable);
		
		PaginationResponse<RestaurantSummary> response = restaurantRepositoryAdapter.getAllRestaurantSummaries(filter);
		
		assertNotNull(response);
		assertEquals(0, response.getContent().size());
		assertEquals(0, response.getTotalElements());
		
		verify(paginationFilterMapper).toPageable(assertArg(filterArg -> assertEquals(URL_LOGO.getValue(), filterArg.getSortBy())));
	}
	
	private Restaurant validRestaurant() {
		return Restaurant.builder()
			.id(RESTAURANT_ID)
			.name(RESTAURANT_NAME)
			.nit(RESTAURANT_NIT)
			.address(RESTAURANT_ADDRESS)
			.phoneNumber(RESTAURANT_PHONE_NUMBER)
			.urlLogo(RESTAURANT_URL_LOGO)
			.ownerId(RESTAURANT_OWNER_ID)
			.build();
	}
	
	private RestaurantData validRestaurantData() {
		return RestaurantData.builder()
			.id(RESTAURANT_ID)
			.name(RESTAURANT_NAME)
			.nit(RESTAURANT_NIT)
			.address(RESTAURANT_ADDRESS)
			.phone(RESTAURANT_PHONE_NUMBER)
			.urlLogo(RESTAURANT_URL_LOGO)
			.ownerId(RESTAURANT_OWNER_ID)
			.build();
	}
	
	private List<RestaurantSummaryProjection> mockSummaryResults() {
		RestaurantSummaryProjection p1 = mock(RestaurantSummaryProjection.class);
		when(p1.getId()).thenReturn(1L);
		when(p1.getName()).thenReturn("Resto 1");
		when(p1.getUrlLogo()).thenReturn("url1");
		
		RestaurantSummaryProjection p2 = mock(RestaurantSummaryProjection.class);
		when(p2.getId()).thenReturn(2L);
		when(p2.getName()).thenReturn("Resto 2");
		when(p2.getUrlLogo()).thenReturn("url2");
		
		return List.of(p1, p2);
	}
}
