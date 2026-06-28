package com.mypace.my_pace_server.modules.auth;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.auth.refreshtoken.RefreshToken;
import com.mypace.my_pace_server.modules.auth.refreshtoken.RefreshTokenRepository;
import com.mypace.my_pace_server.modules.user.UserService;
import com.mypace.my_pace_server.modules.user.dto.UserDto;
import com.mypace.my_pace_server.modules.user.dto.UserResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public UserResponseDto registerUser(UserDto userDto) {
    return userService.createUser(userDto);
  }

  @Override
  @Transactional
  public void revokeAllUserSessions(String userId) {
    List<RefreshToken> activeTokens = refreshTokenRepository.findByUserIdAndRevokedFalse(userId);

    if (activeTokens.isEmpty()) {
      throw new ResourceNotFoundException("No active sessions found.");
    }

    activeTokens.forEach(token -> token.setRevoked(true));
    refreshTokenRepository.saveAll(activeTokens);
  }
}
