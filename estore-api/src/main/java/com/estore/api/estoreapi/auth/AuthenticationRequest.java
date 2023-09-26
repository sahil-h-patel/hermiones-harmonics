package com.estore.api.estoreapi.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
  @JsonProperty("email")
  private String email;
  @JsonProperty("password")
  private String password;
  @JsonProperty("ID")
  private int ID;

  public AuthenticationRequest(@JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("ID") int ID) {
    this.email = email;
    this.password = password;
    this.ID = ID;
  }
}
