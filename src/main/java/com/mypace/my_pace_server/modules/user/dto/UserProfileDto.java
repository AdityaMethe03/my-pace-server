package com.mypace.my_pace_server.modules.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto {
  private String name;
  private String image;
}
