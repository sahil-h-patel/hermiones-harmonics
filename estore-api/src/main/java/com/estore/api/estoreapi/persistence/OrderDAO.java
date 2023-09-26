package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Order;

/**
 * Defines the interface for Product object persistence
 * 
 * @author SWEN Faculty
 */
public interface OrderDAO {

    /**
     * Retrieves all {@linkplain Order orders}
     * 
     * @return An array of {@link Order orders} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Order[] getAll() throws IOException;

    /**
     * Creates and saves a {@linkplain Order order}
     * 
     * @param product {@linkplain Order order} object to be created and saved
     *                <br>
     *                The id of the order object is ignored and a new unique id is
     *                assigned
     *
     * @return new {@link Order order} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    Order createOrder(Order order) throws IOException;

    /**
     * Retrieves a {@linkplain Order order} with the given id
     * 
     * @param id The id of the {@link Order order} to get
     * 
     * @return a {@link Order order} object with the matching id
     *         <br>
     *         null if no {@link Order order} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Order getOrder(int id) throws IOException;

    /**
     * Deletes a {@linkplain Order order} with the given id
     * 
     * @param id The id of the {@link Order order}
     * 
     * @return true if the {@link Order order} was deleted
     *         <br>
     *         false if product with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteOrder(int id) throws IOException;

    /**
     * Updates the {@linkplain Order order}
     * 
     * @param {@link Order order} object to be updated
     * 
     * @return updated {@link Order order} if successful, null if
     *         {@link Order order} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Order updateOrder(Order order) throws IOException;

}
