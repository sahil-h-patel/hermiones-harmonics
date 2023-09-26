package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.estore.api.estoreapi.SpringContext;
import com.estore.api.estoreapi.model.BuyerInfo;
import com.estore.api.estoreapi.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for BuyerInfos
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author Team 2
 */
@Component
public class BuyerInfoFileDAO implements BuyerInfoDAO {
    private static final Logger LOG = Logger.getLogger(BuyerInfoFileDAO.class.getName());
    Map<Integer, BuyerInfo> buyerInfos; // Provides a local cache of the buyerInfo objects
    // so that we don't need to read from the file
    // each time
    private ObjectMapper objectMapper; // Provides conversion between BuyerInfo
                                       // objects and JSON text format written
                                       // to the file
    private static int nextId; // The next Id to assign to a new product
    private String filename; // Filename to read from and write to

    private ProductFileDAO productFileDAOCopy;

    /**
     * Creates a BuyerInfo File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public BuyerInfoFileDAO(@Value("${buyerInfos.file}") String filename, ObjectMapper objectMapper)
            throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load the buyerInfos from the file
        productFileDAOCopy = SpringContext.getBean(ProductFileDAO.class);
    }

    /**
     * Generates the next id for a new {@linkplain Product product}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        return nextId++;
    }

    /**
     * Generates an array of {@linkplain BuyerInfo buyerInfos} from the tree map
     * 
     * @return The array of {@link BuyerInfo buyerInfos}, may be empty
     */
    private BuyerInfo[] getBuyerInfosArray() {
        Collection<BuyerInfo> buyerInfosCollection = buyerInfos.values();
        return buyerInfosCollection.toArray(new BuyerInfo[buyerInfosCollection.size()]);
    }

    /**
     * Saves the {@linkplain BuyerInfo buyerInfos} from the map into the file as an
     * array of JSON objects
     * 
     * @return true if the {@link BuyerInfo buyerInfos} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        BuyerInfo[] buyerInfoArray = getBuyerInfosArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), buyerInfoArray);
        return true;
    }

    /**
     * Loads {@linkplain BuyerInfo buyerInfos} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        buyerInfos = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of buyerInfos
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        BuyerInfo[] buyerInfoArray = objectMapper.readValue(new File(filename), BuyerInfo[].class);

        // Add each product to the tree map and keep track of the greatest id
        for (BuyerInfo buyerInfo : buyerInfoArray) {
            buyerInfos.put(buyerInfo.getId(), buyerInfo);
            if (buyerInfo.getId() > nextId)
                nextId = buyerInfo.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;

        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public BuyerInfo[] getBuyerInfos() {
        synchronized (buyerInfos) {
            return getBuyerInfosArray();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public BuyerInfo getBuyerInfo(int id) {
        LOG.info("Retrieving buyer by id: " + id);
        synchronized (buyerInfos) {
            if (buyerInfos.containsKey(id))
                return buyerInfos.get(id);
            else
                return null;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public BuyerInfo getBuyerInfoByUserId(int userid) throws IOException {
        LOG.info("Retrieving buyer info by associated user id: " + userid);
        synchronized (buyerInfos) {
            for (BuyerInfo buyerInfo : buyerInfos.values()) {
                if (buyerInfo.getUserId() == userid) {
                    return buyerInfo;
                }
            }

            return null;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public BuyerInfo createBuyerInfo(BuyerInfo buyerInfo) throws IOException {
        LOG.info("Create Buyer Info (FILE DAO)");
        synchronized (buyerInfos) {
            BuyerInfo newBuyerInfo = new BuyerInfo(nextId(), buyerInfo.getUserId(), buyerInfo.getName(),
                    buyerInfo.getPhoneNumber(), buyerInfo.getPastOrderIds(), buyerInfo.getCreditCards(),
                    buyerInfo.getShippingAddresses(),
                    buyerInfo.getCart(), buyerInfo.getWishlist());
            buyerInfos.put(newBuyerInfo.getId(), newBuyerInfo);
            save(); // may throw an IOException
            return newBuyerInfo;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public BuyerInfo updateBuyerInfo(BuyerInfo buyerInfo) throws IOException {
        synchronized (buyerInfos) {
            if (!buyerInfos.containsKey(buyerInfo.getId()))
                return null; // buyerInfo does not exist

            buyerInfos.put(buyerInfo.getId(), buyerInfo);
            save(); // may throw an IOException
            return buyerInfo;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public BuyerInfo deleteBuyerInfo(int id) throws IOException {
        synchronized (buyerInfos) {
            if (buyerInfos.containsKey(id)) {
                BuyerInfo buyer = buyerInfos.remove(id);
                save();
                return buyer;
            } else
                return null;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public int calcTotalCost(Collection<Integer> items, BuyerInfo buyerInfo) throws IOException {
        int total = 0;
        for (int id : items) {
            total += productFileDAOCopy.getProduct(id).getPrice();
        }
        return total;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public ArrayList<Product> fetchProducts(BuyerInfo buyerInfo) throws IOException{
        ArrayList<Product> productList = new ArrayList<Product>();
        ArrayList<Integer> idList = (ArrayList<Integer>) buyerInfo.getCart();
        
        for (int id : idList){
            productList.add(productFileDAOCopy.getProduct(id));
        }
        return productList;
        
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public ArrayList<Product> fetchWL(BuyerInfo buyerInfo) throws IOException{
        ArrayList<Product> WLList = new ArrayList<Product>();
        ArrayList<Integer> idList = (ArrayList<Integer>) buyerInfo.getWishlist();

        for (int id : idList){
            WLList.add(productFileDAOCopy.getProduct(id));
        }
        return WLList;
        
    }

}