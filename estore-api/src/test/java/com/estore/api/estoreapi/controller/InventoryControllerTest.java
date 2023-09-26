package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.ProductDAO;

public class InventoryControllerTest {
  InventoryController inventoryController;

  ProductDAO mockProductDAO;
  HttpServletRequest mockRequest;

  @BeforeEach
  public void setUp() {
    mockProductDAO = mock(ProductDAO.class);
    mockRequest = mock(HttpServletRequest.class);
    inventoryController = new InventoryController(mockProductDAO);
  }

  @Test
  public void testGetProducts() throws IOException {
    Integer[] ids = {1, 2, 3, 4};

    when(mockProductDAO.getProduct(0)).thenThrow(new IOException());

    ResponseEntity<Product[]> res = inventoryController.getProducts(ids);

    assertEquals(HttpStatus.OK, res.getStatusCode());

    when(mockProductDAO.getProduct(1)).thenThrow(new IOException());

    res = inventoryController.getProducts(ids);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
  
    

  }

  @Test
  public void testGetProd() throws IOException{
    when(mockProductDAO.getProducts()).thenReturn(new Product[3]);
    // Exercise

    ResponseEntity<Product[]> response = inventoryController.getProducts();
    assertNotNull(response.getBody());
    assertEquals(3, response.getBody().length);

    when(mockProductDAO.getProducts()).thenThrow(new IOException());
    response = inventoryController.getProducts();
    assertNull(response.getBody());

    Product mockProduct = mock(Product.class);
    when(mockProduct.getName()).thenReturn("product1");
    when(mockProductDAO.getProduct(1)).thenReturn(mockProduct);
    ResponseEntity<Product> response2 = inventoryController.getProduct(1);
    assertEquals("product1", response2.getBody().getName());

    when(mockProductDAO.getProduct(1)).thenReturn(null);
    response2 = inventoryController.getProduct(1);
    assertEquals(404, response2.getStatusCodeValue());

    when(mockProductDAO.getProduct(1)).thenThrow(new IOException());
    response2 = inventoryController.getProduct(1);
    assertEquals(500, response2.getStatusCodeValue());
  }

  @Test
  public void testSearchProd() throws IOException{
    when(mockProductDAO.findProducts("a")).thenReturn(new Product[3]);

    ResponseEntity<Product[]> response = inventoryController.searchProducts("a");
    assertNotNull(response.getBody());
    assertEquals(3, response.getBody().length);

    when(mockProductDAO.findProducts("a")).thenThrow(new IOException());
    response = inventoryController.searchProducts("a");
    assertNull(response.getBody());
  }

  @Test
  public void testCreateProd() throws IOException {
    Product mockProduct = mock(Product.class);
    when(mockProduct.getName()).thenReturn("oldProduct");
    when(mockRequest.getHeader("authorization")).thenReturn("admin:admin");
    Product[] mockProducts = { mockProduct };
    when(mockProductDAO.getProducts()).thenReturn(mockProducts);
    Product newProduct = mock(Product.class);
    when(newProduct.getName()).thenReturn("newProduct");

    ResponseEntity<Product> response = inventoryController.createProduct(mockRequest, newProduct);
    assertEquals(response.getStatusCodeValue(), 201);

    when(mockRequest.getHeader("authorization")).thenReturn("user:user");
    response = inventoryController.createProduct(mockRequest, newProduct);
    assertEquals(response.getStatusCodeValue(), 401);

    when(mockRequest.getHeader("authorization")).thenReturn("admin:admin");
    when(newProduct.getName()).thenReturn("oldProduct");
    response = inventoryController.createProduct(mockRequest, newProduct);
    assertEquals(response.getStatusCodeValue(), 409);

    when(newProduct.getName()).thenReturn("newProduct");
    when(mockProductDAO.createProduct(newProduct)).thenThrow(new IOException());
    response = inventoryController.createProduct(mockRequest, newProduct);
    assertEquals(response.getStatusCodeValue(), 500);
  }

  @Test
  public void testUpdateProd() throws IOException {
    Product mockProduct = mock(Product.class);
    when(mockProduct.getName()).thenReturn("oldProduct");
    when(mockProductDAO.getProduct(1)).thenReturn(mockProduct);
    when(mockRequest.getHeader("authorization")).thenReturn("admin:admin");
    Product[] mockProducts = { mockProduct };
    when(mockProductDAO.getProducts()).thenReturn(mockProducts);
    Product newProduct = mock(Product.class);
    when(newProduct.getId()).thenReturn(1);

    ResponseEntity<Product> response = inventoryController.updateProduct(mockRequest, newProduct);
    assertEquals(response.getStatusCodeValue(), 200);

    when(mockRequest.getHeader("authorization")).thenReturn("useruser");
    response = inventoryController.updateProduct(mockRequest, newProduct);
    assertEquals(response.getStatusCodeValue(), 401);

    when(mockRequest.getHeader("authorization")).thenReturn("admin:admin");
    when(newProduct.getId()).thenReturn(3);
    response = inventoryController.updateProduct(mockRequest, newProduct);
    assertEquals(response.getStatusCodeValue(), 404);

    when(mockProductDAO.getProduct(3)).thenThrow(new IOException());
    response = inventoryController.updateProduct(mockRequest, newProduct);
  }

  @Test
  public void testDeleteProd() throws IOException {
    Product mockProduct = mock(Product.class);
    when(mockProduct.getName()).thenReturn("oldProduct");
    when(mockProductDAO.getProduct(1)).thenReturn(mockProduct);
    when(mockRequest.getHeader("authorization")).thenReturn("admin:admin");
    Product[] mockProducts = { mockProduct };
    when(mockProductDAO.getProducts()).thenReturn(mockProducts);

    ResponseEntity<Product> response = inventoryController.deleteProduct(mockRequest, 1);
    assertEquals(response.getStatusCodeValue(), 200);

    when(mockRequest.getHeader("authorization")).thenReturn(null);
    response = inventoryController.deleteProduct(mockRequest, 1);
    assertEquals(response.getStatusCodeValue(), 401);

    when(mockRequest.getHeader("authorization")).thenReturn("admin:admin");
    when(mockProductDAO.getProduct(1)).thenReturn(null);
    response = inventoryController.deleteProduct(mockRequest, 1);
    assertEquals(response.getStatusCodeValue(), 404);

    when(mockProductDAO.getProduct(1)).thenThrow(new IOException());
    response = inventoryController.deleteProduct(mockRequest, 1);
    assertEquals(response.getStatusCodeValue(), 500);
  }
}
