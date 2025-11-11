package com.foodcourt.foodcourt.infrastructure.adapters.persistence.dto;

import lombok.AllArgsConstructor;

import static java.util.Objects.isNull;

@AllArgsConstructor
public class DishFilter {
	
	private Long idRestaurant;
	private Long idCategory;
	
	public long getIdRestaurant() {
		return isNull(idRestaurant) || idRestaurant < 0 ? 0 : idRestaurant;
	}
	
	public long getIdCategory() {
		return isNull(idCategory) || idCategory < 0 ? 0 : idCategory;
	}
}
