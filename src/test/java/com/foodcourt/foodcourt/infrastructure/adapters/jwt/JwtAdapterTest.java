package com.foodcourt.foodcourt.infrastructure.adapters.jwt;

import com.foodcourt.foodcourt.domain.exception.user.InvalidTokenException;
import com.foodcourt.foodcourt.domain.model.auth.UserClaims;
import com.foodcourt.foodcourt.domain.model.auth.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtAdapterTest {

    private static final String SECRET = Base64.getEncoder().encodeToString("01234567890123456789012345678901".getBytes());

    @Test
    void shouldThrowInvalidTokenExceptionForMalformedToken() {
        JwtAdapter jwtAdapter = new JwtAdapter(SECRET);

        String badToken = "this.is.not.a.valid.token";

        assertThrows(InvalidTokenException.class, () -> jwtAdapter.parseToken(badToken));
    }

    @Test
    void shouldParseValidTokenAndReturnUserClaims() {
        JwtAdapter jwtAdapter = new JwtAdapter(SECRET);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

        Long userId = 123L;
        String email = "user@example.com";
        String role = "owner";

        String token = Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .claim("role", role)
            .setIssuedAt(new Date())
            .signWith(key)
            .compact();

        UserClaims claims = jwtAdapter.parseToken(token);

        assertEquals(userId, claims.id());
        assertEquals(email, claims.email());
        assertEquals(UserRole.OWNER, claims.role());
    }

    @Test
    void shouldThrowInvalidTokenExceptionForWrongSignature() {
        JwtAdapter jwtAdapter = new JwtAdapter(SECRET);

        String otherSecret = Base64.getEncoder().encodeToString("another-secret-012345678901234567".getBytes());
        Key otherKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(otherSecret));

        String token = Jwts.builder()
            .setSubject("user@example.com")
            .claim("userId", 1L)
            .claim("role", "owner")
            .setIssuedAt(new Date())
            .signWith(otherKey)
            .compact();

        assertThrows(InvalidTokenException.class, () -> jwtAdapter.parseToken(token));
    }

}
