package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    Market market;
    Portfolio portfolio;

    @BeforeEach
    public void setUp() {
        String[] companies = {"a", "b", "c"};
        portfolio = new Portfolio();
        market = new Market(companies);
    }

    @Test
    public void testLookUpStock() {
        ArrayList<Stock> catalogue = market.getCatalogue();

        assertEquals(catalogue.get(0), market.lookUpStock("a"));
        assertEquals(catalogue.get(1), market.lookUpStock("b"));
    }

    @Test
    public void testPurchaseSharesAboveBoundary() {
        assertTrue(market.purchaseShares("a", 10, portfolio));
    }

    @Test
    public void testPurchaseSharesBelowBoundary() {
        portfolio.addOrSubtractFromBalance(-9000);
        assertFalse(market.purchaseShares("b", 100000, portfolio));
        assertTrue(portfolio.getBalance() == (portfolio.INITIAL_BALANCE - 9000));
    }

    @Test
    public void testPurchaseSharesAtBoundary() {
        double askPrice = market.lookUpStock("b").getAskPrice();
        portfolio.setBalance(askPrice);
        assertTrue(market.purchaseShares("b", 1, portfolio));
    }

    @Test
    public void testNextDay() {
        String[] names = new String[0];
        Market wellBehavedMarket = new Market(names);
        ArrayList<Stock> catalogue = wellBehavedMarket.getCatalogue();

        WellBehavedStock s1 = new WellBehavedStock("a");
        s1.setAskPrice(5);
        s1.setBidPrice(4);

        WellBehavedStock s2 = new WellBehavedStock("b");
        s2.setAskPrice(10);
        s2.setBidPrice(7);

        WellBehavedStock s3 = new WellBehavedStock("c");
        s3.setAskPrice(100);
        s3.setBidPrice(90);

        wellBehavedMarket.addStock(s1);
        wellBehavedMarket.addStock(s2);
        wellBehavedMarket.addStock(s3);

        wellBehavedMarket.nextDay();

        assertTrue(s1.getAskPrice() == 6);
        assertTrue(s1.bidPrice == 3);

        assertTrue(s2.getAskPrice() == 11);
        assertTrue(s2.getBidPrice() == 6);

        assertTrue(s3.getAskPrice() == 101);
        assertTrue(s3.getBidPrice() == 89);

    }

    private class WellBehavedStock extends Stock {

        // EFFECTS:
        public WellBehavedStock(String name) {
            super(name);
        }

        // MODIFIES: this
        // EFFECTS: increases and decreases the ask price and bid price by 1 increment
        //          calculates the percentage change and current value
        @Override
        public void nextDay() {
            double previousValue = currentValue;

            bidPrice -= 1;
            askPrice += 1;
            currentValue = (bidPrice + askPrice) / 2;
            percentChange = (currentValue - previousValue) / previousValue * 100;
        }

        // REQUIRES: bidPrice > 0
        // MODIFIES: this
        // EFFECTS: sets the bid price of the stock
        public void setBidPrice(double bidPrice) {
            super.bidPrice = bidPrice;
        }

        // REQUIRES: askPrice > 0
        // MODIFIES: this
        // EFFECTS: sets the ask price of the stock
        public void setAskPrice(double askPrice) {
            super.askPrice = askPrice;
        }

    }


}
