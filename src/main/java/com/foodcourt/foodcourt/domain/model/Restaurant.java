package com.foodcourt.foodcourt.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
	
	private Long id;
	private String name;
	private String nit;
	private String address;
	private String phoneNumber;
	private String urlLogo;
	private Long ownerId;
	
}
