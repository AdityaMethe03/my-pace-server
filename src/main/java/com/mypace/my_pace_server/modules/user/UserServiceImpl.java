package com.mypace.my_pace_server.modules.user;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.role.RoleRepository;
import com.mypace.my_pace_server.modules.role.enums.UserRole;
import com.mypace.my_pace_server.modules.user.dto.*;
import com.mypace.my_pace_server.modules.user.enums.Provider;
import java.util.Date;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UserResponseDto createUser(UserDto userDto) {
    if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
      throw new IllegalArgumentException("Email is required");
    }

    if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new IllegalArgumentException("User with given email already exists.");
    }

    User user = modelMapper.map(userDto, User.class);
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
    user.setEnable(true);
    user.setCreatedAt(new Date());
    user.setUpdatedAt(null);

    if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
      if (!userDto.getRoles().contains(UserRole.USER.name())) {
        user.getRoles().add(UserRole.USER.name());
      }

      userDto
          .getRoles()
          .forEach(
              role -> {
                roleRepository
                    .findByName(role)
                    .ifPresentOrElse(
                        roleDb -> {
                          user.getRoles().add(roleDb.getName());
                        },
                        () -> {
                          throw new IllegalArgumentException(
                              "Role with given name does not exist.");
                        });
              });
    } else {
      roleRepository
          .findByName(UserRole.USER.name())
          .ifPresentOrElse(
              roleDb -> {
                user.setRoles(Set.of(roleDb.getName()));
              },
              () -> {
                throw new IllegalArgumentException(UserRole.USER.name() + " role does not exist.");
              });
    }

    User savedUser = userRepository.save(user);

    return modelMapper.map(savedUser, UserResponseDto.class);
  }

  @Override
  public UserResponseDto getUserByEmail(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new ResourceNotFoundException("User with given email does not exist."));
    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  public UserResponseDto updateUser(UserDto userDto, String userId) {
    User existingUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with given id does not exist."));
    existingUser.setName(userDto.getName());
    existingUser.setProvider(userDto.getProvider());
    existingUser.setEnable(userDto.isEnable());
    existingUser.setStatus(userDto.getStatus());
    existingUser.setUpdatedAt(new Date());

    User user = userRepository.save(existingUser);

    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  public void deleteUser(String userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with given id does not exist."));
    userRepository.delete(user);
  }

  @Override
  public UserResponseDto getUserById(String userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with given id does not exist."));
    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public Iterable<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> modelMapper.map(user, UserResponseDto.class))
        .toList();
  }

  @Override
  public UserResponseDto updateUserProfile(UserProfileDto userDto, String userId) {
    User existingUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with given id does not exist."));
    existingUser.setName(userDto.getName());
    existingUser.setImage(userDto.getImage());
    existingUser.setUpdatedAt(new Date());

    User user = userRepository.save(existingUser);
    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  public UserResponseDto updateUserPassword(UserPasswordDto userDto, String userId) {
    User existingUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with given id does not exist."));
    if (userDto.getOldPassword().isBlank() || userDto.getOldPassword().isEmpty()) {
      throw new IllegalArgumentException("Password is required");
    }
    if (!passwordEncoder.matches(userDto.getOldPassword(), existingUser.getPassword())) {
      throw new IllegalArgumentException("Old password is incorrect");
    }
    if (userDto.getOldPassword().equals(userDto.getNewPassword())) {
      throw new IllegalArgumentException("New password cannot be same as old password");
    }

    existingUser.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
    existingUser.setUpdatedAt(new Date());

    User user = userRepository.save(existingUser);
    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  public UserResponseDto updateUserStatusById(
      UserStatusUpdateDto userStatusUpdateDto, String userId) {
    User existingUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with given id does not exist."));
    existingUser.setStatus(userStatusUpdateDto.getStatus());
    existingUser.setUpdatedAt(new Date());

    User user = userRepository.save(existingUser);
    return modelMapper.map(user, UserResponseDto.class);
  }
}
