package com.foodcourt.foodcourt.infrastructure.adapters.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class UserServiceConfig {
	
	@Value("${external-api.auth-service.url}")
	private String authServiceUrl;
	
	@Bean
	public RestClient authServiceClient(RestClient.Builder builder) {
		return builder
			.baseUrl(authServiceUrl)
			.defaultHeader(
				HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON_VALUE
			)
			.build();
	}
}
