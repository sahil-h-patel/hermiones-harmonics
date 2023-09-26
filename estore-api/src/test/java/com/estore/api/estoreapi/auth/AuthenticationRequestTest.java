package com.estore.api.estoreapi.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AuthenticationRequestTest {
  @Test
  public void testConstructor() {
    // Exercise
    AuthenticationRequest actualAuthenticationRequest = new AuthenticationRequest("email", "password", 1);

    // Verify
    assertEquals("email", actualAuthenticationRequest.getEmail());
    assertEquals("password", actualAuthenticationRequest.getPassword());
    assertEquals(1,actualAuthenticationRequest.getID());

    // Exercise
    actualAuthenticationRequest.setEmail("newEmail");
    actualAuthenticationRequest.setPassword("newPassword");
    actualAuthenticationRequest.setID(2);

    // Verify
    assertEquals("newEmail", actualAuthenticationRequest.getEmail());
    assertEquals("newPassword", actualAuthenticationRequest.getPassword());
    assertEquals(2, actualAuthenticationRequest.getID());
  }
}
