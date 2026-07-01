package com.mypace.my_pace_server.modules.user;

import com.mypace.my_pace_server.modules.user.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Tags(@Tag(name = "V1 User"))
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  /***  Register apis ***/
  @PostMapping(value = "/register")
  public ResponseEntity<UserResponseDto> createUser(@RequestBody UserDto user) {
    user.setId(UUID.randomUUID().toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
  }

  /***  Update apis ***/
  @PutMapping(value = "/update/{userId}")
  public ResponseEntity<UserResponseDto> updateUser(
      @RequestBody UserDto user, @PathVariable String userId) {
    return ResponseEntity.ok(userService.updateUser(user, userId));
  }

  @PutMapping(value = "/update/profile/{userId}")
  public ResponseEntity<UserResponseDto> updateUserProfile(
      @RequestBody UserProfileDto user, @PathVariable String userId) {
    return ResponseEntity.ok(userService.updateUserProfile(user, userId));
  }

  @PutMapping(value = "/update/password/{userId}")
  public ResponseEntity<UserResponseDto> updateUserPassword(
      @RequestBody UserPasswordDto user, @PathVariable String userId) {
    return ResponseEntity.ok(userService.updateUserPassword(user, userId));
  }

  @PutMapping(value = "/update/status/{userId}")
  public ResponseEntity<UserResponseDto> updateUserStatus(
      @RequestBody UserStatusUpdateDto userStatusUpdateDto, @PathVariable String userId) {
    verifyUserAccess(userId);
    return ResponseEntity.ok(userService.updateUserStatusById(userStatusUpdateDto, userId));
  }

  /***  Delete apis ***/
  @DeleteMapping(value = "/delete/{userId}")
  public void deleteUser(@PathVariable String userId) {
    verifyUserAccess(userId);
    userService.deleteUser(userId);
  }

  /***  Get apis  ***/
  @GetMapping(value = "/lookup/search/{userId}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @GetMapping(value = "/lookup/search/email/{email}")
  public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  @GetMapping(value = "/lookup/search/all")
  public ResponseEntity<Iterable<UserResponseDto>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  /***  helpers ***/
  private void verifyUserAccess(String targetUserId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String currentUserEmail = auth.getName();

    boolean isAdminOrSudo =
        auth.getAuthorities().stream()
            .anyMatch(
                a -> a.getAuthority().equals("ADMIN") || a.getAuthority().equals("SUDO_ADMIN"));

    if (!isAdminOrSudo) {
      UserResponseDto targetUser = userService.getUserById(targetUserId);
      if (!targetUser.getEmail().equals(currentUserEmail)) {
        throw new IllegalArgumentException("Access Denied: You can only modify your own account.");
      }
    }
  }
}
