package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.estore.api.estoreapi.persistence.BuyerInfoFileDAO;
import com.estore.api.estoreapi.persistence.OrderDAO;
import com.estore.api.estoreapi.persistence.ProductFileDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;
import com.estore.api.estoreapi.model.BuyerInfo;
import com.estore.api.estoreapi.model.Order;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Order.OrderStatus;

/**
 * Handles the REST API requests for the Product resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty
 */

@RestController
@RequestMapping("orders")
public class OrderController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private OrderDAO orderDao;
    private UserFileDAO userFileDao;
    private BuyerInfoFileDAO buyerInfoFileDao;
    private ProductFileDAO productFileDao;
    
    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param orderDAO The {@link OrderDAO Order Data Access Object} to perform CRUD
     *                 operations
     *                 <br>
     *                 This dependency is injected by the Spring Framework
     */
    public OrderController(OrderDAO orderDao, UserFileDAO userFileDao, BuyerInfoFileDAO buyerInfoFileDao, ProductFileDAO productFileDao) {
        this.orderDao = orderDao;
        this.userFileDao = userFileDao;
        this.buyerInfoFileDao = buyerInfoFileDao;
        this.productFileDao = productFileDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Order order} for the given id
     * 
     * @param id The id used to locate the {@link Order order}
     * 
     * @return ResponseEntity with {@link Order order} object and HTTP status of OK
     *         if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(HttpServletRequest request, @PathVariable int id) {
        LOG.info("GET /orders/" + id);

        User user = getUser(request);

        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            Order order = orderDao.getOrder(id);
            if (order != null) {
                if (!user.getAuthorities().equals("ADMIN") && order.getUserID() != user.getId())
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                return new ResponseEntity<Order>(order, HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Order order}
     * 
     * @return ResponseEntity with array of {@link Order order} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Order[]> getAllOrders(HttpServletRequest request) {
        LOG.info("GET /orders");
        User user = getUser(request);

        try {
            Order[] allOrders = orderDao.getAll();
            if (!user.getAuthorities().equals("ADMIN"))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<Order[]>(allOrders, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Order order} by {@link uid}
     * 
     * @param uid optinal query paramter that can be provided within the request. If
     *            present then it will search for orders with the specific user id.
     * 
     * @return ResponseEntity with array of {@link Order order} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/user")
    public ResponseEntity<ArrayList<Order>> getOrders(HttpServletRequest request) {
        User user = getUser(request);
        if (user == null || user.getAuthorities().equals("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int id = user.getId();
        BuyerInfo buyer = buyerInfoFileDao.getBuyerInfo(id);
        LOG.info("GET /orders/user");
        try {
            ArrayList<Integer> orderIDs = new ArrayList<>(buyer.getPastOrderIds());
            ArrayList<Order> pastOrders = new ArrayList<Order>();
            for (int num : orderIDs) {
                pastOrders.add(orderDao.getOrder(num));
            }
            return new ResponseEntity<ArrayList<Order>>(pastOrders, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Order order} with the provided product object
     * 
     * @param product - The {@link Order order} to create
     * 
     * @return ResponseEntity with created {@link Order order} object and HTTP
     *         status of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Order order}
     *         object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        LOG.info("POST /orders " + order);

        try {
            for (Order exisitingOrder : orderDao.getAll()) {
                if (exisitingOrder.getOrderID() == order.getOrderID()) {
                    return new ResponseEntity<Order>(HttpStatus.CONFLICT);
                }
            }

            order = orderDao.createOrder(order);
            int[] productIds = order.getProductIds();
            for (int i = 0; i < productIds.length; i++) {
                boolean result = productFileDao.decrementQuantity(productIds[i]);
                if (!result){
                    LOG.info("Could not find product with id: " + productIds[i]);
                }
            }

            return new ResponseEntity<Order>(order, HttpStatus.CREATED);

        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Order order} with the provided {@linkplain Order
     * order} object, if it exists
     * 
     * @param product     The {@link Order order} to update
     * @param orderStatus the {@link OrderStatus status} to change
     * 
     * @return ResponseEntity with updated {@link Order order} object and HTTP
     *         status of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        LOG.info("PUT /orders " + order);

        try {
            if (orderDao.getOrder(order.getOrderID()) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            order = orderDao.updateOrder(order);
            return new ResponseEntity<Order>(order, HttpStatus.OK);

        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Order order} with the given id
     * 
     * @param id The id of the {@link Order order} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable int id) {
        LOG.info("DELETE /orders/" + id);

        try {
            if (orderDao.getOrder(id) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            orderDao.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    User getUser(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null)
            return null;
        String[] authParts = auth.split(":");
        if (authParts.length != 2)
            return null;
        String username = authParts[0], password = authParts[1];
        try {
            return userFileDao.getUserByEmailPassword(username, password);
        } catch (Exception e) {
            return null;
        }

    }

}