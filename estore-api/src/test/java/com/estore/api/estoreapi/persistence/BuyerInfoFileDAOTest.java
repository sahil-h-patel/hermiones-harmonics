package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.estore.api.estoreapi.SpringContext;
import com.estore.api.estoreapi.model.BuyerInfo;
import com.estore.api.estoreapi.model.CreditCard;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;


/**
 * Test the BuyerInfo File DAO Class
 * 
 * @author Team 2
 */
@Tag("Persistence-tier")
public class BuyerInfoFileDAOTest {
    BuyerInfoFileDAO buyerInfoFileDAO;
    BuyerInfo[] testBuyerInfos;
    ObjectMapper mockObjectMapper;
    ApplicationContext mockApplicationContext;
    BuyerInfoFileDAO mockBuyerInfoFileDAO;
    SpringContext context = new SpringContext();

    @BeforeEach
    public void setupBuyerInfoFileDAO() throws IOException {
        mockApplicationContext = mock(ApplicationContext.class);
        mockBuyerInfoFileDAO = mock(BuyerInfoFileDAO.class);

        mockObjectMapper = mock(ObjectMapper.class);
        testBuyerInfos = new BuyerInfo[1];

        when(mockApplicationContext.getBean(BuyerInfoFileDAO.class)).thenReturn(mockBuyerInfoFileDAO);

        context.setApplicationContext(mockApplicationContext);

        Collection<Integer> pastOrderIds = new ArrayList<>();
        pastOrderIds.add(1);
        pastOrderIds.add(2);

        CreditCard card = new CreditCard("John B. Buyer", "1234");
        Collection<CreditCard> creditCards = new ArrayList<>();
        creditCards.add(card);

        Collection<String> shippingAddresses = new ArrayList<>();
        shippingAddresses.add("towny mc town face");

        ArrayList<Integer> cart = new ArrayList<>();
        cart.add(4);

        ArrayList<Integer> wishlist = new ArrayList<>();
        wishlist.add(5);

        testBuyerInfos[0] = new BuyerInfo(1, 2, "John Buyer", "555-123-4567", 
                                          pastOrderIds, creditCards, shippingAddresses, cart, wishlist);
        

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the buyerInfo array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),BuyerInfo[].class))
                .thenReturn(testBuyerInfos);
        buyerInfoFileDAO = new BuyerInfoFileDAO("doesnt_matter.txt",mockObjectMapper);

    }

    @Test
    public void testGetBuyerInfos() {
        // Invoke
        BuyerInfo[] buyerInfos = buyerInfoFileDAO.getBuyerInfos();

        // Analyze
        assertArrayEquals(buyerInfos, testBuyerInfos);
    }

    @Test
    public void testGetBuyerInfo() {
        // Invoke
        BuyerInfo buyerInfo = buyerInfoFileDAO.getBuyerInfo(1);

        // Analzye
        assertEquals(buyerInfo, testBuyerInfos[0]);
    }

    @Test
    public void testCreateBuyerInfo() {
        // Setup
        Collection<Integer> pastOrderIds = new ArrayList<>();
        pastOrderIds.add(6);
        pastOrderIds.add(7);

        CreditCard card = new CreditCard("Joe Setup", "1234");
        Collection<CreditCard> creditCards = new ArrayList<>();
        creditCards.add(card);

        Collection<String> shippingAddresses = new ArrayList<>();
        shippingAddresses.add("whoville");

        ArrayList<Integer> cart = new ArrayList<>();

        ArrayList<Integer> wishlist = new ArrayList<>();
        wishlist.add(2);
        BuyerInfo buyerInfo = new BuyerInfo(2, 3, "joe setup", 
                                "999-999-9999", pastOrderIds, creditCards, shippingAddresses, cart, wishlist);

        // Invoke
        BuyerInfo result = assertDoesNotThrow(() -> buyerInfoFileDAO.createBuyerInfo(buyerInfo),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        BuyerInfo actual = buyerInfoFileDAO.getBuyerInfo(buyerInfo.getId());
        assertEquals(actual.getId(), buyerInfo.getId());
        assertEquals(actual.getUserId(), buyerInfo.getUserId());
        assertEquals(actual.getName(), buyerInfo.getName());
        assertEquals(actual.getPhoneNumber(), buyerInfo.getPhoneNumber());
        assertEquals(actual.getPastOrderIds(), buyerInfo.getPastOrderIds());
        assertEquals(actual.getCreditCards(), buyerInfo.getCreditCards());
        assertEquals(actual.getShippingAddresses(), buyerInfo.getShippingAddresses());
        assertEquals(actual.getCart(), buyerInfo.getCart());
        assertEquals(actual.getWishlist(), buyerInfo.getWishlist());
    }

    @Test
    public void testUpdateBuyerInfo() {
        // Setup
        Collection<Integer> pastOrderIds = new ArrayList<>();
        pastOrderIds.add(6);
        pastOrderIds.add(7);

        CreditCard card = new CreditCard("Joe Setup", "1234");
        Collection<CreditCard> creditCards = new ArrayList<>();
        creditCards.add(card);

        Collection<String> shippingAddresses = new ArrayList<>();
        shippingAddresses.add("whoville");

        ArrayList<Integer> cart = new ArrayList<>();

        ArrayList<Integer> wishlist = new ArrayList<>();
        wishlist.add(2);
        BuyerInfo buyerInfo = new BuyerInfo(2, 3, "joe setup", 
                                "999-999-9999", pastOrderIds, creditCards, shippingAddresses, cart, wishlist);

        try {
        buyerInfoFileDAO.createBuyerInfo(buyerInfo);
        } catch (IOException ioe) {/* SQUASH */}

        // Invoke
        BuyerInfo result = assertDoesNotThrow(() -> buyerInfoFileDAO.updateBuyerInfo(buyerInfo),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        BuyerInfo actual = buyerInfoFileDAO.getBuyerInfo(buyerInfo.getId());
        assertEquals(actual, buyerInfo);
    }

    @Test
    public void testDeleteBuyerInfo() {
        // Invoke
        BuyerInfo result1 = assertDoesNotThrow(() -> buyerInfoFileDAO.deleteBuyerInfo(999),
                            "Unexpected exception thrown");
        // Analzye
        assertEquals(result1, null);


        BuyerInfo result2 = assertDoesNotThrow(() -> buyerInfoFileDAO.deleteBuyerInfo(1),
                            "Unexpected exception thrown");
        assertEquals(result2, testBuyerInfos[0]);
        // We check the internal tree map size against the length
        // of the test buyerInfos array - 1 (because of the delete)
        // Because buyerInfos attribute of BuyerInfoFileDAO is package private
        // we can access it directly
        assertEquals(buyerInfoFileDAO.buyerInfos.size(), testBuyerInfos.length - 1);


    }

}
