package com.mypace.my_pace_server.common.exceptions;

import com.mypace.my_pace_server.common.dtos.ApiError;
import com.mypace.my_pace_server.common.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    UsernameNotFoundException.class,
    BadCredentialsException.class,
    CredentialsExpiredException.class,
    DisabledException.class,
  })
  public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request) {
    ApiError apiError =
        ApiError.of(
            HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), request.getRequestURI());
    return ResponseEntity.badRequest().body(apiError);
  }

  // resource not found exceptions handler :: method
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException exception) {
    ErrorResponse internalServerError =
        new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, 404);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException exception) {
    ErrorResponse internalServerError =
        new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, 400);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internalServerError);
  }
}
