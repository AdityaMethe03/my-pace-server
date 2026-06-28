package com.mypace.my_pace_server.modules.role.dto;

import com.mypace.my_pace_server.modules.role.enums.RoleStatusEnum;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
  private String id;
  private String name;
  private RoleStatusEnum status;
  private Date createdAt;
  private Date updatedAt;
}
