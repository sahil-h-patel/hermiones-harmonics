package com.estore.api.estoreapi.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.auth.AuthenticationRequest;
import com.estore.api.estoreapi.auth.TokenResponse;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserFileDAO;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
  private static final Logger LOG = Logger.getLogger(AuthenticationController.class.getName());

  private final UserFileDAO userDao;

  public AuthenticationController(UserFileDAO userDao) {
    this.userDao = userDao;
  }

  @PostMapping("")
  public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthenticationRequest request) {
    LOG.info("POST /auth");
    try {
      userDao.getUserByEmailPassword(request.getEmail(), request.getPassword());
    } catch (Exception err) {
      return ResponseEntity.status(400).body(null);
    }
    final TokenResponse token = new TokenResponse(
        request.getEmail() + ":" + request.getPassword());
    return ResponseEntity.ok(token);
  }

  @PutMapping("/register")
  public ResponseEntity<TokenResponse> register(@RequestBody AuthenticationRequest request) {
    LOG.info("PUT /auth/register");
    try {
      User user = userDao.createUser(request.getEmail(), request.getPassword());
      final TokenResponse token = new TokenResponse(user.getUsername() + ":" + user.getPassword());
      return ResponseEntity.ok(token);
    } catch (Exception err) {
      return ResponseEntity.status(400).body(null);
    }
  }
}
