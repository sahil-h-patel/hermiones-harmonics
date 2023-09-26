package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.BuyerInfo;
import com.estore.api.estoreapi.model.Order;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Order.OrderStatus;
import com.estore.api.estoreapi.persistence.BuyerInfoFileDAO;
import com.estore.api.estoreapi.persistence.OrderDAO;
import com.estore.api.estoreapi.persistence.ProductFileDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;

public class OrderControllerTest {
    @Spy
    OrderController orderController;

    OrderDAO mockOrderDAO;
    UserFileDAO mockUserDAO;
    BuyerInfoFileDAO mockBuyerInfoDAO;
    HttpServletRequest mockRequest;
    User mockUser;
    Order mockOrder;
    BuyerInfo mockBuyerInfo;
    ProductFileDAO mockProductFileDAO;


    @BeforeEach
    public void setUp() throws Exception {
        mockOrderDAO = mock(OrderDAO.class);
        mockUserDAO = mock(UserFileDAO.class);
        mockBuyerInfoDAO = mock(BuyerInfoFileDAO.class);
        mockProductFileDAO = mock(ProductFileDAO.class);

        orderController = new OrderController(mockOrderDAO, mockUserDAO, mockBuyerInfoDAO, mockProductFileDAO);
        mockRequest = mock(HttpServletRequest.class);
        mockUser = mock(User.class);
        mockOrder = mock(Order.class);
        mockBuyerInfo = mock(BuyerInfo.class);
    }

    @Test
    public void testCreateOrder() throws IOException {

        Order[] orderArray = new Order[0];

        when(mockOrderDAO.getAll()).thenReturn(orderArray);

        ResponseEntity<Order> res = orderController.createOrder(mockOrder);

        assertEquals(HttpStatus.CREATED, res.getStatusCode());

        int[] productIds = {1, 2, 3};

        Order testOrder = new Order(productIds, new Date(), OrderStatus.CANCELLED, 1324, "address", 1, 3);

        Order[] orderArray2 = { testOrder };

        when(mockOrder.getOrderID()).thenReturn(1);
        when(mockOrderDAO.getAll()).thenReturn(orderArray2);

        res = orderController.createOrder(testOrder);

        assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

        when(mockOrderDAO.getAll()).thenReturn(orderArray);
        when(mockOrderDAO.createOrder(mockOrder)).thenThrow(new IOException());

        res = orderController.createOrder(mockOrder);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
    }

    @Test
    public void testGetAllOrders() throws IOException {
        when(mockUser.getAuthorities()).thenReturn("USER");
        when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
        when(orderController.getUser(mockRequest)).thenReturn(mockUser);
        
        int[] productIds = {1, 2, 3};
        Order testOrder = new Order(productIds, new Date(), OrderStatus.CANCELLED, 1324, "address", 1, 3);

        Order[] orderArray = { testOrder };

        when(mockOrderDAO.getAll()).thenReturn(orderArray);
        when(mockUser.getId()).thenReturn(2);

        ResponseEntity<Order[]> res = orderController.getAllOrders(mockRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());

        when(mockUser.getAuthorities()).thenReturn("ADMIN");

        res = orderController.getAllOrders(mockRequest);

        assertEquals(HttpStatus.OK, res.getStatusCode());

        when(mockOrderDAO.getAll()).thenThrow(new IOException());

        res = orderController.getAllOrders(mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());

    }

    @Test
    public void testGetOrder_Null() throws IOException {
        // Setup
        when(mockUser.getAuthorities()).thenReturn("USER");
        when(mockOrderDAO.getOrder(1)).thenReturn(mockOrder);

        // Invoke
        ResponseEntity<Order> res1 = orderController.getOrder(mockRequest, 1);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, res1.getStatusCode());
    }
    
    @Test
    public void testGetOrder_ValidAdmin() throws IOException {
        // Setup
        when(mockRequest.getHeader("Authorization")).thenReturn("admin:admin");
        when(mockUser.getAuthorities()).thenReturn("ADMIN");
        when(mockOrderDAO.getOrder(1)).thenReturn(mockOrder);
        when(orderController.getUser(mockRequest)).thenReturn(mockUser);

        // Invoke
        ResponseEntity<Order> res1 = orderController.getOrder(mockRequest, 1);

        // Assert
        assertEquals(HttpStatus.OK, res1.getStatusCode());
    }

    @Test
    public void testGetOrder_ValidNotAdmin() throws Exception {
        // Setup
        when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
        when(mockUser.getAuthorities()).thenReturn("USER");
        when(mockUser.getId()).thenReturn(1);
        when(mockOrderDAO.getOrder(1)).thenReturn(mockOrder);
        when(orderController.getUser(mockRequest)).thenReturn(mockUser);
        when(mockOrder.getUserID()).thenReturn(1);

        // Invoke
        ResponseEntity<Order> result = orderController.getOrder(mockRequest, 1);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetOrder_Exception() throws Exception {
        // Setup
        when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
        when(mockUser.getAuthorities()).thenReturn("USER");
        when(mockUser.getId()).thenReturn(1);
        when(mockOrderDAO.getOrder(1)).thenThrow(new IOException());
        when(orderController.getUser(mockRequest)).thenReturn(mockUser);
        when(mockOrder.getUserID()).thenReturn(1);

        // Invoke
        ResponseEntity<Order> res1 = orderController.getOrder(mockRequest, 1);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res1.getStatusCode());
    }

    @Test
    public void testGetOrders_Null() throws IOException {
        // Setup
        when(mockUser.getAuthorities()).thenReturn("USER");
        when(mockOrderDAO.getOrder(1)).thenReturn(mockOrder);

        // Invoke
        ResponseEntity<ArrayList<Order>> res1 = orderController.getOrders(mockRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, res1.getStatusCode());
    }

    @Test
    public void testGetOrders_Ok() throws Exception {
        // Setup
        when(mockRequest.getHeader("Authorization")).thenReturn("user:password");
        when(mockUser.getAuthorities()).thenReturn("USER");
        when(mockUser.getId()).thenReturn(1);
        when(mockOrderDAO.getOrder(1)).thenThrow(new IOException());
        when(mockBuyerInfo.getPastOrderIds()).thenReturn(new ArrayList<>());
        when(orderController.getUser(mockRequest)).thenReturn(mockUser);
        when(mockBuyerInfoDAO.getBuyerInfo(1)).thenReturn(mockBuyerInfo);
        when(mockOrder.getUserID()).thenReturn(1);

        // Invoke
        ResponseEntity<ArrayList<Order>> res1 = orderController.getOrders(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, res1.getStatusCode());
    }

    @Test
    public void testDeleteOrder() throws IOException {
        when(mockOrderDAO.getOrder(1)).thenReturn(null);

        ResponseEntity<Order> res = orderController.deleteOrder(1);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

        when(mockOrderDAO.getOrder(1)).thenReturn(mockOrder);
        
        res = orderController.deleteOrder(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());

        when(mockOrderDAO.deleteOrder(1)).thenThrow(new IOException());

        res = orderController.deleteOrder(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
    }

}
