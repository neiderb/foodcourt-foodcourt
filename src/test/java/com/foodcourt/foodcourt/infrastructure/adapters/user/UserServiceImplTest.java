package com.foodcourt.foodcourt.infrastructure.adapters.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.foodcourt.domain.exception.BusinessException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidRoleException;
import com.foodcourt.foodcourt.domain.exception.user.InvalidUserException;
import com.foodcourt.foodcourt.domain.exception.TechnicalException;
import com.foodcourt.foodcourt.domain.model.auth.User;
import com.foodcourt.foodcourt.domain.model.auth.UserRole;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.ErrorExternalResponse;
import com.foodcourt.foodcourt.infrastructure.adapters.user.dto.UserExternalResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"rawtypes", "unchecked"})
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Mock
	private RestClient restClient;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private RestClient.RequestHeadersSpec requestHeadersSpec;

	@Mock
	private RestClient.ResponseSpec responseSpec;

	@BeforeEach
	void setUpSecurityContext() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			"test-user",
			"dummy-token",
			Collections.emptyList()
		);
		SecurityContextHolder.getContext().setAuthentication(auth);

		when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
	}

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void shouldFindUserByIdSuccessfully() {
		Long id = 1L;
		UserExternalResponse externalResponse = validUserExternalResponse();
		User expectedUser = validOwner();
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(UserExternalResponse.class)).thenReturn(externalResponse);
		
		User foundUser = userServiceImpl.findById(id);
		
		assertNotNull(foundUser);
		assertEquals(id, foundUser.getId());
		assertEquals(expectedUser.getName(), foundUser.getName());
		assertEquals(expectedUser.getRole(), foundUser.getRole());
	}

	@Test
	void shouldAddAuthorizationHeaderWhenTokenPresent() {
		Long id = 1L;
		UserExternalResponse externalResponse = validUserExternalResponse();
		
		AtomicReference<Consumer<HttpHeaders>> headersConsumer = new AtomicReference<>();
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.headers(any())).thenAnswer(invocation -> {
			headersConsumer.set(invocation.getArgument(0));
			return requestHeadersSpec;
		});
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(UserExternalResponse.class)).thenReturn(externalResponse);
		
		userServiceImpl.findById(id);
		
		HttpHeaders headers = new HttpHeaders();
		Consumer<HttpHeaders> consumer = headersConsumer.get();
		assertNotNull(consumer);
		consumer.accept(headers);
		assertEquals("Bearer dummy-token", headers.getFirst(HttpHeaders.AUTHORIZATION));
	}

	@Test
	void shouldNotAddAuthorizationHeaderWhenCredentialsNotString() {
		Long id = 1L;
		UserExternalResponse externalResponse = validUserExternalResponse();
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			"test-user",
			12345,
			Collections.emptyList()
		);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		AtomicReference<Consumer<HttpHeaders>> headersConsumer = new AtomicReference<>();
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.headers(any())).thenAnswer(invocation -> {
			headersConsumer.set(invocation.getArgument(0));
			return requestHeadersSpec;
		});
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(UserExternalResponse.class)).thenReturn(externalResponse);
		
		userServiceImpl.findById(id);
		
		HttpHeaders headers = new HttpHeaders();
		Consumer<HttpHeaders> consumer = headersConsumer.get();
		assertNotNull(consumer, "headers consumer should be captured");
		consumer.accept(headers);
		assertNull(headers.getFirst(HttpHeaders.AUTHORIZATION), "Authorization must not be set when credentials are not a String");
	}

	@Test
	void shouldThrowExceptionWhenResponseIsNull() {
		Long id = 1L;
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(UserExternalResponse.class)).thenReturn(null);
		
		assertThrows(InvalidUserException.class, () -> userServiceImpl.findById(id));
	}
	
	@Test
	void shouldThrowExceptionOn4xxError() throws Exception {
		final int code = 404;
		final String errorMessage = "User not found";
		String errorBodyJson = String.format("""
				{
					"code": %d,
					"message": "%s"
				}
			""", code, errorMessage);
		Long id = 1L;
		ClientHttpResponse clientHttpResponse = mock(ClientHttpResponse.class);
		ErrorExternalResponse errorResponse = new ErrorExternalResponse(code, errorMessage);
		
		when(clientHttpResponse.getBody()).thenReturn(new ByteArrayInputStream(errorBodyJson.getBytes()));
		when(objectMapper.readValue(any(InputStream.class), eq(ErrorExternalResponse.class))).thenReturn(errorResponse);
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		
		when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
			RestClient.ResponseSpec.ErrorHandler handler = invocation.getArgument(1);
			handler.handle(mock(ClientHttpRequest.class), clientHttpResponse);
			return responseSpec;
		});
		
		assertThrows(BusinessException.class, () -> userServiceImpl.findById(id));
	}
	
	@Test
	void shouldThrowExceptionOnMappingErrorResponse() throws Exception {
		String errorBodyJson = """
				{
				"prop": "someThing",
				"otherProp": "any"
				}
			""";
		Long id = 1L;
		ClientHttpResponse clientHttpResponse = mock(ClientHttpResponse.class);
		
		when(clientHttpResponse.getBody()).thenReturn(new ByteArrayInputStream(errorBodyJson.getBytes()));
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		
		when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
			RestClient.ResponseSpec.ErrorHandler handler = invocation.getArgument(1);
			handler.handle(mock(ClientHttpRequest.class), clientHttpResponse);
			return responseSpec;
		});
		
		when(objectMapper.readValue(any(InputStream.class), eq(ErrorExternalResponse.class)))
			.thenThrow(new RuntimeException("Mapping error"));
		
		assertThrows(TechnicalException.class, () -> userServiceImpl.findById(id));
	}
	
	@Test
	void shouldThrowExceptionOnMappingNotIncludedRole() {
		Long id = 1L;
		UserExternalResponse externalResponse = new UserExternalResponse(
			1L,
			"John",
			"not_included_role"
		);
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(UserExternalResponse.class)).thenReturn(externalResponse);
		
		assertThrows(InvalidRoleException.class, () -> userServiceImpl.findById(id));
	}
	
	@Test
	void shouldThrowTechnicalExceptionOn5xxError() {
		Long id = 1L;
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/user/{id}", id)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		
		when(responseSpec.onStatus(any(), any())).thenThrow(new TechnicalException("EXTERNAL_SERVICE_ERROR"));
		
		assertThrows(TechnicalException.class, () -> userServiceImpl.findById(id));
	}
	
	private UserExternalResponse validUserExternalResponse() {
		return new UserExternalResponse(
			1L,
			"John",
			"owner"
		);
	}
	
	private User validOwner() {
		return User.builder()
			.id(1L)
			.name("John")
			.role(UserRole.OWNER)
			.build();
	}
}
