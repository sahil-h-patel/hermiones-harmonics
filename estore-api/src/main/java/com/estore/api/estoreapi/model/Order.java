package com.estore.api.estoreapi.model;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an order entity
 * 
 * @author Team 2
 */
public class Order {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    static final String STRING_FORMAT = "Order [productIds=%s, date=%s, order=%s]";

    public enum OrderStatus {
        UNPROCESSED,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }

    @JsonProperty("orderID")
    private int id;

    @JsonProperty("userID")
    private int uid;

    @JsonProperty("productId")
    private int[] productIds;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("orderStatus")
    private OrderStatus orderStatus;

    @JsonProperty("ccDigits")
    private int ccDigits;

    @JsonProperty("address")
    private String address;

    /**
     * Create an order with the product sold and the date it was placed
     * 
     * @param productIds    ids of products that were sold in this order
     * @param date          date that this order was placed
     * @param orderStatus   status of the order
     */

    public Order(@JsonProperty("productId") int[] productIds, @JsonProperty("date") Date date, @JsonProperty("orderStatus") OrderStatus orderStatus, 
                @JsonProperty("ccDigits") int ccDigits, @JsonProperty("address") String address, @JsonProperty("orderID") int id, @JsonProperty("userID") int uid) {
        this.productIds = productIds;
        this.date = date;
        this.orderStatus = orderStatus;
        this.ccDigits = ccDigits;
        this.address = address;
        this.id = id;
        this.uid = uid;
        LOG.info("Creating " + this);
    }

    /**
     * Retrieve the product ids that were sold in this order
     * 
     * @return sold product ids
     */
    public int[] getProductIds() {
        LOG.info("Retrieving order product by id: " + productIds);
        return productIds;
    }

    /**
     * Retrieve the date this order was placed
     * 
     * @return date of order
     */
    public Date getDate() {
        LOG.info("Retrieving order date: " + date);
        return date;
    }

    /**
     * Retrieve the order status
     * 
     * @return order status
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public String getAddress() {
        return address;
    }

    public int getCCDigits(){
        return ccDigits;
    }

    public int getUserID(){
        return uid;
    }

    public int getOrderID(){
        return id;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, Arrays.toString(productIds), date, orderStatus);
    }

}
