package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.User;
import org.springframework.stereotype.Repository;

import org.springframework.beans.factory.annotation.Value;

@Repository
public class UserFileDAO {
  private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());

  private static final String DEFAULT_AUTHORITIES = "USER";

  private Integer nextId = 0;
  private ObjectMapper objectMapper;
  private String filename;
  private Map<Integer, User> users;

  public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
    LOG.info("UserFileDAO constructed with filename: " + filename);
    this.filename = filename;
    this.objectMapper = objectMapper;
    load();
  }

  public User getUser(String email) {
    synchronized (users) {
      return users.values().stream()
          .filter(user -> user.getUsername().equals(email))
          .findFirst()
          .orElseThrow();
    }
  }

  public User getUserById(int id) {
    synchronized (users) {
      return users.get(id);
    }
  }

  public User getUserByEmailPassword(String email, String password) throws Exception {
    synchronized (users) {
      return users.values().stream()
          .filter(user -> user.getUsername().equals(email) && user.getPassword().equals(password))
          .findFirst()
          .orElseThrow();
    }
  }

  /**
   * Generates an array of {@linkplain User users} from the tree map
   * 
   * @return The array of {@link User users}, may be empty
   */
  private User[] getUsersArray() {
    Collection<User> usersCollection = users.values();
    return usersCollection.toArray(new User[usersCollection.size()]);
  }

  private void load() throws IOException {
    LOG.info("Loading users");
    this.users = new TreeMap<>();
    User[] userArray;
    try {

      userArray = objectMapper.readValue(new File(filename), User[].class);
    } catch (Exception Exception) {
      LOG.info("No users found");
      return;
    }
    for (User user : userArray) {
      users.put(user.getId(), user);
      if (user.getId() > nextId)
        nextId = user.getId();
      LOG.info("Loaded user: " + user);
    }

    save();
  }

  private boolean save() throws IOException {
    LOG.info("Saving users");
    User[] userArray = getUsersArray();

    // Serializes the Java Objects to JSON objects into the file
    // writeValue will thrown an IOException if there is an issue
    // with the file or reading from the file
    objectMapper.writeValue(new File(filename), userArray);

    return true;
  }

  public User[] getUsers() {
    synchronized (users) {
      return getUsersArray();
    }
  }

  public User createUser(String username, String password) throws IOException {
    LOG.info("Creating user: " + username + " " + password);

    synchronized (users) {
      // checking to see if the user's email is taken
      User user;
      try {
        user = getUser(username);
      } catch (Exception err) {
        user = null;
      }
      if (user != null) {
        throw new IllegalArgumentException("Email is already taken");
      }

      User newUser = new User(getNextId(), username, password, DEFAULT_AUTHORITIES);
      users.put(newUser.getId(), newUser);
      save();
      return newUser;
    }
  }

  public User updateUser(User user) throws IOException {
    LOG.info("Updating user: " + user);

    synchronized (users) {
      users.put(user.getId(), user);
      save();
    }
    return user;
  }

  public boolean deleteUser(int id) throws IOException {
    LOG.info("Deleting user with id: " + id);

    synchronized (users) {
      User response = users.remove(id);
      save();
      return response != null;
    }
  }

  public Integer getNextId() {
    return ++nextId;
  }
}
