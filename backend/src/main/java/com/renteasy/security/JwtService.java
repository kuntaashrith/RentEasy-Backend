package com.renteasy.security;

import com.renteasy.config.JwtProperties;
import com.renteasy.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
	private final JwtProperties props;
	private final SecretKey key;

	public JwtService(JwtProperties props) {
		this.props = props;
		this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
	}

	public long accessTokenExpiresInSeconds() {
		return props.accessTokenExpMinutes() * 60;
	}

	public String generateAccessToken(AuthUser user) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(accessTokenExpiresInSeconds());

		return Jwts.builder()
				.issuer(props.issuer())
				.subject(String.valueOf(user.getId()))
				.issuedAt(Date.from(now))
				.expiration(Date.from(exp))
				.claims(Map.of(
						"email", user.getEmail(),
						"role", user.getRole().name()
				))
				.signWith(key, Jwts.SIG.HS256)
				.compact();
	}

	public Claims parseAndValidate(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.requireIssuer(props.issuer())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public Long getUserId(Claims claims) {
		return Long.valueOf(claims.getSubject());
	}

	public Role getRole(Claims claims) {
		String role = claims.get("role", String.class);
		return Role.valueOf(role);
	}
}

