package com.mypace.my_pace_server.modules.role.enums;

import java.util.Arrays;

public enum UserRole {
  SUDO_ADMIN,
  ADMIN,
  GUEST;

  public static String[] ALL() {
    return Arrays.stream(values()).map(Enum::name).toArray(String[]::new);
  }
}
