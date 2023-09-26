package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("username")
  private String username;
  @JsonProperty("password")
  private String password;
  @JsonProperty("authorities")
  private String authorities;

  /**
   * To make authority to be serialized properly we convert it to list of strings
   * so the constructor uses Collection of Strings
   * 
   * @param id
   * @param username
   * @param password
   * @param authorities
   */
  @JsonCreator
  public User(
      @JsonProperty("id") int id,
      @JsonProperty("username") String username,
      @JsonProperty("password") String password,
      @JsonProperty("authorities") String authorities) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  // @Override
  // public boolean equals(Object o) {
  // if (o instanceof User) {
  // User other = (User) o;
  // return (this.id == other.id);
  // } else {
  // return false;
  // }
  // }

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + getUsername() + ", password=" + getPassword() + ", authorities="
        + getAuthorities() + "]";
  }
}
