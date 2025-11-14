package com.foodcourt.foodcourt.infrastructure.adapters.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class NotificationServiceConfig {
	
	@Value("${external-api.notification-service.url}")
	private String notificationServiceUrl;
	
	@Bean
	public RestClient notificationServiceClient(RestClient.Builder builder) {
		return builder
			.baseUrl(notificationServiceUrl)
			.defaultHeader(
				HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON_VALUE
			)
			.build();
	}
	
}
