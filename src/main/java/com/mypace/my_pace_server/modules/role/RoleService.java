package com.mypace.my_pace_server.modules.role;

import com.mypace.my_pace_server.modules.role.dto.RoleDto;
import com.mypace.my_pace_server.modules.role.enums.RoleStatusEnum;

public interface RoleService {

  // Create role
  RoleDto createRole(RoleDto role);

  // update role
  RoleDto updateRole(RoleDto role, String roleId);

  // delete role
  void deleteRole(String roleId);

  // get a role by id
  RoleDto getRoleById(String roleId);

  // get all roles
  Iterable<RoleDto> getAllRoles();

  RoleDto updateRoleStatusById(RoleStatusEnum status, String roleId);
}
