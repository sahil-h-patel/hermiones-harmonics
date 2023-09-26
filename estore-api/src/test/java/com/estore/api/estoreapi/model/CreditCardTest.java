package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class CreditCardTest {
        @Test
        public void testCtor() {
                // Setup

                String holderName1 = "Parappa the Rapper";
                String cardNum1 = "55555555";

                // Invoke
                CreditCard card1 = new CreditCard(holderName1, cardNum1);

                // Analyze
                assertEquals(card1.getHolderName(), holderName1);
                assertEquals(card1.getCardNumber(), cardNum1);

                card1.setCardNumber("1234");
                card1.setHolderName("unit test bob");

                assertEquals(card1.getCardNumber(), "1234");
                assertEquals(card1.getHolderName(), "unit test bob");
        }

        @Test
        public void testNotEquals() {
                // Setup

                String holderName1 = "Parappa the Rapper";
                String cardNum1 = "55555555";

                // Invoke
                CreditCard card1 = new CreditCard(holderName1, cardNum1);

                // Setup
                String holderName2 = "Legoshi";
                String cardNum2 = "44444444";

                // Invoke
                CreditCard card2 = new CreditCard(holderName2, cardNum2);

                // Analyze

                assertNotEquals(card1, card2);
        }

        public void testEquals() {
                // Setup

                String holderName1 = "Parappa the Rapper";
                String cardNum1 = "55555555";

                // Invoke
                CreditCard card1 = new CreditCard(holderName1, cardNum1);

                // Setup
                String holderName2 = "Parappa the Rapper";
                String cardNum2 = "44444444";

                // Invoke
                CreditCard card2 = new CreditCard(holderName2, cardNum2);

                // Analyze

                assertEquals(card1, card2);
        }

        @Test
        public void testToString() {
                // Setup
                String holderName = "Parappa the Rapper";
                String cardNum = "55555555";

                String expectedString = String.format(CreditCard.STRING_FORMAT, holderName, cardNum);

                // Invoke
                CreditCard newCard = new CreditCard(holderName, cardNum);
                String actualString = newCard.toString();

                // Analyze
                assertEquals(expectedString, actualString);

        }
}
