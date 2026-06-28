package com.mypace.my_pace_server.security;

import com.mypace.my_pace_server.modules.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class JwtService {

  private final SecretKey key;
  private final long accessTtlSeconds;
  private final long refreshTtlSeconds;
  private final String issuer;

  public JwtService(
      @Value("${security.jwt.secret-key}") String secret,
      @Value("${security.jwt.access-ttl-seconds}") long accessTtlSeconds,
      @Value("${security.jwt.refresh-ttl-seconds}") long refreshTtlSeconds,
      @Value("${security.jwt.issuer}") String issuer) {

    if (secret == null || secret.length() < 64)
      throw new IllegalArgumentException("Invalid secret key");

    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTtlSeconds = accessTtlSeconds;
    this.refreshTtlSeconds = refreshTtlSeconds;
    this.issuer = issuer;
  }

  public String generateAccessToken(User user) {
    List<String> roles = user.getRoles() == null ? List.of() : user.getRoles().stream().toList();

    return Jwts.builder()
        .id(UUID.randomUUID().toString())
        .subject(user.getId())
        .issuer(issuer)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plusSeconds(accessTtlSeconds)))
        .claims(Map.of("email", user.getEmail(), "roles", roles, "typ", "access"))
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  public String generateRefreshToken(User user, String jti) {
    Date now = new Date();
    return Jwts.builder()
        .id(jti)
        .subject(user.getId())
        .issuer(issuer)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plusSeconds(refreshTtlSeconds)))
        .claim("typ", "refresh")
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  public Jws<Claims> parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
  }

  public boolean isAccessToken(String token) {
    return "access".equals(parse(token).getPayload().get("typ"));
  }

  public boolean isRefreshToken(String token) {
    return "refresh".equals(parse(token).getPayload().get("typ"));
  }

  public String getUserId(String token) {
    return parse(token).getPayload().getSubject();
  }

  public String getJti(String token) {
    return parse(token).getPayload().getId();
  }
}
