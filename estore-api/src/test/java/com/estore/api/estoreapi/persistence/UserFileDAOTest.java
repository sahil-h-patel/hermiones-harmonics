package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class UserFileDAOTest {
  ObjectMapper mockObjectMapper;
  UserFileDAO userFileDAO;

  @BeforeEach
  public void setUp() throws IOException {
    mockObjectMapper = mock(ObjectMapper.class);

    User[] testUsers = new User[3];
    testUsers[0] = mock(User.class);
    testUsers[1] = mock(User.class);
    testUsers[2] = mock(User.class);

    when(testUsers[0].getUsername()).thenReturn("user1");
    when(testUsers[1].getUsername()).thenReturn("user2");
    when(testUsers[2].getUsername()).thenReturn("user3");
    when(testUsers[0].getPassword()).thenReturn("password1");
    when(testUsers[1].getPassword()).thenReturn("password2");
    when(testUsers[2].getPassword()).thenReturn("password3");
    when(testUsers[0].getId()).thenReturn(1);
    when(testUsers[1].getId()).thenReturn(2);
    when(testUsers[2].getId()).thenReturn(3);

    when(mockObjectMapper
        .readValue(new File("doesnt_matter.txt"), User[].class))
        .thenReturn(testUsers);
    userFileDAO = new UserFileDAO("doesnt_matter.txt", mockObjectMapper);
  }

  @Test
  public void testConstructor() throws IOException {
    // Exercise
    when(mockObjectMapper
        .readValue(new File("doesnt_matter.txt"), User[].class))
        .thenThrow(new IOException());
    assertDoesNotThrow(() -> new UserFileDAO("doesnt_matter.txt", mockObjectMapper));
  }

  @Test
  public void testGetters() throws Exception {
    // Exercise
    User[] actualUsers = userFileDAO.getUsers();
    assertEquals(3, actualUsers.length);

    User actualUser = userFileDAO.getUser("user1");
    assertEquals("user1", actualUser.getUsername());

    User actualUser2 = userFileDAO.getUserById(1);
    assertEquals("user1", actualUser2.getUsername());

    User actualUser3 = userFileDAO.getUserByEmailPassword("user1", "password1");
    assertEquals("user1", actualUser3.getUsername());
    assertThrows(Exception.class, () -> userFileDAO.getUserByEmailPassword("user1", "password2"));
  }

  @Test
  public void testUserManagement() throws IOException {
    assertThrows(IllegalArgumentException.class, () -> userFileDAO.createUser("user1", "password1"));
    userFileDAO.createUser("user4", "password4");
    assertEquals(4, userFileDAO.getUsers().length);

    User newMockedUser = mock(User.class);
    when(newMockedUser.getUsername()).thenReturn("user5");
    when(newMockedUser.getPassword()).thenReturn("password5");
    when(newMockedUser.getId()).thenReturn(2);
    userFileDAO.updateUser(newMockedUser);
    User actualUser = userFileDAO.getUser("user5");
    assertEquals("user5", actualUser.getUsername());

    assertTrue(userFileDAO.deleteUser(3));
    assertEquals(3, userFileDAO.getUsers().length);
    assertFalse(userFileDAO.deleteUser(5));
  }

}
