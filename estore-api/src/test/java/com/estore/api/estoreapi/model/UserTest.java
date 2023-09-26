package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class UserTest {
    private int expectedID;
    private String expectedUserName;
    private String expectedPassword;
    private String expectedAuthorities;
    private User user;

    @BeforeEach
    public void setupUserTest() {
        expectedID = 1;
        expectedUserName = "john";
        expectedPassword = "pass";
        expectedAuthorities = "auth";
        user = new User(expectedID, expectedUserName, expectedPassword, expectedAuthorities);
    }

    @Test
    public void testID() {
        assertEquals(expectedID, user.getId());

        user.setId(2);
        assertEquals(2, user.getId());
    }

    @Test
    public void testUsername() {
        assertEquals(expectedUserName, user.getUsername());

        user.setUsername("joe");
        assertEquals("joe", user.getUsername());
    }

    @Test
    public void testPassword() {
        assertEquals(expectedPassword, user.getPassword());

        user.setPassword("betterpass");
        assertEquals("betterpass", user.getPassword());
    }

    @Test
    public void testAuthorities() {
        assertEquals(expectedAuthorities, user.getAuthorities());

        user.setAuthorities("test");
        assertEquals("test", user.getAuthorities());
    }

    @Test
    public void testCheckPassword() {
        user.setPassword("password");
        assertEquals(user.checkPassword("password"), true);
    }

    @Test
    public void testToString(){
        String expectedString =  "User [id=" + expectedID + ", username=" + expectedUserName + ", password=" + expectedPassword + ", authorities="
        + expectedAuthorities + "]";

        assertEquals(expectedString, user.toString());

    }
}
