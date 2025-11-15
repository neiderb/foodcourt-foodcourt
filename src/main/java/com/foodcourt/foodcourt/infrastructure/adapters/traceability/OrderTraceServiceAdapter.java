package com.foodcourt.foodcourt.infrastructure.adapters.traceability;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.foodcourt.domain.exception.BusinessException;
import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.gateways.TraceServiceGateway;
import com.foodcourt.foodcourt.domain.model.order.OrderTrace;
import com.foodcourt.foodcourt.infrastructure.adapters.notification.dto.ErrorExternalResponse;
import com.foodcourt.foodcourt.infrastructure.adapters.traceability.dto.ExternalOrderTraceInput;
import com.foodcourt.foodcourt.infrastructure.adapters.traceability.mappers.ExternalOrderTraceInputMapper;
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
public class OrderTraceServiceAdapter implements TraceServiceGateway {
	
	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	
	public OrderTraceServiceAdapter(
		@Qualifier("traceabilityServiceClient")
		RestClient restClient,
		ObjectMapper objectMapper
	) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void saveOrderTrace(OrderTrace orderTrace) {
		log.trace("Saving order trace: {}", orderTrace);
		ExternalOrderTraceInput body = ExternalOrderTraceInputMapper.INSTANCE.toDto(
			orderTrace
		);
		
		restClient.post()
			.uri("/api/v1/trace")
			.headers(buildHeaders())
			.body(body)
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
		log.debug("Order trace saved successfully for order ID: {}", orderTrace.idOrder());
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
