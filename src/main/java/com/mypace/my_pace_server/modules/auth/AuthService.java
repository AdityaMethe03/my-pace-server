package com.mypace.my_pace_server.modules.auth;

import com.mypace.my_pace_server.modules.user.dto.UserDto;
import com.mypace.my_pace_server.modules.user.dto.UserResponseDto;

public interface AuthService {
  UserResponseDto registerUser(UserDto userDto);

  void revokeAllUserSessions(String userId);
}
