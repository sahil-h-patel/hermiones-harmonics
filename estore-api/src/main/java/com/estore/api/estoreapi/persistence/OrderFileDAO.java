package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

import com.estore.api.estoreapi.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Orders
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class OrderFileDAO implements OrderDAO {
    Map<Integer, Order> orders = new HashMap<Integer, Order>(); // Provides a local cache of the order objects
    // so that we don't need to read from the file
    // each time
    private ObjectMapper objectMapper; // Provides conversion between Order
                                       // objects and JSON text format written
                                       // to the file
    private static int nextId; // The next Id to assign to a new order
    private String filename; // Filename to read from and write to

    /**
     * Creates a Order File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public OrderFileDAO(@Value("${orders.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load the products from the file
    }

    /**
     * Generates the next id for a new {@linkplain Order order}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of all of the {@linkplain Order orders} from the tree map
     * <br>
     * 
     * @return The array of {@link Order orders}, may be empty
     */
    private Order[] getAllOrders() {

        Collection<Order> values = orders.values();
        Order[] array = values.toArray(new Order[0]);
        return array;

    }

    /**
     * Loads {@linkplain Order orders} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        orders = new HashMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of products
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Order[] orderArray = objectMapper.readValue(new File(filename), Order[].class);

        // Add each product to the Hash map and keep track of the greatest id
        for (Order order : orderArray) {
            orders.put(order.getOrderID(), order);
            if (order.getOrderID() > nextId)
                nextId = order.getOrderID();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * Saves the {@linkplain Order order} from the map into the file as an
     * array of JSON objects
     * 
     * @return true if the {@link Order orders} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Order[] orderArray = getAllOrders();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), orderArray);
        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Order[] getAll() {
        synchronized (orders) {
            return getAllOrders();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Order getOrder(int id) {
        synchronized (orders) {
            for (Order order : orders.values()) {
                if (id == order.getOrderID()) {
                    return order;
                }
            }
            return null;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Order createOrder(Order order) throws IOException {
        synchronized (orders) {
            // We create a new product object because the id field is immutable
            // and we need to assign the next unique id
            Order newOrder = new Order(order.getProductIds(), order.getDate(), order.getOrderStatus(),
                    order.getCCDigits(), order.getAddress(), nextId(), order.getUserID());
            orders.put(newOrder.getOrderID(), newOrder);
            save(); // may throw an IOException
            return newOrder;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean deleteOrder(int id) throws IOException {
        synchronized (orders) {
            // If product does exist
            if (orders.containsKey(id) == false)
                return false;
            else {
                orders.remove(id);
                save(); // may throw an IOException
                return true;
            }

        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Order updateOrder(Order order) throws IOException {
        synchronized (orders) {
            // If product does exist
            if (orders.containsKey(order.getOrderID()) == false)
                return null;

            orders.put(order.getOrderID(), order);
            save(); // may throw an IOException
            return order;
        }
    }

}