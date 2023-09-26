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

import com.estore.api.estoreapi.persistence.BuyerInfoDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;
import com.estore.api.estoreapi.model.BuyerInfo;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;

/**
 * Handles the REST API requests for the BuyerInfo resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty
 */

@RestController
@RequestMapping("buyerInformation")
public class BuyerInfoController {
    private static final Logger LOG = Logger.getLogger(BuyerInfoController.class.getName());
    private BuyerInfoDAO buyerInfoDao;
    private UserFileDAO userFileDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param buyerInfoDao The {@link BuyerInfoInfoDAO BuyerInfo Data Access Object}
     *                     to perform CRUD operations
     *                     <br>
     *                     This dependency is injected by the Spring Framework
     */
    public BuyerInfoController(BuyerInfoDAO buyerInfoDao, UserFileDAO userFileDao) {
        this.buyerInfoDao = buyerInfoDao;
        this.userFileDao = userFileDao;
    }

    /**
     * Responds to the GET request for a {@linkplain BuyerInfo buyerInfo} for the
     * given id
     * 
     * @param id The id used to locate the {@link BuyerInfo buyerInfo}
     * 
     * @return ResponseEntity with {@link BuyerInfo buyerInfo} object and HTTP
     *         status of OK if found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<BuyerInfo> getBuyerInfo(@PathVariable int id) {
        LOG.info("GET /buyerInformation/" + id);
        try {
            BuyerInfo buyerInfo = buyerInfoDao.getBuyerInfo(id);
            if (buyerInfo == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<BuyerInfo>(buyerInfo, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain BuyerInfo buyerInfos}
     * 
     * @return ResponseEntity with array of {@link BuyerInfo buyerInfo} objects (may
     *         be empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     *         TODO only admin should be able to see full list of buyerInfos
     */
    @GetMapping("")
    public ResponseEntity<BuyerInfo[]> getBuyerInfos(HttpServletRequest request) {
        LOG.info("GET /buyerInformation");

        try {
            BuyerInfo[] buyerInfos = buyerInfoDao.getBuyerInfos();
            return new ResponseEntity<BuyerInfo[]>(buyerInfos, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain BuyerInfo buyerinfos} whose
     * userid is given
     * 
     * @param userid The userid parameter which {@link BuyerInfo buyerinfos} will
     *               belong to
     * 
     * @return ResponseEntity with array of {@link BuyerInfo buyerinfo} objects (may
     *         be empty) and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     */
    @GetMapping("/this_user")
    public ResponseEntity<BuyerInfo> getBuyerInfoByUser(HttpServletRequest request) {
        User user = getUser(request);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int userid = user.getId();

        try {
            BuyerInfo buyerInfo = buyerInfoDao.getBuyerInfoByUserId(userid);
            return new ResponseEntity<BuyerInfo>(buyerInfo, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain BuyerInfo buyerInfo} with the provided buyerInfo object
     * 
     * @param buyerInfo - The {@link BuyerInfo buyerInfo} to create
     * 
     * @return ResponseEntity with created {@link BuyerInfo buyerInfo} object and
     *         HTTP status of CREATED<br>
     * 
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<BuyerInfo> createBuyerInfo(HttpServletRequest request, @RequestBody BuyerInfo buyerInfo) {
        LOG.info("POST /buyerInformation " + buyerInfo);
        User user = getUser(request);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int userid = user.getId();
        buyerInfo.setUserID(userid);

        try {
            buyerInfo = buyerInfoDao.createBuyerInfo(buyerInfo);
            return new ResponseEntity<BuyerInfo>(buyerInfo, HttpStatus.CREATED);

        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain BuyerInfo buyerInfo} with the provided
     * {@linkplain BuyerInfo buyerInfo} object, if it exists
     * 
     * @param buyerInfo The {@link BuyerInfo buyerInfo} to update
     * 
     * @return ResponseEntity with updated {@link BuyerInfo buyerInfo} object and
     *         HTTP status of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<BuyerInfo> updateBuyerInfo(@RequestBody BuyerInfo buyerInfo) {
        LOG.info("PUT /buyerInformation " + buyerInfo);

        try {
            buyerInfo = buyerInfoDao.updateBuyerInfo(buyerInfo);

            if (buyerInfo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<BuyerInfo>(buyerInfo, HttpStatus.OK);

        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain BuyerInfo buyerInfo} with the given id
     * 
     * @param id The id of the {@link BuyerInfo buyerInfo} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BuyerInfo> deleteBuyerInfo(HttpServletRequest request, @PathVariable int id) {
        LOG.info("DELETE /buyerInformation/" + id);
        boolean isAuthorized = isAuthorized(request, "ADMIN");

        if (!isAuthorized) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            BuyerInfo buyer = buyerInfoDao.deleteBuyerInfo(id);
            if (buyer == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(buyer, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<ArrayList<Integer>> getCart(HttpServletRequest request) {
        User user = getUser(request);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int id = user.getId();
        LOG.info("GET /buyerInformation/" + id + "/cart");

        try {
            BuyerInfo buyer = buyerInfoDao.getBuyerInfoByUserId(id);
            if (buyer == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ArrayList<Integer> cart = buyer.getCart();
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("convertProduct")
    public ResponseEntity<ArrayList<Product>> cartToProduct(HttpServletRequest request) {
        User user = getUser(request);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int id = user.getId();
        LOG.info("Fetching products...");

        try {
            BuyerInfo buyer = buyerInfoDao.getBuyerInfoByUserId(id);
            if (buyer == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ArrayList<Product> cart = buyerInfoDao.fetchProducts(buyer);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO these methods should be moved to their own class

    private boolean isAuthorized(HttpServletRequest request, String requiredRole) {
        User user = getUser(request);
        if (user == null)
            return false;
        return user.getAuthorities().equals(requiredRole);
    }

    private User getUser(HttpServletRequest request) {
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