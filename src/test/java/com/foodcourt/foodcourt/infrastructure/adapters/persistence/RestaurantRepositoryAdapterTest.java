package com.foodcourt.foodcourt.infrastructure.adapters.persistence;

import com.foodcourt.foodcourt.domain.exception.InvalidRestaurantException;
import com.foodcourt.foodcourt.domain.model.Restaurant;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities.RestaurantData;
import com.foodcourt.foodcourt.infrastructure.adapters.persistence.jpa.RestaurantJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryAdapterTest {
	
	@InjectMocks
	private RestaurantRepositoryAdapter restaurantRepositoryAdapter;
	
	@Mock
	private RestaurantJpaRepository restaurantJpaRepository;
	
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
		
		when(restaurantJpaRepository.findByName(restaurantToSave.getName())).thenReturn(null);
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
		RestaurantData existingRestaurantData = validRestaurantData();
		
		when(restaurantJpaRepository.findByName(restaurantToSave.getName())).thenReturn(existingRestaurantData);
		
		assertThrows(InvalidRestaurantException.class, () -> restaurantRepositoryAdapter.save(restaurantToSave));
	}
	
	@Test
	void shouldFindRestaurantByNameSuccessfully() {
		RestaurantData testRestaurantData = validRestaurantData();
		
		when(restaurantJpaRepository.findByName(testRestaurantData.getName())).thenReturn(testRestaurantData);
		
		Restaurant foundRestaurant = restaurantRepositoryAdapter.findByName(RESTAURANT_NAME);
		
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
	void shouldReturnNullWhenRestaurantNotFoundByName() {
		when(restaurantJpaRepository.findByName("NonExistentRestaurant")).thenReturn(null);
		
		Restaurant foundRestaurant = restaurantRepositoryAdapter.findByName("NonExistentRestaurant");
		
		assertNull(foundRestaurant);
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
}
