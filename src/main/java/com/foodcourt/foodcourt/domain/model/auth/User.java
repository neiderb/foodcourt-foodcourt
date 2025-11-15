package com.foodcourt.foodcourt.domain.model.auth;

import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
	private Long id;
	private String email;
	private UserRole role;
	private String phone;
}
