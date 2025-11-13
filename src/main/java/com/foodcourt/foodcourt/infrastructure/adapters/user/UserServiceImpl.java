package com.foodcourt.foodcourt.infrastructure.adapters.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.foodcourt.domain.exception.BusinessException;
import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.exception.auth.InvalidUserException;
import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.auth.enums.UserRole;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.ErrorExternalResponse;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.UserExternalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.function.Consumer;

import static com.foodcourt.foodcourt.domain.constants.AuthErrorMessage.USER_NOT_FOUND;
import static com.foodcourt.foodcourt.domain.model.auth.enums.UserRole.CLIENT;
import static com.foodcourt.foodcourt.domain.model.auth.enums.UserRole.OWNER;
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
	public boolean isOwner(Long idUser) {
		UserExternalResponse user = findById(idUser);
		if (isNull(user)) throw new InvalidUserException(USER_NOT_FOUND);
		return OWNER.equals(UserRole.getRoleof(user.role()));
	}
	
	@Override
	public boolean isClient(Long idUser) {
		UserExternalResponse user = findById(idUser);
		if (isNull(user)) throw new InvalidUserException(USER_NOT_FOUND);
		return CLIENT.equals(UserRole.getRoleof(user.role()));
	}
	
	@Override
	public String getUserPhone(Long idUser) {
		UserExternalResponse user = findById(idUser);
		if (isNull(user)) throw new InvalidUserException(USER_NOT_FOUND);
		return user.phone();
	}
	
	private UserExternalResponse findById(Long id) {
		return restClient.get()
			.uri("/api/v1/user/{id}", id)
			.headers(buildHeaders())
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
				ErrorExternalResponse error = mapErrorResponse(res);
				throw new BusinessException(error.message());
			})
			.onStatus(HttpStatusCode::isError, (req, res) -> {
				throw new TechnicalException(EXTERNAL_SERVICE_ERROR);
			})
			.body(UserExternalResponse.class);
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
	
	private Consumer<HttpHeaders> buildHeaders() {
		return headers -> {
			String token = getToken();
			if (token != null) {
				headers.set(HttpHeaders.AUTHORIZATION, token);
			}
		};
	}
	
	private String getToken() {
		String token = null;
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if (credentials instanceof String strToken) {
			token = "Bearer ".concat(strToken);
		}
		
		return token;
	}
}
