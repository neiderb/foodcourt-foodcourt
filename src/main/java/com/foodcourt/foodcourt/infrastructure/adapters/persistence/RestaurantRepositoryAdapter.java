package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.RestaurantJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_ALREADY_EXISTS;
import static java.util.Objects.nonNull;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepositoryGateway {
	
	private final RestaurantJpaRepository restaurantJpaRepository;
	
	@Override
	public Restaurant save(Restaurant restaurant) {
		Restaurant existingRestaurant = findByName(restaurant.getName());
		if (nonNull(existingRestaurant)) throw new InvalidRestaurantException(RESTAURANT_ALREADY_EXISTS);
		
		return RestaurantMapper.INSTANCE.toDomain(
			restaurantJpaRepository.save(
				RestaurantMapper.INSTANCE.toData(restaurant)
			)
		);
	}
	
	@Override
	public Restaurant findByName(String name) {
		return RestaurantMapper.INSTANCE.toDomain(
			restaurantJpaRepository.findByName(name)
		);
	}
	
	@Override
	public Restaurant findById(Long id) {
		return RestaurantMapper.INSTANCE.toDomain(
			restaurantJpaRepository.findById(id).orElse(null)
		);
	}
}
