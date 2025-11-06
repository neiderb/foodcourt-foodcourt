package com.foodcourt.foodcourt.infrastructure.adapters.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.foodcourt.domain.exception.BusinessException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.User;
import com.foodcourt.foodcourt.domain.model.UserRole;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.ErrorExternalResponse;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.UserExternalResponse;
import com.foodcourt.foodcourt.infrastructure.adapters.user.mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.USER_NOT_FOUND;
import static com.foodcourt.foodcourt.infrastructure.adapters.user.constants.ErrorMessage.EXTERNAL_SERVICE_ERROR;
import static com.foodcourt.foodcourt.infrastructure.adapters.user.constants.ErrorMessage.UNMAPPING_RESPONSE;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class UserServiceImpl implements UserServiceGateway {
	
	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	
	public UserServiceImpl(
		@Qualifier("authServiceClient")
		RestClient restClient,
		ObjectMapper objectMapper
	) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public User findById(Long id) {
		UserExternalResponse response = restClient.get()
			.uri("/api/v1/user/{id}", id)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
				ErrorExternalResponse error = mapErrorResponse(res);
				throw new BusinessException(error.message());
			})
			.onStatus(HttpStatusCode::isError, (req, res) -> {
				throw new TechnicalException(EXTERNAL_SERVICE_ERROR);
			})
			.body(UserExternalResponse.class);
		
		if (isNull(response)) throw new InvalidUserException(USER_NOT_FOUND);
		return mapToDomain(response);
	}
	
	private ErrorExternalResponse mapErrorResponse(ClientHttpResponse response) {
		try {
			return objectMapper.readValue(
				response.getBody(),
				ErrorExternalResponse.class
			);
		} catch (Exception e) {
			log.error("Error mapping error response", e);
			throw new TechnicalException(UNMAPPING_RESPONSE);
		}
	}
	
	private User mapToDomain(UserExternalResponse response) {
		UserRole role = UserRole.getRoleof(response.role());
		User user = UserMapper.INSTANCE.toDomain(response);
		user.setRole(role);
		return user;
	}
}
