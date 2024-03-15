package com.tmploeg.hotelbooker;

import com.tmploeg.hotelbooker.controllers.BookingController;
import com.tmploeg.hotelbooker.controllers.ControllerBase;
import com.tmploeg.hotelbooker.controllers.UserController;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
public class ProjectConfiguration {
  private static final String ROUTE_SEPARATOR = "/";

  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

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
                    .requestMatchers(
                        HttpMethod.GET,
                        getBaseRouteForController(BookingController.class) + ROUTE_SEPARATOR + "**")
                    .permitAll()
                    .requestMatchers(
                        getBaseRouteForController(UserController.class) + ROUTE_SEPARATOR + "**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .build();
  }

  private static String getBaseRouteForController(Class<? extends ControllerBase> controllerClass) {
    Optional<RequestMapping> requestMapping =
        Optional.ofNullable(controllerClass.getAnnotation(RequestMapping.class));

    return requestMapping.map(mapping -> String.join(ROUTE_SEPARATOR, mapping.value())).orElse("");
  }
}
