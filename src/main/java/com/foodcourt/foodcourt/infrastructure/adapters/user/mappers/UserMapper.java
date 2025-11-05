package com.foodcourt.foodcourt.infrastructure.adapters.user.mappers;

import com.foodcourt.foodcourt.domain.model.User;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.UserExternalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
	
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	@Mapping(target = "role", ignore = true)
	User toDomain(UserExternalResponse response);
	
}
