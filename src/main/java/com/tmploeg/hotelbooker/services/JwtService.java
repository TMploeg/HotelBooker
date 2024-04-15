package com.tmploeg.hotelbooker.services;

import com.tmploeg.hotelbooker.data.UserRepository;
import com.tmploeg.hotelbooker.models.JwtToken;
import com.tmploeg.hotelbooker.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
  @Value("${authentication.jwt-secret}")
  private String JWT_SECRET;

  @Value("${authentication.jwt.expiration-ms}")
  private int JWT_EXPIRATION_MS;

  private final UserRepository userRepository;
  private final Logger logger;

  private static String ROLES_CLAIMS_NAME = "roles";

  public String generateTokenForUser(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("user '" + username + "' not found"));

    return buildToken(user);
  }

  public Optional<JwtToken> readToken(String token) {
    try {
      Claims claims =
          Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();

      return Optional.of(
          new JwtToken(
              claims.getSubject(),
              getRolesFromClaims(claims),
              claims.getIssuedAt(),
              claims.getExpiration()));
    } catch (RuntimeException ex) {
      logger.log(
          Level.INFO,
          "Exception reading JWT-token: TYPE: '"
              + ex.getClass().getName()
              + "', MESSAGE: '"
              + ex.getMessage()
              + "'");
    }

    return Optional.empty();
  }

  private String buildToken(User user) {
    long currentTimeMillis = System.currentTimeMillis();

    return Jwts.builder()
        .claims(Map.of("roles", user.getAuthorities()))
        .subject(user.getUsername())
        .issuedAt(new Date(currentTimeMillis))
        .expiration(new Date(currentTimeMillis + JWT_EXPIRATION_MS))
        .signWith(getSignKey())
        .compact();
  }

  private SecretKey getSignKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
  }

  private String[] getRolesFromClaims(Claims claims) {
    Object rolesObject = claims.get(ROLES_CLAIMS_NAME);

    if (rolesObject == null) {
      throw new IllegalArgumentException("'" + ROLES_CLAIMS_NAME + "' claim not found");
    }

    if (!(rolesObject instanceof String[] roles)) {
      throw new IllegalArgumentException("claims '" + ROLES_CLAIMS_NAME + "' value is invalid");
    }

    return roles;
  }
}
