package com.foodcourt.foodcourt.infrastructure.adapters.jwt;

import com.foodcourt.foodcourt.domain.exception.user.InvalidTokenException;
import com.foodcourt.foodcourt.domain.gateways.TokenServiceGateway;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.auth.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

import static com.foodcourt.foodcourt.domain.constants.UserErrorMessage.INVALID_TOKEN;
import static com.foodcourt.foodcourt.domain.model.auth.AuthClaim.ROLE;
import static com.foodcourt.foodcourt.domain.model.auth.AuthClaim.USER_ID;

@Service
public class JwtAdapter implements TokenServiceGateway {
	
	private final Key key;
	
	public JwtAdapter(
		@Value("${jwt.secret}") String secret
	) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
	
	@Override
	public UserClaims parseToken(String token) {
		try {
			Claims claims = getClaimsFromToken(token);
			return new UserClaims(
				claims.get(USER_ID.value, Long.class),
				claims.getSubject(),
				UserRole.getRoleof(claims.get(ROLE.value, String.class))
			);
		} catch (Exception e) {
			throw new InvalidTokenException(INVALID_TOKEN);
		}
	}
	
	private Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
	
}
