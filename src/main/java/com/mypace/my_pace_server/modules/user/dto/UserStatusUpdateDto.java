package com.mypace.my_pace_server.modules.user.dto;

import com.mypace.my_pace_server.modules.user.enums.UserStatusEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatusUpdateDto {
  private UserStatusEnum status;
}
