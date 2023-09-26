package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class ProductTest {

    @Test
    public void testID() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        int expected = 4;

        // Invoke
        int actual = product.getId();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        String expected = "Saxophone";

        // Invoke
        String actual = product.getName();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testSetName() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        String expected = "Alto Saxophone";

        // Invoke
        product.setName("Alto Saxophone");
        String actual = product.getName();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPrice() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        double expected = 699.99;

        // Invoke
        double actual = product.getPrice();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPrice() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        double expected = 799.99;

        // Invoke
        product.setPrice(799.99);
        double actual = product.getPrice();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuantity() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        int expected = 7;

        // Invoke
        double actual = product.getQuantity();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testSetQuantity() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        int expected = 5;

        // Invoke
        product.setQuantity(5);
        double actual = product.getQuantity();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        String[] expected = {"Brass", "Wind"};

        // Invoke
        String[] actual = product.getTags();

        // Analyze
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSetTags() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        String[] expected = {"Gold", "Wind"};

        // Invoke
        String[] newTags = {"Gold", "Wind"};
        product.setTags(newTags);
        String[] actual = product.getTags();

        // Analyze
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetImage() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        String expected = "img.png";

        // Invoke
        String actual = product.getImage();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testSetImage() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        String expected = "img2.png";

        // Invoke
        String newImg = "img2.png";
        product.setImage(newImg);
        String actual = product.getImage();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testIncrementQuantity() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        int expected = 8;

        // Invoke
        product.incrementQuantity();
        double actual = product.getQuantity();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testDecrementQuantity() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        int expected = 6;

        // Invoke
        product.decrementQuantity();
        double actual = product.getQuantity();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        // Setup
        int id = 4;
        String name = "Saxophone";
        double price = 699.99;
        int quantity = 7;
        String description = "Brass saxophone, jazzy.";
        String[] tags = {"Brass", "Wind"};
        String image = "img.png";
        Product product = new Product(id, name, tags, description, price, quantity, image);
        String expected = "Product [id=4, name=Saxophone]";

        // Invoke
        String actual = product.toString();

        // Analyze
        assertEquals(expected,actual);
    }

    @Test
    public void testEquals() {
        String[] tags1 = {"Brass"};
        String[] tags2 = {"Brass"};
        String[] tags3 = {"Brass", "Heavy"};
        Product product1 = new Product(1, "sax", tags1, "jazzy", 20, 2, "img.png");
        Product product2 = new Product(1, "sax", tags2, "jazzy", 20, 2, "img.png");
        Product product3 = new Product(2, "trombone", tags3, "not too jazzy", 999, 1,"img2.png");
        Object o = new Object();

        assertEquals(product1.equals(product2), true);
        assertEquals(product1.equals(product3), false);
        assertEquals(product1.equals(o), false);
    }

    @Test
    public void testSetDescAndCompare() {
        String[] tags1 = {"Brass"};
        Product product1 = new Product(1, "sax", tags1, "jazzy", 20, 2, "img.png");
        
        product1.setDescription("desc");

        assertEquals(product1.getDescription(), "desc");

         String[] tags2 = {"Brass"};
         Product product2 = new Product(1, "sax", tags2, "jazzy", 20, 2, "img.png");
    
        assertEquals(product1.compareTo(product2), 0);

    }
    
}
