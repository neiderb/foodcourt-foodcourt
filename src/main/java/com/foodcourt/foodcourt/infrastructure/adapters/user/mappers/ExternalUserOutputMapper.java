package com.foodcourt.foodcourt.infrastructure.adapters.user.mappers;

import com.foodcourt.foodcourt.domain.model.auth.User;
import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.UserExternalResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

@Mapper
public interface ExternalUserOutputMapper {
	
	ExternalUserOutputMapper INSTANCE = Mappers.getMapper(ExternalUserOutputMapper.class);
	
	@Mapping(target = "role", ignore = true)
	User toDomain(UserExternalResponse response);
	
	@AfterMapping
	default void mapRole(UserExternalResponse response, @MappingTarget User user) {
		String role = response.role();
		if (StringUtils.hasText(role)) {
			user.setRole(UserRole.getRoleof(role));
		}
	}
	
}
