package com.foodcourt.foodcourt.infrastructure.adapters.traceability.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class TraceabilityServiceConfig {
	
	@Value("${external-api.traceability-service.url}")
	private String traceabilityApiUrl;
	
	@Bean
	public RestClient traceabilityServiceClient() {
		return RestClient.builder()
			.baseUrl(traceabilityApiUrl)
			.defaultHeader(
				HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON_VALUE
			)
			.build();
	}
	
}
