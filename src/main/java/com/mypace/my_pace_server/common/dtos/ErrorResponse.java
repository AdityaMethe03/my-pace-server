package com.mypace.my_pace_server.common.dtos;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus status, int statusCode) {}
