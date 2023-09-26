package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class ProductFileDAOTest {
    ProductFileDAO productFileDAO;
    Product[] testProducts;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupProductFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);

        String[] tags = {"Brass", "Heavy"};
        String[] tags2 = {"Woodwind"};
        String[] tags3 = {"String"};

        testProducts = new Product[3];
        testProducts[0] = new Product(1, "Tuba", tags, "A large tuba", 599.99, 5, "img.png");
        testProducts[1] = new Product(2, "Clarinet", tags2, "Squidwards instrument", 349.99, 7, "img2.png");
        testProducts[2] = new Product(3, "Guitar", tags3, "Cool", 499.99, 4, "img3.png");

        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Product[].class))
                .thenReturn(testProducts);

        productFileDAO = new ProductFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        doThrow(new IOException()).when(mockObjectMapper).readValue(new File("doesnt_matter.txt"), Product[].class);

        // Invoke & Analyze
        assertThrows(IOException.class, () -> new ProductFileDAO("doesnt_matter.txt", mockObjectMapper),
                "IOException not thrown");
    }

    @Test
    public void testGetProducts() {
        // Setup
        Product[] expected = testProducts;

        // Invoke
        Product[] actual = productFileDAO.getProducts();

        // Analyze
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFindProducts() {
        // Setup
        String search = "i";
        int index1 = 1;
        int index2 = 2;
        Product[] expected = { testProducts[index1], testProducts[index2] };

        // Invoke
        Product[] actual = productFileDAO.findProducts(search);

        // Analyze
        Set<Product> expectedSet = new HashSet<>(Arrays.asList(expected));
        Set<Product> actualSet = new HashSet<>(Arrays.asList(actual));

        assertTrue(actualSet.containsAll(expectedSet), "Expected products not found in the search results");
        assertEquals(expectedSet.size(), actualSet.size(), "Unexpected products found in the search results");
    }

    @Test
    public void testGetProduct() {
        // Setup
        int index = 0; // First id is at index 0.
        Product expected = testProducts[index];

        // Invoke
        int id = 1;
        Product actual = productFileDAO.getProduct(id);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteProduct() {
        // Setup
        int index0 = 0;
        int index1 = 1;
        Product[] expected = { testProducts[index0], testProducts[index1] };

        // Invoke
        try {
            productFileDAO.deleteProduct(3);
        } catch (IOException e) {
            /* Squash */}

        Product[] actual = productFileDAO.getProducts();

        // Analyze
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCreateProduct() {
        // Setup
        int index0 = 0;
        int index1 = 1;
        int index2 = 2;
        String[] tags = {"Wind", "Light"};
        Product newProduct = new Product(4, "Flute", tags, "Small and sleek", 249.99, 3, "img.png");
        Product[] expected = { testProducts[index0], testProducts[index1], testProducts[index2], newProduct };

        // Invoke
        try {
            productFileDAO.createProduct(newProduct);
        } catch (IOException e) {
            /* Squash */}

        Product[] actual = productFileDAO.getProducts();

        // Analyze
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testUpdateProduct() {
        // Setup
        int index0 = 0;
        int index1 = 1;
        String[] tags = {"String", "Cool"};
        Product updatedProduct = new Product(3, "Electric Guitar", tags, "Very cool and modern", 599.99, 9, "img.png");
        Product[] expected = { testProducts[index0], testProducts[index1], updatedProduct };

        // Invoke
        try {
            productFileDAO.updateProduct(updatedProduct);
        } catch (IOException e) {
            /* Squash */}

        Product[] actual = productFileDAO.getProducts();

        // Analyze
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNonExistantProduct() throws IOException {
        // Setup
        int id = 4;

        // Invoke
        Product actual = productFileDAO.getProduct(id);

        // Analyze
        assertNull(actual);

        Product mockedProduct = mock(Product.class);
        when(mockedProduct.getId()).thenReturn(id);

        Product actual2 = productFileDAO.updateProduct(mockedProduct);

        assertNull(actual2);

        assertFalse(productFileDAO.deleteProduct(id));
    }
}
