package com.foodcourt.foodcourt.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant")
public class RestaurantData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String nit;
	
	private String address;
	
	private String phone;
	
	@Column(name = "url_logo")
	private String urlLogo;
	
	@Column(name = "id_owner")
	private Long ownerId;
	
}
