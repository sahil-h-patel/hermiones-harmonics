package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.auth.AuthenticationRequest;
import com.estore.api.estoreapi.auth.TokenResponse;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Controller-tier")
public class AuthenticationControllerTest {
  ObjectMapper mockObjectMapper;
  UserFileDAO mockUserFileDAO;

  AuthenticationController authenticationController;

  @BeforeEach
  public void setUp() {
    mockObjectMapper = mock(ObjectMapper.class);
    mockUserFileDAO = mock(UserFileDAO.class);

    authenticationController = new AuthenticationController(mockUserFileDAO);
  }

  @Test
  public void authenticateTest() throws Exception {
    // Setup

    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn("user");
    when(mockUser.getPassword()).thenReturn("password");
    when(mockUserFileDAO.createUser("user", "password")).thenReturn(mockUser);

    AuthenticationRequest request = mock(AuthenticationRequest.class);
    when(request.getEmail()).thenReturn("user");
    when(request.getPassword()).thenReturn("password");

    // Exercise
    ResponseEntity<TokenResponse> response = authenticationController.register(request);
    TokenResponse actualToken = response.getBody();

    // Assert
    assertNotNull(actualToken);
    assertNotNull(actualToken.getToken());
    assertEquals("user:password", actualToken.getToken());

    // Exercise
    when(mockUserFileDAO.getUserByEmailPassword("user", "password")).thenReturn(mockUser);
    response = authenticationController.authenticate(request);
    actualToken = response.getBody();

    // Assert
    assertNotNull(actualToken);
    assertNotNull(actualToken.getToken());
    assertEquals("user:password", actualToken.getToken());

    // Exercise
    when(mockUserFileDAO.getUserByEmailPassword("user", "password")).thenThrow(new Exception());
    response = authenticationController.authenticate(request);

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    assertNull(response.getBody());

    // Exercise
    when(mockUserFileDAO.createUser("user", "password")).thenThrow(new IOException());
    response = authenticationController.register(request);

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    assertNull(response.getBody());

  }

}
