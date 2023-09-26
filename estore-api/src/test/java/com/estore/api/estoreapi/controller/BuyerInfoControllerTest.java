package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.BuyerInfo;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.BuyerInfoFileDAO;
import com.estore.api.estoreapi.persistence.OrderDAO;
import com.estore.api.estoreapi.persistence.ProductFileDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;

public class BuyerInfoControllerTest {
  BuyerInfoController buyerInfoController;
  BuyerInfoFileDAO mockBuyerInfoDAO;
  UserFileDAO mockUserDAO;
  ProductFileDAO mockProductDAO;
  HttpServletRequest mockRequest;
  User mockUser;
  BuyerInfo mockBuyerInfo;
  OrderDAO mockOrderDAO;

  @BeforeEach
  public void setUp() throws Exception {
    mockOrderDAO = mock(OrderDAO.class);
    mockProductDAO = mock(ProductFileDAO.class);
    mockBuyerInfoDAO = mock(BuyerInfoFileDAO.class);
    mockUserDAO = mock(UserFileDAO.class);
    buyerInfoController = new BuyerInfoController(mockBuyerInfoDAO, mockUserDAO);
    mockRequest = mock(HttpServletRequest.class);
    mockUser = mock(User.class);
    mockBuyerInfo = mock(BuyerInfo.class);

    when(mockUserDAO.getUserByEmailPassword("admin", "admin")).thenReturn(mockUser);
    when(mockRequest.getHeader("Authorization")).thenReturn("admin:admin");
  }

