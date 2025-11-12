package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.PaginationResponse;
import com.foodcourt.foodcourt.domain.model.dish.Dish;
import com.foodcourt.foodcourt.domain.model.dish.DishPaginationFilter;
import com.foodcourt.foodcourt.domain.model.dish.DishSummary;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.dto.DishFilter;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.CategoryJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.DishJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.DishMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.PaginationFilterMapper;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.projection.DishSummaryProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static com.foodcourt.foodcourt.domain.constants.DishErrorMessage.INVALID_CATEGORY;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DishRepositoryAdapter implements DishRepositoryGateway {
	
	private final DishJpaRepository dishJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;
	private final PaginationFilterMapper paginationFilterMapper;
	
	@Override
	public Dish save(Dish dish) {
		if (!categoryJpaRepository.existsById(dish.getIdCategory()))
			throw new TechnicalException(INVALID_CATEGORY);
	
		log.trace("Saving dish with name: {}", dish.getName());
		return DishMapper.INSTANCE.toDomain(
			dishJpaRepository.save(DishMapper.INSTANCE.toData(dish))
		);
	}
	
	@Override
	public Dish findById(Long idDish) {
		log.trace("Finding dish by ID: {}", idDish);
		return DishMapper.INSTANCE.toDomain(
			dishJpaRepository.findById(idDish).orElse(null)
		);
	}
	
	@Override
	public PaginationResponse<DishSummary> getDishesSummaryByIdRestaurant(Long idRestaurant, DishPaginationFilter filter) {
		Pageable pageable = paginationFilterMapper.toPageable(filter);
		DishFilter dishFilter = new DishFilter(idRestaurant, filter.getIdCategory());
		
		Page<DishSummaryProjection> resultPage = dishJpaRepository.findDishPaginatedBy(pageable, dishFilter);
		log.debug("Retrieved {} dishes for restaurant ID: {}", resultPage.getTotalElements(), idRestaurant);
		return mapToPaginationResponse(resultPage);
	}
	
	@Override
	public boolean existAllByIdsInAndRestaurantId(Set<Long> ids, Long idRestaurant) {
		return dishJpaRepository.existsAllByIdInAndIdRestaurantAndIsAvailableIsTrue(ids, idRestaurant);
	}
	
	private PaginationResponse<DishSummary> mapToPaginationResponse(Page<DishSummaryProjection> page) {
		return PaginationResponse.<DishSummary>builder()
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.pageNumber(page.getNumber())
			.pageSize(page.getSize())
			.content(
				page.map(projection -> DishSummary.builder()
					.id(projection.getId())
					.name(projection.getName())
					.price(projection.getPrice())
					.categoryName(projection.getCategoryName())
					.imageUrl(projection.getImageUrl())
					.build()
				).getContent()
			)
			.build();
	}
	
}
