package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.controllers.ControllerRoutes;
import com.tmploeg.hotelbooker.enums.RoleName;
import com.tmploeg.hotelbooker.filters.JwtAuthenticationFilter;
import com.tmploeg.hotelbooker.other.UnauthorizedEntryPoint;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProjectConfiguration {
  private static final String ROUTE_SEPARATOR = "/";

  @Value("${hotelbooker.cors}")
  private String corsAllowedOrigins;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(ControllerRoutes.AUTH + ROUTE_SEPARATOR + "**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET, ControllerRoutes.HOTELS + ROUTE_SEPARATOR + "**")
                    .permitAll()
                    .requestMatchers(ControllerRoutes.HOTELS + ROUTE_SEPARATOR + "**")
                    .hasAuthority(RoleName.ADMIN.toString())
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer.authenticationEntryPoint(new UnauthorizedEntryPoint()))
        .build();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(corsAllowedOrigins);
      }
    };
  }

  @Bean
  public SecretKey secretKey() {
    return Jwts.SIG.HS256.key().build();
  }
}
