package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a credit card payment method
 * 
 * @author Team 2
 */
public class CreditCard {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    static final String STRING_FORMAT = "Credit Card [holdername=%s, cardNumber=%s]";

    @JsonProperty("holderName")
    private String holderName;
    @JsonProperty("cardNumber")
    private String cardNumber;

    /**
     * Create a credit card for buyer use
     * 
     * @param holderName name of the holder of the card
     * @param cardNumber credit card number
     */
    public CreditCard(@JsonProperty("holderName") String holderName, @JsonProperty("cardNumber") String cardNumber) {

        this.holderName = holderName;
        this.cardNumber = cardNumber;
        LOG.info("Creating " + this);
    }

    /**
     * Sets the holder name of the credit card
     * 
     * @param holderName new holder name
     */
    public void setHolderName(String holderName) {
        LOG.info("Setting credit card name to: " + holderName);
        this.holderName = holderName;
    }

    /**
     * Sets the credit card's number
     * 
     * @param cardNumber new credit card number
     */
    public void setCardNumber(String cardNumber) {
        LOG.info("Setting credit card number to: " + cardNumber);
        this.cardNumber = cardNumber;
    }

    /**
     * Retrieves the holder name of the credit card
     * 
     * @return holder name of the card
     */
    public String getHolderName() {
        LOG.info("Retrieving credit card name: " + holderName);
        return holderName;
    }

    /**
     * Retrieves the number of the credit card
     * 
     * @return card number
     */
    public String getCardNumber() {
        LOG.info("Retrieving credit card number: " + cardNumber);
        return cardNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, holderName, cardNumber);
    }

}
