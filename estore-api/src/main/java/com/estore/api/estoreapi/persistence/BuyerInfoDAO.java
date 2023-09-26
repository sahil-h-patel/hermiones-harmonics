package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.estore.api.estoreapi.model.BuyerInfo;
import com.estore.api.estoreapi.model.Product;

/**
 * Defines the interface for BuyerInfo object persistence
 * 
 * @author Team 2
 */
public interface BuyerInfoDAO {
    /**
     * Retrieves all {@linkplain BuyerInfo buyerInfos}
     * 
     * @return An array of {@link BuyerInfo buyerInfos} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    BuyerInfo[] getBuyerInfos() throws IOException;

    /**
     * Retrieves a {@linkplain BuyerInfo buyerInfo} with the given id
     * 
     * @param id The id of the {@link BuyerInfo buyerInfo} to get
     * 
     * @return a {@link BuyerInfo buyerInfo} object with the matching id
     *         <br>
     *         null if no {@link BuyerInfo buyerInfo} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    BuyerInfo getBuyerInfo(int id) throws IOException;

    /**
     * Retrieves the {@linkplain BuyerInfo buyerInfo} belonging to
     * the user with the given id
     * 
     * @param userid The user id of the {@link User user} whose buyers
     *               will be fetched
     * 
     * @return {@link BuyerInfo buyerInfo} object associated with the user id
     * 
     * @throws IOException if an issue with underlying storage
     */
    BuyerInfo getBuyerInfoByUserId(int userid) throws IOException;

    /**
     * Creates and saves a {@linkplain BuyerInfo buyerInfo}
     * 
     * @param buyerInfo {@linkplain BuyerInfo buyerInfo} object to be created and
     *                  saved
     *                  <br>
     *                  The id of the buyerInfo object is ignored and a new uniqe id
     *                  is assigned
     *
     * @return new {@link BuyerInfo buyerInfo} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    BuyerInfo createBuyerInfo(BuyerInfo buyerInfo) throws IOException;

    /**
     * Updates and saves a {@linkplain BuyerInfo buyerInfo}
     * 
     * @param {@link BuyerInfo buyerInfo} object to be updated and saved
     * 
     * @return updated {@link BuyerInfo buyerInfo} if successful, null if
     *         {@link BuyerInfo buyerInfo} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    BuyerInfo updateBuyerInfo(BuyerInfo buyerInfo) throws IOException;

    /**
     * Deletes a {@linkplain BuyerInfo buyerInfo} with the given id
     * 
     * @param id The id of the {@link BuyerInfo buyerInfo}
     * 
     * @return the {@link BuyerInfo buyerInfo} that was deleted
     *         <br>
     *         null if buyerInfo with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    BuyerInfo deleteBuyerInfo(int id) throws IOException;

        /**
     * Calculates the total cost of the Buyer's cart based off of the items
     * 
     * @param items
     * @param buyerInfo
     * @throws IOException
     */

    int calcTotalCost(Collection<Integer> items, BuyerInfo buyerInfo) throws IOException;

    /**
     * Fetches all of the product objects from the list of IDs in the cart
     * @param buyerInfo
     * @return the product objects in an arraylist
     * @throws IOException
     */

    public ArrayList<Product> fetchProducts(BuyerInfo buyerInfo) throws IOException;
    
    /**
     * Fetches all of the product objects from the list of IDs in the WL
     * @param buyerInfo
     * @return the product objects in an arraylist
     * @throws IOException
     */

    public ArrayList<Product> fetchWL(BuyerInfo buyerInfo) throws IOException;
}
