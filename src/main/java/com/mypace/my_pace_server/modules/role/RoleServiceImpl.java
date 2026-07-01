package com.mypace.my_pace_server.modules.role;

import com.mypace.my_pace_server.common.exceptions.ResourceNotFoundException;
import com.mypace.my_pace_server.modules.role.dto.RoleDto;
import com.mypace.my_pace_server.modules.role.enums.RoleStatusEnum;
import com.mypace.my_pace_server.modules.role.enums.UserRole;
import com.mypace.my_pace_server.modules.user.User;
import com.mypace.my_pace_server.modules.user.UserRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
  private final ModelMapper modelMapper;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public RoleDto createRole(RoleDto roleDto) {

    roleRepository
        .findByName(roleDto.getName())
        .ifPresent(
            role -> {
              throw new IllegalArgumentException("Role with given name already exists.");
            });

    Role role = modelMapper.map(roleDto, Role.class);
    role.setCreatedAt(new Date());
    role.setUpdatedAt(null);

    Role savedRole = roleRepository.save(role);

    return modelMapper.map(savedRole, RoleDto.class);
  }

  @Override
  public RoleDto updateRole(RoleDto roleDto, String roleId) {

    Role existingRole =
        roleRepository
            .findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found with given id"));
    existingRole.setName(roleDto.getName());
    existingRole.setStatus(roleDto.getStatus());
    existingRole.setUpdatedAt(new Date());

    Role role = roleRepository.save(existingRole);

    return modelMapper.map(role, RoleDto.class);
  }

  @Override
  @Transactional
  public void deleteRole(String roleId) {
    Role role =
        roleRepository
            .findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found with given id"));

    if (UserRole.SUDO_ADMIN.name().equals(role.getName())
        || UserRole.USER.name().equals(role.getName())) {
      throw new IllegalArgumentException("Role with given name cannot be deleted.");
    }

    List<User> users = userRepository.findByRolesContaining(role.getName());
    users.forEach(user -> user.getRoles().remove(role.getName()));
    userRepository.saveAll(users);

    roleRepository.delete(role);
  }

  @Override
  public RoleDto getRoleById(String roleId) {
    Role role =
        roleRepository
            .findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found with given id"));
    return modelMapper.map(role, RoleDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public Iterable<RoleDto> getAllRoles() {
    return roleRepository.findAll().stream()
        .map(role -> modelMapper.map(role, RoleDto.class))
        .toList();
  }

  @Override
  public RoleDto updateRoleStatusById(RoleStatusEnum status, String roleId) {
    Role existingRole =
        roleRepository
            .findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found with given id"));

    if (UserRole.SUDO_ADMIN.name().equals(existingRole.getName())
        || UserRole.USER.name().equals(existingRole.getName())) {
      throw new IllegalArgumentException("Default role with given name cannot be updated.");
    }

    existingRole.setStatus(status);
    existingRole.setUpdatedAt(new Date());
    Role role = roleRepository.save(existingRole);
    return modelMapper.map(role, RoleDto.class);
  }
}
