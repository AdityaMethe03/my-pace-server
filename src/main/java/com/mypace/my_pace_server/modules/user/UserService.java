package com.mypace.my_pace_server.modules.user;

import com.mypace.my_pace_server.modules.user.dto.*;

public interface UserService {

  // Create user
  UserResponseDto createUser(UserDto user);

  // get user by email
  UserResponseDto getUserByEmail(String email);

  // update user
  UserResponseDto updateUser(UserDto user, String userId);

  // delete user
  void deleteUser(String userId);

  // get user by id
  UserResponseDto getUserById(String userId);

  // get all users
  Iterable<UserResponseDto> getAllUsers();

  // update user
  UserResponseDto updateUserProfile(UserProfileDto user, String userId);

  UserResponseDto updateUserPassword(UserPasswordDto user, String userId);

  UserResponseDto updateUserStatusById(UserStatusUpdateDto userStatusUpdateDto, String userId);
}
