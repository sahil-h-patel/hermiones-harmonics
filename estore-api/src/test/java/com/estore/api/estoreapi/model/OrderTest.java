package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Order.OrderStatus;

@Tag("Model-tier")
public class OrderTest {
    private int expectedId;
    private int expectedUserId;
    private int[] expectedProductIds;
    private Date expectedDate;
    private OrderStatus expectedOrderStatus;
    private int expectedCCDigits;
    private String expectedAddress;
    private Order order;

    @BeforeEach
    public void setUpOrderTest() {
        expectedId = 1;
        expectedUserId = 2;
        expectedProductIds = new int[3];
        expectedProductIds[0] = 1;
        expectedProductIds[1] = 2;
        expectedProductIds[2] = 3;
        expectedDate = new Date();
        expectedOrderStatus = OrderStatus.DELIVERED;
        expectedCCDigits = 1234;
        expectedAddress = "stupid town";
        order = new Order(expectedProductIds, expectedDate, expectedOrderStatus,
                expectedCCDigits, expectedAddress, expectedId, expectedUserId);
    }

    @Test
    public void testID() {
        assertEquals(expectedId, order.getOrderID());
    }

    @Test
    public void testUserID() {
        assertEquals(expectedUserId, order.getUserID());
    }

    @Test
    public void testProductIDS() {
        assertEquals(expectedProductIds, order.getProductIds());
    }

    @Test
    public void testDate() {
        assertEquals(expectedDate, order.getDate());
    }

    @Test
    public void testOrderStatus() {
        assertEquals(expectedOrderStatus, order.getOrderStatus());
    }

    @Test
    public void testSetOrderStatus() {
        order.setOrderStatus(OrderStatus.SHIPPED);
        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
    }

    @Test
    public void testCCDigits() {
        assertEquals(expectedCCDigits, order.getCCDigits());
    }

    @Test
    public void testAddress() {
        assertEquals(expectedAddress, order.getAddress());
    }

    // @Test
    // public void testToString(){
    // //Setup
    // Date date = new Date(1688607420);

    // int[] ids = new int[3];
    // ids[0] = 1;
    // ids[1] = 2;
    // ids[2] = 3;

    // String expectedString = String.format(Order.STRING_FORMAT,
    // Arrays.toString(ids), date, OrderStatus.SHIPPED);

    // //Invoke
    // Order order = new Order(ids, date, OrderStatus.SHIPPED);

    // String actualString = order.toString();

    // assertEquals(expectedString, actualString);

    // }
}
