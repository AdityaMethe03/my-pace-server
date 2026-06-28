package com.mypace.my_pace_server.security;

import com.mypace.my_pace_server.modules.user.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        if (!jwtService.isAccessToken(token)) {
          filterChain.doFilter(request, response);
          return;
        }

        Claims payload = jwtService.parse(token).getPayload();
        String userId = payload.getSubject();

        userRepository
            .findById(userId)
            .ifPresent(
                user -> {
                  if (user.isEnable()) {
                    List<GrantedAuthority> authorities =
                        user.getRoles() == null
                            ? List.of()
                            : user.getRoles().stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                    var auth =
                        new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    if (SecurityContextHolder.getContext().getAuthentication() == null)
                      SecurityContextHolder.getContext().setAuthentication(auth);
                  }
                });
      } catch (ExpiredJwtException e) {
        request.setAttribute("error", "Token expired");
      } catch (Exception e) {
        request.setAttribute("error", "Invalid Token");
      }
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getRequestURI().startsWith("/api/v1/auth/");
  }
}
