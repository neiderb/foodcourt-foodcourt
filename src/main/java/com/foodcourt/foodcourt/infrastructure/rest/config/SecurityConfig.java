package com.foodcourt.foodcourt.infrastructure.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.foodcourt.infrastructure.rest.constants.paths.DishPath;
import com.foodcourt.foodcourt.infrastructure.rest.constants.paths.RestaurantPath;
import com.foodcourt.foodcourt.infrastructure.rest.dto.ErrorApiResponse;
import com.foodcourt.foodcourt.infrastructure.rest.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.foodcourt.foodcourt.domain.model.auth.UserRole.*;
import static com.foodcourt.foodcourt.infrastructure.rest.constants.ErrorMessage.ACCESS_DENIED;
import static com.foodcourt.foodcourt.infrastructure.rest.constants.ErrorMessage.UNAUTHORIZED;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtFilter jwtFilter;
	private final ObjectMapper objectMapper;
	
	private static final String[] ALLOWED_PATHS_SWAGGER = {
		"/v3/api-docs/**",
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/webjars/**"
	};
	private static final String[] ALLOWED_PATHS_ACTUATOR = {
		"/actuator/health",
	};
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(ALLOWED_PATHS_SWAGGER).permitAll()
				.requestMatchers(ALLOWED_PATHS_ACTUATOR).permitAll()
				.requestMatchers(POST, RestaurantPath.BASE).hasRole(ADMIN.name())
				.requestMatchers(GET, RestaurantPath.BASE.concat(RestaurantPath.FIND_BY_ID))
					.hasAnyRole(ADMIN.name(), OWNER.name())
				.requestMatchers(GET, RestaurantPath.BASE).hasRole(CLIENT.name())
				.requestMatchers(POST, DishPath.BASE).hasRole(OWNER.name())
				.requestMatchers(PATCH, DishPath.BASE).hasRole(OWNER.name())
				.requestMatchers(PATCH, DishPath.BASE.concat(DishPath.TOGGLE_AVAILABILITY)).hasRole(OWNER.name())
				.requestMatchers(GET, DishPath.BASE.concat(DishPath.BY_RESTAURANT)).hasRole(CLIENT.name())
				.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(handling -> handling
				.authenticationEntryPoint(jwtAuthenticationEntryPoint())
				.accessDeniedHandler(jwtAccessDeniedHandler())
			);
			
		return http.build();
	}
	
	public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		final int status = HttpStatus.UNAUTHORIZED.value();
		return (request, response, authException) -> {
			response.setStatus(status);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ErrorApiResponse errorData = new ErrorApiResponse(
				status,
				UNAUTHORIZED
			);
			
			objectMapper.writeValue(response.getOutputStream(), errorData);
		};
	}
	
	public AccessDeniedHandler jwtAccessDeniedHandler() {
		final int status = HttpStatus.FORBIDDEN.value();
		return (request, response, accessDeniedException) -> {
			response.setStatus(status);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ErrorApiResponse errorData = new ErrorApiResponse(
				status,
				ACCESS_DENIED
			);
			
			objectMapper.writeValue(response.getOutputStream(), errorData);
		};
	}
	
}
