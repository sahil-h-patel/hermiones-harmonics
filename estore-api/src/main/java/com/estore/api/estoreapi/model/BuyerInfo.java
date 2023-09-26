package com.estore.api.estoreapi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the information for a buyer
 * 
 * @author Team 2
 */
public class BuyerInfo {
    private static final Logger LOG = Logger.getLogger(BuyerInfo.class.getName());

    static final String STRING_FORMAT = "Buyer [id=%d, user id=%d, name=%s, phone number=%s, past orders=%s, credit cards=%s, shipping addresses=%s, cart=%s, wishlist=%s]";

    @JsonProperty("id")
    private int id;
    @JsonProperty("userId")
    private int userid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("pastOrdersIds")
    private Collection<Integer> pastOrderIds;
    @JsonProperty("creditCards")
    private Collection<CreditCard> creditCards;
    @JsonProperty("shippingAddresses")
    private Collection<String> shippingAddresses;
    @JsonProperty("cart")
    private ArrayList<Integer> cart;
    @JsonProperty("wishlist")
    private ArrayList<Integer> wishlist;

    public BuyerInfo(
            @JsonProperty("id") int id,
            @JsonProperty("userId") int userid,
            @JsonProperty("name") String name,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("pastOrdersIds") Collection<Integer> pastOrderIds,
            @JsonProperty("creditCards") Collection<CreditCard> creditCards,
            @JsonProperty("shippingAddresses") Collection<String> shippingAddresses,
            @JsonProperty("cart") ArrayList<Integer> cart,
            @JsonProperty("wishlist") ArrayList<Integer> wishlist) {

        this.id = id;
        this.userid = userid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pastOrderIds = pastOrderIds;
        this.creditCards = creditCards;
        this.shippingAddresses = shippingAddresses;
        this.cart = cart;
        this.wishlist = wishlist;
        LOG.info("Creating buyer info: " + this);
    }

    /**
     * Retrieves this buyer's account id
     * 
     * @return buyer account id
     */
    public int getId() {
        LOG.info("Retrieving buyer id: " + id);
        return id;
    }

    /**
     * Retrieves the id of the associated user
     * 
     * @return associated user id
     */
    public int getUserId() {
        LOG.info("Retrieving buyer's associated user id: " + userid);
        return userid;
    }

    /**
     * Retrieves this buyer's name
     * 
     * @return buyer name
     */
    public String getName() {
        LOG.info("Retrieving buyer first name: " + name);
        return name;
    }

    /**
     * Retrieves this buyer's phone number
     * 
     * @return buyer phone number
     */
    public String getPhoneNumber() {
        LOG.info("Retrieving buyer phone number: " + phoneNumber);
        return phoneNumber;
    }

    /**
     * Retrieves this buyer's past order ids
     * 
     * @return list of buyer's past order ids
     */
    public Collection<Integer> getPastOrderIds() {
        LOG.info("Retrieving buyer's past order ids: " + pastOrderIds);
        return pastOrderIds;
    }

    /**
     * Retrieves this buyer's credit cards
     * 
     * @return list of buyer's credit cards
     */
    public Collection<CreditCard> getCreditCards() {
        LOG.info("Retrieving buyer's credit cards: " + creditCards);
        return creditCards;
    }

    /**
     * Retrieves this buyer's shipping addresses
     * 
     * @return list of buyer's shipping addresses
     */
    public Collection<String> getShippingAddresses() {
        LOG.info("Retrieving buyer's shippingAddress: " + shippingAddresses);
        return shippingAddresses;
    }

    /**
     * Sets this buyer's associated user id
     * 
     * @param userid new associated user id
     */
    public void setUserID(int userid) {
        LOG.info("Setting buyer's associated user id: " + userid);
        this.userid = userid;
    }

    /**
     * Sets the buyer's name
     * 
     * @param firstName new name
     */
    public void setName(String name) {
        LOG.info("Setting buyer name: " + name);
        this.name = name;
    }

    /**
     * Sets the buyer's phone number
     * 
     * @param phoneNumber new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        LOG.info("Setting buyer phone number: " + phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the buyer's payment method
     * 
     * @param creditCards new payment method
     */
    public void setCreditCards(Collection<CreditCard> creditCards) {
        LOG.info("Setting buyer's credit cards: " + creditCards);
        this.creditCards = creditCards;
    }

    /**
     * Sets the buyer's shipping addresses
     * 
     * @param shippingAddresses new shipping addresses
     */
    // @JsonSetter("shippingAddresses")
    public void setShippingAddresses(List<String> shippingAddresses) {
        LOG.info("Setting buyer's shipping address: " + shippingAddresses);
        this.shippingAddresses = shippingAddresses;
    }

    /**
     * Grabs the array of the ids in the cart
     * 
     * @return the ids of the items in the cart
     */

    // @JsonGetter("cart")
    public ArrayList<Integer> getCart() {
        LOG.info("Retrieving buyer's cart: " + cart);
        return cart;
    }

    /**
     * Add a product into the cart
     * 
     * @param product
     */

    public void addProductCart(Product product) {
        LOG.info("Adding product to buyer's cart: " + product.getId());
        this.cart.add(product.getId());
    }

    /**
     * Remove a product from the cart
     * 
     * @param product
     */

    public void removeProductCart(Product product) {
        LOG.info("Removing product to buyer's cart: " + product.getId());
        this.cart.remove(product.getId());
    }

    /**
     * Grabs the array of the ids in the wishlist
     * 
     * @return the ids of the items in the cart
     */

    // @JsonGetter("wishlist")
    public ArrayList<Integer> getWishlist() {
        LOG.info("Getting buyer's wishlist: " + wishlist);
        return wishlist;
    }

    /**
     * Add a product into the wishlist
     * 
     * @param product
     */

    public void addProductWishlist(Product product) {
        LOG.info("Adding a product to buyer's wishlist: " + product.getId());
        this.wishlist.add(product.getId());
    }

    /**
     * Remove a product from the wishlist
     * 
     * @param product
     */

    public void removeProductWishlist(Product product) {
        LOG.info("Removing a product to buyer's wishlist: " + product.getId());
        this.wishlist.remove(product.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, userid, name,
                phoneNumber, pastOrderIds, creditCards, shippingAddresses, cart, wishlist);
    }

}
