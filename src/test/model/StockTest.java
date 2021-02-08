package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {
    Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock("A");
    }

    @Test
    public void testNextDayRandomness() {
        double previousAsk = stock.getAskPrice();
        double previousBid = stock.getBidPrice();

        int timesBidChanged = 0;
        int timesAskChanged = 0;

        for (int i = 0; i < 20; i++) {
            stock.nextDay();
            if (previousAsk != stock.getAskPrice()) {
                timesAskChanged++;
            }

            if (previousBid != stock.getBidPrice()) {
                timesBidChanged++;
            }

            previousAsk = stock.getAskPrice();
            previousBid = stock.getBidPrice();
        }

        assertFalse(timesBidChanged == 0);
        assertFalse(timesAskChanged == 0);

    }

    @Test
    public void testNextDayValueCalculation() {
        stock.nextDay();
        assertEquals(round((stock.getAskPrice() + stock.getBidPrice()) / 2), stock.getCurrentValue());
    }

    @Test
    public void testNextDayPercentChangeCalculation() {
        double previousValue = stock.getCurrentValue();
        stock.nextDay();

        double currentValue = stock.getCurrentValue();
        assertEquals(round((currentValue - previousValue) / previousValue * 100), stock.getPercentChange());
    }

    @Test
    public void testThatAskIsGreaterThanBid() {
        int timesBidIsGreaterThanAsk = 0;

        for (int i = 0; i< 10; i++) {
            if (stock.getAskPrice() > stock.getBidPrice()) {
                timesBidIsGreaterThanAsk++;
            }
            stock = new Stock("B");
        }

        assertTrue(timesBidIsGreaterThanAsk == 10);

        stock.nextDay();

        assertTrue(stock.getAskPrice() > stock.getBidPrice());

    }

    @Test
    public void testPurchaseShares() {
        stock.purchaseShares(1);
        assertTrue(1 == stock.getSharesPurchased());

        stock.purchaseShares(9);
        assertTrue(10 == stock.getSharesPurchased());
    }

    public double round(double num) {
        return Math.round(num * 100.0) / 100.0;
    }

}