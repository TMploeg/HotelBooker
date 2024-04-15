package com.tmploeg.hotelbooker.filters;

import com.tmploeg.hotelbooker.models.JwtToken;
import com.tmploeg.hotelbooker.models.entities.User;
import com.tmploeg.hotelbooker.services.JwtService;
import com.tmploeg.hotelbooker.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
  private final UserService userService;
  private final JwtService jwtService;

  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  private static final String AUTHORIZATION_HEADER_JWT_PREFIX = "Bearer ";

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      return;
    }

    Optional<String> authHeader = getAuthorizationHeader(request);

    if (authHeader.isEmpty()) {
      return;
    }

    String rawToken = authHeader.get().substring(AUTHORIZATION_HEADER_JWT_PREFIX.length());
    Optional<JwtToken> parsedToken = jwtService.readToken(rawToken);

    if (parsedToken.isEmpty()
        || parsedToken.get().isExpired()
        || !userService.userExists(parsedToken.get().username())) {
      return;
    }

    User user = userService.loadUserByUsername(parsedToken.get().username());

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }

  private static Optional<String> getAuthorizationHeader(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER_NAME))
        .filter(authHeader -> authHeader.startsWith(AUTHORIZATION_HEADER_JWT_PREFIX));
  }
}
