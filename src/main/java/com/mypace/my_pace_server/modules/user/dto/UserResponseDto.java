package com.mypace.my_pace_server.modules.user.dto;

import com.mypace.my_pace_server.modules.user.enums.Provider;
import com.mypace.my_pace_server.modules.user.enums.UserStatusEnum;
import java.util.Date;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
  private String id;
  private String email;
  private String name;
  private String image;
  private boolean enable;
  private Date createdAt;
  private Date updatedAt;
  private Provider provider;
  private Set<String> roles;
  private UserStatusEnum status;
}
