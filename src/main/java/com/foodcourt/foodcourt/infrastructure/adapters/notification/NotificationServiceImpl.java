package com.foodcourt.foodcourt.infrastructure.adapters.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.foodcourt.domain.exception.BusinessException;
import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.gateways.NotificationServiceGateway;
import com.foodcourt.foodcourt.infrastructure.adapters.notification.dto.ErrorExternalResponse;
import com.foodcourt.foodcourt.infrastructure.adapters.notification.dto.NotificationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.function.Consumer;

import static com.foodcourt.foodcourt.infrastructure.adapters.notification.constants.ErrorMessage.EXTERNAL_SERVICE_ERROR;
import static com.foodcourt.foodcourt.infrastructure.adapters.notification.constants.ErrorMessage.UNMAPPING_RESPONSE;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationServiceGateway {
	
	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	
	public NotificationServiceImpl(
		@Qualifier("notificationServiceClient")
		RestClient restClient,
		ObjectMapper objectMapper
	) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void sendOrderCompletedNotification(String phoneNumber, String securePin) {
		log.trace("Sending order completed notification to phone number: {}", phoneNumber);
		restClient.post()
			.uri("/api/v1/notification")
			.headers(buildHeaders())
			.body(new NotificationInfo(
				phoneNumber,
				securePin
			))
			.retrieve()
			.onStatus(
				HttpStatusCode::is4xxClientError, (req, res) -> {
					ErrorExternalResponse error = mapErrorResponse(res);
					throw new BusinessException(error.message());
				})
			.onStatus(HttpStatusCode::isError, (req, res) -> {
				throw new TechnicalException(EXTERNAL_SERVICE_ERROR);
			})
			.toBodilessEntity();
		log.debug("Order completed notification sent to phone number: {}", phoneNumber);
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
