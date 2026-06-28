package com.mypace.my_pace_server.security.config;

import com.mypace.my_pace_server.common.dtos.ApiError;
import com.mypace.my_pace_server.config.SecurityUrls;
import com.mypace.my_pace_server.modules.role.enums.UserRole;
import com.mypace.my_pace_server.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final ObjectMapper objectMapper;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(SecurityUrls.AUTH_PUBLIC_URLS)
                    .permitAll()
                    .requestMatchers(SecurityUrls.SUDO_ADMIN_URLS)
                    .hasAuthority(UserRole.SUDO_ADMIN.name())
                    .requestMatchers(SecurityUrls.ADMIN_URLS)
                    .hasAnyAuthority(UserRole.SUDO_ADMIN.name(), UserRole.ADMIN.name())
                    .requestMatchers(SecurityUrls.ALL_ROLES_URLS)
                    .hasAnyAuthority(UserRole.ALL())
                    .anyRequest()
                    .authenticated())
        .logout(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                        (request, response, e) -> {
                          response.setStatus(401);
                          response.setContentType("application/json");
                          String message =
                              Optional.ofNullable((String) request.getAttribute("error"))
                                  .orElse("Unauthorized Access! " + e.getMessage());
                          var apiError =
                              ApiError.of(
                                  HttpStatus.UNAUTHORIZED.value(),
                                  "Unauthorized Access!",
                                  message,
                                  request.getRequestURI());
                          response.getWriter().write(objectMapper.writeValueAsString(apiError));
                        })
                    .accessDeniedHandler(
                        ((request, response, e) -> {
                          response.setStatus(403);
                          response.setContentType("application/json");
                          String message = e.getMessage();
                          String error = (String) request.getAttribute("error");
                          if (error != null) {
                            message = error;
                          }
                          var apiError =
                              ApiError.of(
                                  HttpStatus.FORBIDDEN.value(),
                                  "Forbidden Access!",
                                  message,
                                  request.getRequestURI(),
                                  true);
                          response.getWriter().write(objectMapper.writeValueAsString(apiError));
                        })))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(
      @Value("${app.cors.front-end-url}") String corsUrls) {
    String[] urls = corsUrls.trim().split(",");
    var config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(urls));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
