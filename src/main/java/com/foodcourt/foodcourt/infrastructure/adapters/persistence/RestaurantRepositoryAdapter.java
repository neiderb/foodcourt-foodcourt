package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.restaurant.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.gateways.RestaurantRepositoryGateway;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.RestaurantJpaRepository;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.mappers.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.foodcourt.foodcourt.domain.constants.RestaurantErrorMessage.RESTAURANT_ALREADY_EXISTS;
import static java.util.Objects.isNull;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepositoryGateway {
	
	private final RestaurantJpaRepository restaurantJpaRepository;
	
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
		RestaurantData existingRestaurant = restaurantJpaRepository.findById(idRestaurant).orElse(null);
		if (isNull(existingRestaurant)) return false;
		return existingRestaurant.getOwnerId().equals(idUser);
	}
}
