package com.mypace.my_pace_server.core;

import com.mypace.my_pace_server.modules.role.Role;
import com.mypace.my_pace_server.modules.role.RoleRepository;
import com.mypace.my_pace_server.modules.role.enums.RoleStatusEnum;
import com.mypace.my_pace_server.modules.role.enums.UserRole;
import com.mypace.my_pace_server.modules.user.User;
import com.mypace.my_pace_server.modules.user.UserRepository;
import com.mypace.my_pace_server.modules.user.enums.Provider;
import com.mypace.my_pace_server.modules.user.enums.UserStatusEnum;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Value("${app.sudo-admin.email}")
  private String sudoAdminEmail;

  @Value("${app.sudo-admin.password}")
  private String sudoAdminPassword;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    seedDefaultRoles();
    seedSudoAdmin();
  }

  private void seedDefaultRoles() {
    Stream.of(UserRole.SUDO_ADMIN.name(), UserRole.ADMIN.name(), UserRole.GUEST.name())
        .forEach(this::seedRole);
  }

  private void seedRole(String roleName) {
    roleRepository
        .findByName(roleName)
        .ifPresentOrElse(
            role -> log.info("{} role already exists.", role.getName()),
            () -> {
              Role role = new Role();
              role.setId(UUID.randomUUID().toString());
              role.setName(roleName);
              role.setStatus(RoleStatusEnum.ACTIVE);
              role.setCreatedAt(new Date());
              roleRepository.save(role);
              log.info("{} role created.", roleName);
            });
  }

  private void seedSudoAdmin() {
    userRepository
        .findByEmail(sudoAdminEmail)
        .ifPresentOrElse(
            user -> log.info("Sudo admin already exists: {}", user.getEmail()),
            () -> {
              User user =
                  User.builder()
                      .id(sudoAdminEmail)
                      .email(sudoAdminEmail)
                      .name("Sudo Admin")
                      .password(passwordEncoder.encode(sudoAdminPassword))
                      .enable(true)
                      .provider(Provider.LOCAL)
                      .roles(Set.of(UserRole.SUDO_ADMIN.name()))
                      .status(UserStatusEnum.ACTIVE)
                      .createdAt(new Date())
                      .build();
              userRepository.save(user);
              log.info("Sudo admin created: {}", sudoAdminEmail);
            });
  }
}
