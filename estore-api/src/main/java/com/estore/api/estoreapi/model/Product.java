package com.estore.api.estoreapi.model;

import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Product entity
 * 
 * @author Team 2
 */
@Setter
@Getter
public class Product implements Comparable<Product> {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Product [id=%d, name=%s]";

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("tags")
    private String[] tags;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private double price;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("image")
    private String image;

    /**
     * Create a product with the given id and name
     * 
     * @param id       The id of the product
     * @param name     The name of the product
     * @param price    The price of the product
     * @param quantity How many products are in stock
     * @param image    File path to the product image
     * 
     *                 {@literal @}JsonProperty is used in serialization and
     *                 deserialization
     *                 of the JSON object to the Java object in mapping the fields.
     *                 If a field
     *                 is not provided in the JSON object, the Java field gets the
     *                 default Java
     *                 value, i.e. 0 for int
     */
    public Product(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("tags") String[] tags,
            @JsonProperty("description") String description,
            @JsonProperty("price") double price,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("image") String image) {
        LOG.info("Creating Product with id: " + id + " and name: " + name);
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    /**
     * Increments the quantity by 1
     */
    public void incrementQuantity() {
        LOG.info("Incrementing Product quantity by 1");
        this.quantity++;
    }

    /**
     * Decrements the quantity by 1
     */
    public void decrementQuantity() {
        LOG.info("Decrementing Product quantity by 1");
        this.quantity--;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product other = (Product) o;
            return (this.id == other.id);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, name);
    }

    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Product o) {
        return Double.compare(this.getPrice(), o.getPrice());
    }
}