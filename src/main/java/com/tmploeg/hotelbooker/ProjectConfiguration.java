package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.controllers.ControllerRoutes;
import com.tmploeg.hotelbooker.enums.RoleName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProjectConfiguration {
  private static final String ROUTE_SEPARATOR = "/";

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .httpBasic(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(ControllerRoutes.USERS + ROUTE_SEPARATOR + "**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET, ControllerRoutes.HOTELS + ROUTE_SEPARATOR + "**")
                    .permitAll()
                    .requestMatchers(ControllerRoutes.HOTELS + ROUTE_SEPARATOR + "**")
                    .hasAuthority(RoleName.ADMIN.toString())
                    .anyRequest()
                    .authenticated())
        .build();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:4200");
      }
    };
  }
}
