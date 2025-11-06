package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.gateways.DishRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Dish;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.CategoryJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.DishJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.DishMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.foodcourt.foodcourt.domain.constants.DishErrorMessage.INVALID_CATEGORY;

@Repository
@RequiredArgsConstructor
public class DishRepositoryAdapter implements DishRepositoryGateway {
	
	private final DishJpaRepository dishJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;
	
	@Override
	public Dish save(Dish dish) {
		if (!categoryJpaRepository.existsById(dish.getIdCategory()))
			throw new TechnicalException(INVALID_CATEGORY);
		
		return DishMapper.INSTANCE.toDomain(
			dishJpaRepository.save(DishMapper.INSTANCE.toData(dish))
		);
	}
	
	@Override
	public Dish findById(Long idDish) {
		return DishMapper.INSTANCE.toDomain(
			dishJpaRepository.findById(idDish).orElse(null)
		);
	}
}
