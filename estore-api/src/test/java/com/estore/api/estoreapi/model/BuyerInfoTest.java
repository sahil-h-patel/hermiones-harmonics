package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class BuyerInfoTest {
    private int expectedId;
    private int expectedUserId;
    private String expectedName;
    private String expectedPhoneNumber;
    private Collection<Integer> expectedPastOrderIds;
    private Collection<CreditCard> expectedCreditCards;
    private Collection<String> expectedShippingAddresses;
    private ArrayList<Integer> expectedProductCart;
    private ArrayList<Integer> expectedProductWishlist;
    private BuyerInfo buyerInfo;
    
    @BeforeEach
    public void setupBuyerInfoTest() {
        expectedId = 1;
        expectedUserId = 2;
        expectedName = "joe smith";
        expectedPhoneNumber = "222-222-2222";

        expectedPastOrderIds = new ArrayList<>();
        expectedPastOrderIds.add(1);
        expectedPastOrderIds.add(2);

        CreditCard card = new CreditCard("joe smith", "1234");
        expectedCreditCards = new ArrayList<>();
        expectedCreditCards.add(card);

        expectedShippingAddresses = new ArrayList<>();
        expectedShippingAddresses.add("stupid town");

        expectedProductCart = new ArrayList<>();
        expectedProductCart.add(4);

        expectedProductWishlist = new ArrayList<>();
        expectedProductWishlist.add(5);


        buyerInfo = new BuyerInfo(expectedId, expectedUserId, expectedName, expectedPhoneNumber, expectedPastOrderIds, expectedCreditCards, 
                          expectedShippingAddresses, expectedProductCart, expectedProductWishlist);
    }


    @Test
    public void testGetID() {

        assertEquals(expectedId, buyerInfo.getId());
    }

    @Test
    public void testGetUserId() {
        assertEquals(expectedUserId, buyerInfo.getUserId());
    }

    @Test
    public void testSetUserId() {

        buyerInfo.setUserID(1); 
        double actualID = buyerInfo.getUserId();
        assertEquals(expectedId, actualID);

    }

    @Test
    public void testGetName() {
        assertEquals(expectedName, buyerInfo.getName());
    }

      @Test
    public void testSetName() {
        buyerInfo.setName("jill smith");
        String actualName = buyerInfo.getName();
        assertEquals("jill smith", actualName);
    }

    @Test
    public void testGetPhoneNumber() {
        assertEquals(expectedPhoneNumber, buyerInfo.getPhoneNumber());
    }

    @Test
    public void testSetPhoneNumber() {
        buyerInfo.setPhoneNumber("333-333-3333");
        String actualPhoneNumber = buyerInfo.getPhoneNumber();
        assertEquals("333-333-3333", actualPhoneNumber);
    }

    @Test
    public void testGetPastOrderIds() {
        assertEquals(expectedPastOrderIds, buyerInfo.getPastOrderIds());
    }

    @Test
    public void testGetCreditCards() {
        assertEquals(expectedCreditCards, buyerInfo.getCreditCards());
    }

    @Test
    public void testSetCreditCards() {
        CreditCard card1 = new CreditCard("jill smith", "3333");
        CreditCard card2 = new CreditCard("jane doe", "4444");

        Collection<CreditCard> newCreditCards = new ArrayList<>();
        newCreditCards.add(card1);
        newCreditCards.add(card2);

        buyerInfo.setCreditCards(newCreditCards);
        Collection<CreditCard>  actualCreditCards =  buyerInfo.getCreditCards();
        assertEquals(newCreditCards, actualCreditCards);
    }

    @Test
    public void testGetShippingAddresses() {
        assertEquals(expectedShippingAddresses, buyerInfo.getShippingAddresses());
    }

    @Test
    public void testSetShippingAddresses() {

        ArrayList<String> newShippingAddresses = new ArrayList<String>();
        newShippingAddresses.add("stupid town");
        newShippingAddresses.add("bougie hills");

        buyerInfo.setShippingAddresses(newShippingAddresses);
        Collection<String> actualShippingAddresses = buyerInfo.getShippingAddresses();
        assertEquals(newShippingAddresses, actualShippingAddresses);
    }

    @Test
    public void testAddProductCart() {
        ArrayList<Integer> expectedCart = new ArrayList<>(expectedProductCart);
        expectedCart.add(3);

        buyerInfo.getCart().add(3); 

        ArrayList<Integer> actualCart = buyerInfo.getCart();
        assertEquals(expectedCart, actualCart); 
    }

    @Test
    public void testRemoveProductCart() {
        String[] tags1 = {"Brass"};
        Product product1 = new Product(0, "sax", tags1, "jazzy", 20, 2, "img.png");
        buyerInfo.removeProductCart(product1);

        assertEquals(0, buyerInfo.getCart().size());
    }

    @Test
    public void testAddProductWishlist() {

        ArrayList<Integer> expectedWishlist = new ArrayList<>(expectedProductWishlist);

        expectedWishlist.add(3);
        buyerInfo.getWishlist().add(3);

        ArrayList<Integer> actualWishlist = buyerInfo.getWishlist();
        assertEquals(expectedWishlist, actualWishlist);
    }

    @Test
    public void testToString() {
        String expectedString = String.format(BuyerInfo.STRING_FORMAT, expectedId, expectedUserId, 
                                              expectedName, expectedPhoneNumber,
                                              expectedPastOrderIds, expectedCreditCards, 
                                              expectedShippingAddresses, expectedProductCart, expectedProductWishlist);

        assertEquals(expectedString, buyerInfo.toString());
    }
}
