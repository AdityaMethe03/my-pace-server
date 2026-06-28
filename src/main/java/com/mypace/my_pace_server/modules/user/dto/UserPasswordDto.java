package com.mypace.my_pace_server.modules.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswordDto {
  private String oldPassword;
  private String newPassword;
}
