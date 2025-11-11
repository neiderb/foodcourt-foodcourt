package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.restaurant.Restaurant;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantPaginationFilter;
import com.foodcourt.foodcourt.domain.model.restaurant.RestaurantSummary;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.RestaurantJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.PaginationFilterMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.RestaurantMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.RestaurantSummaryProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_ALREADY_EXISTS;
import static java.util.Objects.isNull;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepositoryGateway {
	
	private final RestaurantJpaRepository restaurantJpaRepository;
	private final PaginationFilterMapper paginationFilterMapper;
	
	@Override
	public Restaurant save(Restaurant restaurant) {
		log.trace("Validating restaurant before saving: {}", restaurant);
		if (existByNameOrNit(restaurant.getName(), restaurant.getNit()))
			throw new InvalidRestaurantException(RESTAURANT_ALREADY_EXISTS);
		
		log.trace("Saving restaurant");
		return RestaurantMapper.INSTANCE.toDomain(
			restaurantJpaRepository.save(
				RestaurantMapper.INSTANCE.toData(restaurant)
			)
		);
	}
	
	@Override
	public boolean existByNameOrNit(String name, String nit) {
		log.trace("Checking existence of restaurant by name: {} or NIT: {}", name, nit);
		return restaurantJpaRepository.existsByNameIgnoreCaseOrNit(name, nit);
	}
	
	@Override
	public Restaurant findById(Long id) {
		log.trace("Finding restaurant by ID: {}", id);
		return RestaurantMapper.INSTANCE.toDomain(
			restaurantJpaRepository.findById(id).orElse(null)
		);
	}
	
	@Override
	public boolean isRestaurantOwner(Long idRestaurant, Long idUser) {
		log.trace("Checking if user with ID: {} is owner of restaurant with ID: {}", idUser, idRestaurant);
		RestaurantData existingRestaurant = restaurantJpaRepository.findById(idRestaurant).orElse(null);
		if (isNull(existingRestaurant)) return false;
		log.debug("Found restaurant: {}", existingRestaurant);
		return existingRestaurant.getOwnerId().equals(idUser);
	}
	
	@Override
	public PaginationResponse<RestaurantSummary> getAllRestaurantSummaries(RestaurantPaginationFilter filter) {
		Pageable pageable = paginationFilterMapper.toPageable(filter);
		
		Page<RestaurantSummaryProjection> resultPage = restaurantJpaRepository.findRestaurantPaginatedBy(pageable);
		log.debug("Retrieved restaurant summaries page {} with {} elements", resultPage.getNumber(), resultPage.getContent().size());
		return mapToPaginationResponse(resultPage);
	}
	
	@Override
	public boolean existById(Long id) {
		return restaurantJpaRepository.existsById(id);
	}
	
	private PaginationResponse<RestaurantSummary> mapToPaginationResponse(Page<RestaurantSummaryProjection> page) {
		return PaginationResponse.<RestaurantSummary>builder()
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.pageNumber(page.getNumber())
			.pageSize(page.getSize())
			.content(
				page.map(projection -> RestaurantSummary.builder()
					.idRestaurant(projection.getId())
					.name(projection.getName())
					.urlLogo(projection.getUrlLogo())
					.build()
				).getContent()
			)
			.build();
	}
}