  @Test
  public void testGetBuyer() throws IOException {
    // Setup
    when(mockBuyerInfoDAO.getBuyerInfo(1)).thenReturn(mockBuyerInfo);
    when(mockBuyerInfoDAO.getBuyerInfo(2)).thenReturn(null);
    when(mockBuyerInfoDAO.getBuyerInfo(3)).thenThrow(new IOException());

    // Exercise
    ResponseEntity<BuyerInfo> res1 = buyerInfoController.getBuyerInfo(1);
    ResponseEntity<BuyerInfo> res2 = buyerInfoController.getBuyerInfo(2);
    ResponseEntity<BuyerInfo> res3 = buyerInfoController.getBuyerInfo(3);

    // Assert
    assertEquals(HttpStatus.OK.value(), res1.getStatusCodeValue());
    assertEquals(HttpStatus.NOT_FOUND.value(), res2.getStatusCodeValue());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), res3.getStatusCodeValue());
  }

  @Test
  public void testGetBuyerInfos() throws Exception {
    // Setup
    BuyerInfo[] BuyerInfos = { mockBuyerInfo };
    when(mockBuyerInfoDAO.getBuyerInfos()).thenReturn(BuyerInfos);
    when(mockUser.getAuthorities()).thenReturn("ADMIN");
    // Exercise
    ResponseEntity<BuyerInfo[]> res1 = buyerInfoController.getBuyerInfos(mockRequest);
    when(mockUser.getAuthorities()).thenReturn("USER");
    ResponseEntity<BuyerInfo[]> res2 = buyerInfoController.getBuyerInfos(mockRequest);
    when(mockUser.getAuthorities()).thenReturn("ADMIN");
    when(mockBuyerInfoDAO.getBuyerInfos()).thenThrow(new IOException());
    ResponseEntity<BuyerInfo[]> res3 = buyerInfoController.getBuyerInfos(mockRequest);
    when(mockUserDAO.getUserByEmailPassword("admin", "admin")).thenThrow(new Exception());
    ResponseEntity<BuyerInfo[]> res4 = buyerInfoController.getBuyerInfos(mockRequest);

    // Assert
    assertEquals(HttpStatus.OK.value(), res1.getStatusCodeValue());
    assertEquals(HttpStatus.OK.value(), res2.getStatusCodeValue());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), res3.getStatusCodeValue());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), res4.getStatusCodeValue());
  }

  @Test
  public void testCreateBuyerInfo() throws IOException {
    BuyerInfo[] BuyerInfos = { mockBuyerInfo };
    when(mockBuyerInfoDAO.getBuyerInfos()).thenReturn(BuyerInfos);

    BuyerInfo newMockBuyerInfo = mock(BuyerInfo.class);
    BuyerInfo newMockBuyerInfo2 = mock(BuyerInfo.class);

    // Setup
    when(mockBuyerInfoDAO.createBuyerInfo(newMockBuyerInfo)).thenReturn(newMockBuyerInfo);
    when(mockBuyerInfoDAO.createBuyerInfo(mockBuyerInfo)).thenReturn(mockBuyerInfo);
    when(mockBuyerInfoDAO.createBuyerInfo(newMockBuyerInfo2)).thenThrow(new IOException());

    // Exercise
    ResponseEntity<BuyerInfo> res1 = buyerInfoController.createBuyerInfo(mockRequest, newMockBuyerInfo);
    ResponseEntity<BuyerInfo> res2 = buyerInfoController.createBuyerInfo(mockRequest, mockBuyerInfo);
    ResponseEntity<BuyerInfo> res3 = buyerInfoController.createBuyerInfo(mockRequest, newMockBuyerInfo2);

    // Assert
    assertEquals(HttpStatus.CREATED, res1.getStatusCode());
    assertEquals(HttpStatus.CREATED, res2.getStatusCode());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res3.getStatusCode());
  }

  @Test
  public void testUpdateBuyerInfo() throws IOException{
    // Setup
    when(mockUser.getAuthorities()).thenReturn("USER");
    when(mockBuyerInfoDAO.updateBuyerInfo(null)).thenReturn(null);

    // Exercise
    ResponseEntity<BuyerInfo> res1 = buyerInfoController.updateBuyerInfo(mockBuyerInfo);

    // Assert
    when(mockRequest.getHeader("Authorization")).thenReturn("admin:admin");
    when(mockUser.getAuthorities()).thenReturn("ADMIN");
    res1 = buyerInfoController.updateBuyerInfo(mockBuyerInfo);
    assertEquals(HttpStatus.NOT_FOUND, res1.getStatusCode());

    when(mockBuyerInfoDAO.updateBuyerInfo(mockBuyerInfo)).thenReturn(mockBuyerInfo);
    res1 = buyerInfoController.updateBuyerInfo(mockBuyerInfo);
    assertEquals(HttpStatus.OK, res1.getStatusCode());
    
    when(mockBuyerInfoDAO.updateBuyerInfo(mockBuyerInfo)).thenThrow(new IOException());
    res1 = buyerInfoController.updateBuyerInfo(mockBuyerInfo);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res1.getStatusCode());
  }

  @Test
  public void testDeleteBuyerInfo() throws IOException{
    when(mockRequest.getHeader("Authorization")).thenReturn("adminadmin");
    ResponseEntity<BuyerInfo> res1 = buyerInfoController.deleteBuyerInfo(mockRequest,1);
    assertEquals(HttpStatus.UNAUTHORIZED, res1.getStatusCode());

    when(mockRequest.getHeader("Authorization")).thenReturn("admin:admin");
    when(mockUser.getAuthorities()).thenReturn("USER");
    when(mockUser.getUsername()).thenReturn("notadmin");
    res1 = buyerInfoController.deleteBuyerInfo(mockRequest,1);
    assertEquals(HttpStatus.UNAUTHORIZED, res1.getStatusCode());
    
    when(mockUser.getAuthorities()).thenReturn("ADMIN");
    when(mockUser.getUsername()).thenReturn("admin");
    when(mockBuyerInfoDAO.deleteBuyerInfo(1)).thenReturn(null);
    ResponseEntity<BuyerInfo> res2 = buyerInfoController.deleteBuyerInfo(mockRequest,1);
    assertEquals(HttpStatus.NOT_FOUND, res2.getStatusCode());
    
    when(mockBuyerInfoDAO.deleteBuyerInfo(1)).thenReturn(mockBuyerInfo);
    res2 = buyerInfoController.deleteBuyerInfo(mockRequest,1);
    assertEquals(HttpStatus.OK, res2.getStatusCode());

    when(mockBuyerInfoDAO.deleteBuyerInfo(1)).thenThrow(new IOException());
    res1 = buyerInfoController.deleteBuyerInfo(mockRequest,1);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res1.getStatusCode());
  }

  @Test
  public void testGetCart_Unauthorized() {
    // Setup
    when(mockRequest.getHeader("Authorization")).thenReturn(null);

    // Invoke
    ResponseEntity<ArrayList<Integer>> res1 = buyerInfoController.getCart(mockRequest);

    // Analyze
    assertEquals(HttpStatus.UNAUTHORIZED, res1.getStatusCode());
  }

  @Test
  public void testGetCart_EmptyCart() throws Exception {
    // Setup
    when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
    when(mockUser.getId()).thenReturn(1);
    when(mockUser.getAuthorities()).thenReturn("USER");
    when(mockUserDAO.getUserByEmailPassword("user", "password")).thenReturn(mockUser);
    when(mockBuyerInfoDAO.getBuyerInfoByUserId(1)).thenReturn(new BuyerInfo(1, 1, "John", "333-333-3333", 
      new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

    // Invoke
    ResponseEntity<ArrayList<Integer>> res1 = buyerInfoController.getCart(mockRequest);

    // Analyze
    assertEquals(HttpStatus.OK, res1.getStatusCode());
  }

  @Test
  public void testGetCart_NoUser() throws Exception {
    // Setup
    when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
    when(mockUser.getId()).thenReturn(1);
    when(mockUser.getAuthorities()).thenReturn("USER");
    when(mockUserDAO.getUserByEmailPassword("user", "password")).thenReturn(mockUser);
    when(mockBuyerInfoDAO.getBuyerInfoByUserId(1)).thenReturn(null);
      
    // Invoke
    ResponseEntity<ArrayList<Integer>> res1 = buyerInfoController.getCart(mockRequest);

    // Analyze
    assertEquals(HttpStatus.NOT_FOUND, res1.getStatusCode());
  }

  @Test
  public void testGetCart_Exception() throws Exception {
    // Setup
    when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
    when(mockUser.getId()).thenReturn(1);
    when(mockUser.getAuthorities()).thenReturn("USER");
    when(mockUserDAO.getUserByEmailPassword("user", "password")).thenReturn(mockUser);
    when(mockBuyerInfoDAO.getBuyerInfoByUserId(1)).thenThrow(new IOException());
      
    // Invoke
    ResponseEntity<ArrayList<Integer>> res1 = buyerInfoController.getCart(mockRequest);

    // Analyze
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res1.getStatusCode());
  }

  @Test
  public void testGetBuyerInfoByUser_Null() throws Exception {
    // Setup
    when(mockRequest.getHeader("Authorization")).thenReturn(null);
    when(mockUser.getId()).thenReturn(null);
    when(mockUser.getAuthorities()).thenReturn(null);
    when(buyerInfoController.getBuyerInfoByUser(mockRequest)).thenReturn(null);
      
    // Invoke
    ResponseEntity<BuyerInfo> res1 = buyerInfoController.getBuyerInfoByUser(mockRequest);

    // Analyze
    assertEquals(HttpStatus.UNAUTHORIZED, res1.getStatusCode());
  }
  @Test
  public void testGetBuyerInfoByUser_NotNull() throws Exception {
    // Setup
    when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
    when(mockUser.getId()).thenReturn(1);
    when(mockUser.getAuthorities()).thenReturn("USER");
    when(mockUserDAO.getUserByEmailPassword("user", "password")).thenReturn(mockUser);
    when(buyerInfoController.getBuyerInfoByUser(mockRequest)).thenReturn(null);
      
    // Invoke
    ResponseEntity<BuyerInfo> res1 = buyerInfoController.getBuyerInfoByUser(mockRequest);

    // Analyze
    assertEquals(HttpStatus.OK, res1.getStatusCode());
  }

  @Test
  public void testGetBuyerInfoByUser_Exception() throws Exception {
    // Setup
    when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
    when(mockUser.getId()).thenReturn(1);
    when(mockUser.getAuthorities()).thenReturn("USER");
    when(mockUserDAO.getUserByEmailPassword("user", "password")).thenReturn(mockUser);
    when(buyerInfoController.getBuyerInfoByUser(mockRequest)).thenThrow(new IOException());
      
    // Invoke
    ResponseEntity<BuyerInfo> res1 = buyerInfoController.getBuyerInfoByUser(mockRequest);

    // Analyze
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res1.getStatusCode());
  }
}
