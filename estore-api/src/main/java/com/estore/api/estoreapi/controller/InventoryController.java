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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.model.Product;

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
@RequestMapping("inventory")
public class InventoryController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private ProductDAO productDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param productDao The {@link ProductDAO Product Data Access Object} to
     *                   perform CRUD operations
     *                   <br>
     *                   This dependency is injected by the Spring Framework
     */
    public InventoryController(ProductDAO productDao) {
        this.productDao = productDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Product product} for the given
     * id
     * 
     * @param id The id used to locate the {@link Product product}
     * 
     * @return ResponseEntity with {@link Product product} object and HTTP status of
     *         OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        LOG.info("GET /inventory/" + id);
        try {
            Product product = productDao.getProduct(id);
            if (product != null)
                return new ResponseEntity<Product>(product, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Product products}
     * 
     * @return ResponseEntity with array of {@link Product product} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Product[]> getProducts() {
        LOG.info("GET /inventory");
        try {
            Product[] products = productDao.getProducts();
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Product products} whose any
     * attribute contains the text
     * 
     * @param search The search parameter which contains the text used to find the
     *               {@link Product products}
     * 
     * @return ResponseEntity with array of {@link Product product} objects (may be
     *         empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     *         Example: Find all products that contain the text "ma"
     *         GET http://localhost:8080/products/?search=ma
     */
    @GetMapping("/")
    public ResponseEntity<Product[]> searchProducts(@RequestParam String search) {
        LOG.info("GET /inventory/?search=" + search);

        try {
            Product[] products = productDao.findProducts(search);
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/")
    public ResponseEntity<Product[]> getProducts(@RequestParam Integer[] ids){
        LOG.info("GET /inventory/products/?ids=" + Arrays.toString(ids));

        Product[] products = new Product[ids.length];
        try {
            for (int i = 0; i < ids.length; i++) {
                products[i] = productDao.getProduct(ids[i]);
            }            
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Product product} with the provided product object
     * 
     * @param product - The {@link Product product} to create
     * 
     * @return ResponseEntity with created {@link Product product} object and HTTP
     *         status of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Product
     *         product} object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(HttpServletRequest request, @RequestBody Product product) {
        LOG.info("POST /inventory " + product);

        if (!isAuthorized(request, "admin"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            for (Product existingProduct : productDao.getProducts()) {
                if (existingProduct.getName().equals(product.getName())) {
                    return new ResponseEntity<Product>(HttpStatus.CONFLICT);
                }
            }

            product = productDao.createProduct(product);
            return new ResponseEntity<Product>(product, HttpStatus.CREATED);

        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Product product} with the provided
     * {@linkplain Product product} object, if it exists
     * 
     * @param product The {@link Product product} to update
     * 
     * @return ResponseEntity with updated {@link Product product} object and HTTP
     *         status of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Product> updateProduct(HttpServletRequest request, @RequestBody Product product) {
        LOG.info("PUT /inventory " + product);

        if (!isAuthorized(request, "admin"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            if (productDao.getProduct(product.getId()) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            product = productDao.updateProduct(product);
            return new ResponseEntity<Product>(product, HttpStatus.OK);

        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Product product} with the given id
     * 
     * @param id The id of the {@link Product product} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(HttpServletRequest request, @PathVariable int id) {
        LOG.info("DELETE /inventory/" + id);

        if (!isAuthorized(request, "admin"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try {
            if (productDao.getProduct(id) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            productDao.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isAuthorized(HttpServletRequest request, String requiredRole) {
        String auth = request.getHeader("authorization");
        return auth != null && auth.split(":").length == 2 && auth.split(":")[0].equals(requiredRole);
    }
}