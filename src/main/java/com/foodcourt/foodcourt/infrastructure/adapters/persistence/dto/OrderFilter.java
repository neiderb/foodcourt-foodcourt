package com.foodcourt.foodcourt.infrastructure.adapters.persistence.dto;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static java.util.Objects.isNull;

@AllArgsConstructor
public class OrderFilter {
	
	private Long idRestaurant;
	private String status;
	
	public Long getIdRestaurant() {
		return isNull(this.idRestaurant) ? 0L : this.idRestaurant;
	}
	
	public String getStatus() {
		return isNull(this.status) ? StringUtils.EMPTY : this.status;
	}
	
}
