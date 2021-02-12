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
    public void testPurchaseSharesWithMoreThanEnoughMoney() {
        Stock stock = market.lookUpStock("a");

        assertTrue(market.purchaseShares("a", 10, portfolio));
        assertTrue(stock.getSharesPurchased() == 10);
    }

    @Test
    public void testPurchaseSharesWithNotEnoughMoney() {
       Stock stock = market.lookUpStock("b");

        portfolio.addOrSubtractFromBalance(-9000);
        assertFalse(market.purchaseShares("b", 100000, portfolio));
        assertTrue(portfolio.getBalance() == (portfolio.INITIAL_BALANCE - 9000));
        assertTrue(stock.getSharesPurchased() == 0);
    }

    @Test
    public void testPurchaseSharesWithJustEnoughMoney() {
        Stock stock = market.lookUpStock("b");

        portfolio.setBalance(stock.getAskPrice());
        assertTrue(market.purchaseShares("b", 1, portfolio));
        assertTrue(stock.getSharesPurchased() == 1);
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

    @Test
    public void testSellShares() {
        Stock s1 = market.lookUpStock("a");
        Stock s2 = market.lookUpStock("b");
        Stock s3 = market.lookUpStock("c");

        double previousBalance;

        market.purchaseShares("a", 2, portfolio);
        market.purchaseShares("b", 3, portfolio);

        previousBalance = portfolio.getBalance();

        // when there are sufficient shares owned
        assertEquals("2 shares from a have been sold.", market.sellShares("a", 2, portfolio));
        assertTrue(s1.getSharesPurchased() == 0);
        assertTrue(portfolio.getBalance() == (previousBalance + s1.getAskPrice() * 2));


        // when there are insufficient shares owned
        previousBalance = portfolio.getBalance();

        market.sellShares("b", 4, portfolio);
        assertEquals("Are you sure you entered the correct amount? You currently don't own 4 shares in b.",
                market.sellShares("b", 4, portfolio));
        assertTrue(s2.getSharesPurchased() == 3);
        assertTrue(portfolio.getBalance() == previousBalance);

        // when no shares are owned
        assertEquals("Are you sure you entered the correct company? You currently don't own any shares in c.",
                market.sellShares("c", 3, portfolio));
        assertTrue(s3.getSharesPurchased() == 0);
        assertTrue(portfolio.getBalance() == previousBalance);

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
