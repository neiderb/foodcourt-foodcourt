package com.foodcourt.foodcourt.domain.model.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	private Long id;
	private String name;
	private UserRole role;
	
}
