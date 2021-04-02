package model;

import org.json.JSONObject;
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
    public void testAlternateConstructor() {
        stock = new Stock("A", 10, 11, 12, 0.10, 13);
        assertEquals("A", stock.getName());
        assertEquals(stock.getAskPrice(), 10);
        assertEquals(stock.getBidPrice(), 11);
        assertEquals(stock.getCurrentValue(), 12);
        assertEquals(stock.getPercentChange(), 0.10);
        assertEquals(stock.getSharesPurchased(), 13);
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

        assertNotEquals(timesBidChanged, 0);
        assertNotEquals(timesAskChanged, 0);

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

        assertEquals(timesBidIsGreaterThanAsk, 10);

        stock.nextDay();

        assertTrue(stock.getAskPrice() > stock.getBidPrice());

    }

    @Test
    public void testPurchaseShares() {
        stock.purchaseShares(1);
        assertEquals(stock.getSharesPurchased(), 1);

        stock.purchaseShares(9);
        assertEquals(stock.getSharesPurchased(), 10);
    }

    @Test
    public void testRemoveShares() {
        stock.purchaseShares(100);

        // when there are sufficient shares
        assertTrue(stock.removeShares(10));
        assertEquals(stock.getSharesPurchased(), 90);

        assertTrue(stock.removeShares(90));
        assertEquals(stock.getSharesPurchased(), 0);

        // when there are insufficient shares
        assertFalse(stock.removeShares(5));
        assertEquals(stock.getSharesPurchased(), 0);

        stock.purchaseShares(20);
        assertFalse(stock.removeShares(21));
        assertEquals(stock.getSharesPurchased(), 20);
    }



    @Test
    public void testGenerateBid() {
        double spread = stock.getAskPrice() - stock.getBidPrice();
        double previousBid;

        // when rise in bid is equal to spread
        stock.generateBid(true, spread);
        assertTrue(stock.getBidPrice() < stock.getAskPrice());

        // when rise in bid is greater than spread
        stock.generateBid(true, spread + 10);
        assertTrue(stock.getBidPrice() < stock.getAskPrice());

        // when rise in bid is less than spread
        spread = stock.getAskPrice() - stock.getBidPrice();
        previousBid = stock.getBidPrice();
        stock.generateBid(true, spread - 2);
        assertEquals((previousBid + (spread - 2)), stock.getBidPrice());

        // when bid decreases
        previousBid = stock.getBidPrice();
        stock.generateBid(false, spread);
        assertEquals((previousBid - spread), stock.getBidPrice());
    }

    @Test
    public void testGenerateAsk() {
        double previousAsk = stock.getAskPrice();

        // rise in ask
        stock.generateAsk(true, 5);
        assertEquals((previousAsk + 5), stock.getAskPrice());

        // decrease in ask
        previousAsk = stock.getAskPrice();
        stock.generateAsk(false, 5);
        assertEquals((previousAsk - 5), stock.getAskPrice());

    }

    @Test
    public void testToJson() {
        JSONObject json = stock.toJson();

        assertEquals("A", json.getString("company"));
        assertEquals(json.getDouble("ask value"), stock.getAskPrice());
        assertEquals(json.getDouble("bid value"), stock.getBidPrice());
        assertEquals(json.getDouble("percentage change"), stock.getPercentChange());
        assertEquals(json.getInt("shares purchased"), stock.getSharesPurchased());

    }

    private double round(double num) {
        return Math.round(num * 100.0) / 100.0;
    }

}