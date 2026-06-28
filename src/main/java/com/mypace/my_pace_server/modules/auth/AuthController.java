package com.mypace.my_pace_server.modules.auth;

import com.mypace.my_pace_server.modules.auth.dto.LoginRequest;
import com.mypace.my_pace_server.modules.auth.dto.RefreshTokenRequest;
import com.mypace.my_pace_server.modules.auth.dto.TokenResponse;
import com.mypace.my_pace_server.modules.auth.refreshtoken.RefreshToken;
import com.mypace.my_pace_server.modules.auth.refreshtoken.RefreshTokenRepository;
import com.mypace.my_pace_server.modules.user.User;
import com.mypace.my_pace_server.modules.user.UserRepository;
import com.mypace.my_pace_server.modules.user.dto.UserDto;
import com.mypace.my_pace_server.modules.user.dto.UserResponseDto;
import com.mypace.my_pace_server.security.CookieService;
import com.mypace.my_pace_server.security.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;
  private final RefreshTokenRepository refreshTokenRepository;

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final ModelMapper modelMapper;
  private final CookieService cookieService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(
      @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    // authenticate
    Authentication authenticate = authenticate(loginRequest);
    User user =
        userRepository
            .findByEmail(loginRequest.email())
            .orElseThrow(() -> new BadCredentialsException("Invalid Username or Password."));
    if (!user.isEnable()) {
      throw new DisabledException("User is disabled");
    }

    String jti = UUID.randomUUID().toString();

    var refreshTokenOb =
        RefreshToken.builder() // refresh token object
            .id(UUID.randomUUID().toString())
            .jti(jti)
            .userId(user.getId())
            .createdAt(new Date())
            .expiresAt(Date.from(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds())))
            .revoked(false)
            .build();

    // save refresh token INFORMATION
    refreshTokenRepository.save(refreshTokenOb);

    // generate token
    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

    // use cookie service to attach a refresh token in a cookie
    cookieService.attachRefreshCookie(
        response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
    cookieService.addNoStoreHeaders(response);

    TokenResponse tokenResponse =
        TokenResponse.of(
            accessToken,
            refreshToken,
            jwtService.getAccessTtlSeconds(),
            modelMapper.map(user, UserResponseDto.class));
    return ResponseEntity.ok(tokenResponse);
  }

  private Authentication authenticate(LoginRequest loginRequest) {
    try {
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    } catch (Exception e) {
      e.printStackTrace();
      throw new BadCredentialsException("Invalid Username or Password.");
    }
  }

  // Api to renew access and refresh token
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refreshToken(
      @RequestBody(required = false) RefreshTokenRequest body,
      HttpServletRequest request,
      HttpServletResponse response) {

    // This method will read the refresh token from the request header or body
    String refreshToken =
        readRefreshTokenFromRequest(body, request)
            .orElseThrow(() -> new BadCredentialsException("Refresh token is missing."));

    if (!jwtService.isRefreshToken(refreshToken)) {
      throw new BadCredentialsException("Invalid Refresh Token Type.");
    }

    String jti = jwtService.getJti(refreshToken);
    String userId = jwtService.getUserId(refreshToken);
    RefreshToken storedRefreshToken =
        refreshTokenRepository
            .findByJti(jti)
            .orElseThrow(() -> new BadCredentialsException("Refresh Token not recognized."));

    User storedUser =
        userRepository
            .findById(storedRefreshToken.getUserId())
            .orElseThrow(() -> new BadCredentialsException("User not found."));

    if (storedRefreshToken.isRevoked()) {
      throw new BadCredentialsException("Refresh Token Expired or Revoked");
    }

    if (storedRefreshToken.getExpiresAt().before(new Date())) {
      throw new BadCredentialsException("Refresh Token Expired.");
    }

    // Rotate refresh token
    storedRefreshToken.setRevoked(true);
    String newJti = UUID.randomUUID().toString();
    storedRefreshToken.setReplacedByToken(newJti);
    refreshTokenRepository.save(storedRefreshToken);

    var newRefreshTokenOb =
        RefreshToken.builder()
            .id(UUID.randomUUID().toString())
            .jti(newJti)
            .userId(storedUser.getId())
            .createdAt(new Date())
            .expiresAt(Date.from(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds())))
            .revoked(false)
            .build();

    refreshTokenRepository.save(newRefreshTokenOb);
    String newAccessToken = jwtService.generateAccessToken(storedUser);
    String newRefreshToken =
        jwtService.generateRefreshToken(storedUser, newRefreshTokenOb.getJti());

    cookieService.attachRefreshCookie(
        response, newRefreshToken, (int) jwtService.getRefreshTtlSeconds());
    cookieService.addNoStoreHeaders(response);
    return ResponseEntity.ok(
        TokenResponse.of(
            newAccessToken,
            newRefreshToken,
            jwtService.getAccessTtlSeconds(),
            modelMapper.map(storedUser, UserResponseDto.class)));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    readRefreshTokenFromRequest(null, request)
        .ifPresent(
            token -> {
              try {
                if (jwtService.isRefreshToken(token)) {
                  String jti = jwtService.getJti(token);
                  refreshTokenRepository
                      .findByJti(jti)
                      .ifPresent(
                          rt -> {
                            rt.setRevoked(true);
                            refreshTokenRepository.save(rt);
                          });
                }
              } catch (JwtException ignored) {
              }
            });

    // Use CookieUtil (same behavior)
    cookieService.clearRefreshCookie(response);
    cookieService.addNoStoreHeaders(response);
    SecurityContextHolder.clearContext();
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private Optional<String> readRefreshTokenFromRequest(
      RefreshTokenRequest body, HttpServletRequest request) {
    // 1. prefer reading the refresh token from a cookie
    if (request.getCookies() != null) {
      Optional<String> fromCookie =
          Arrays.stream(request.getCookies())
              .filter((Cookie c) -> cookieService.getRefreshTokenCookieName().equals(c.getName()))
              .map(Cookie::getValue)
              .filter((String v) -> !v.isBlank())
              .findFirst();

      if (fromCookie.isPresent()) {
        return fromCookie;
      }
    }
    // 2. body
    if (body != null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
      return Optional.of(body.refreshToken());
    }

    // 3. custom header
    String refreshHeader = request.getHeader("X-Refresh-Token");
    if (refreshHeader != null && !refreshHeader.isBlank()) {
      return Optional.of(refreshHeader.trim());
    }

    // 4. Authorization = Bearer <token> (token is Refresh Token)
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.regionMatches(true, 0, "Bearer", 0, 7)) {
      String candidate = authHeader.substring(7).trim();
      if (!candidate.isEmpty()) {
        try {
          if (jwtService.isRefreshToken(candidate)) {
            return Optional.of(candidate);
          }
        } catch (Exception ignored) {

        }
      }
    }

    return Optional.empty();
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserDto userDto) {
    userDto.setId(UUID.randomUUID().toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto));
  }

  @PostMapping("/logout/all")
  public ResponseEntity<Void> logoutAllSessions(
      HttpServletRequest request, HttpServletResponse response) {
    String userId =
        readRefreshTokenFromRequest(null, request)
            .map(
                token -> {
                  try {
                    if (jwtService.isRefreshToken(token)) {
                      String jti = jwtService.getJti(token);
                      refreshTokenRepository
                          .findByJti(jti)
                          .ifPresent(
                              rt -> {
                                rt.setRevoked(true);
                                refreshTokenRepository.save(rt);
                              });
                      return jwtService.getUserId(token); // Extract userId
                    }
                  } catch (JwtException ignored) {
                  }
                  return "";
                })
            .orElse("");

    log.info("userId: " + userId);

    if (userId.isBlank()) {
      throw new IllegalArgumentException("User not identified.");
    }

    authService.revokeAllUserSessions(userId);

    // Use CookieUtil (same behavior)
    cookieService.clearRefreshCookie(response);
    cookieService.addNoStoreHeaders(response);
    SecurityContextHolder.clearContext();

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
