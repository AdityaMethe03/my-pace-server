package com.mypace.my_pace_server.config;

public class SecurityUrls {

  public static final String[] AUTH_PUBLIC_URLS = {
    // authentication paths endpoints
    "/api/v1/auth/register",
    "/api/v1/auth/login",
    "/api/v1/auth/refresh",

    // open api documentation paths endpoints
    "/v3/api-docs/**",
    "/api-docs/**",
    "/api-docs",
    // swagger ui endpoints
    "/swagger-ui.html",
    "/swagger-ui/**"
  };

  public static final String[] ALL_ROLES_URLS = {
    // authentication endpoints
    "/api/v1/auth/logout",
    "/api/v1/auth/logout/all",
    "/api/v1/users/delete/*",
    "/api/v1/users/update/status/*",

    // user endpoints
    "/api/v1/users/update/profile/*",
    "/api/v1/users/lookup/search/*",
    "/api/v1/users/lookup/search/email/*",
    "/api/v1/users/update/password/*",
  };

  public static final String[] SUDO_ADMIN_URLS = {
    // user endpoints
    "/api/v1/users/lookup/search/all",
    "/api/v1/users/register",
    "/api/v1/users/update/*",

    // role endpoints
    "/api/v1/role/register",
    "/api/v1/role/update/*",
    "/api/v1/role/lookup/**",
    "/api/v1/role/delete/*",
    "/api/v1/role/update/status/*",
  };
}
