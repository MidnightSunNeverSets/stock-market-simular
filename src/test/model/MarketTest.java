package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    Market market;
    Portfolio portfolio;
    Stock s1, s2, s3;

    @BeforeEach
    public void setUp() {
        String[] companies = {"a", "b", "c"};
        portfolio = new Portfolio();
        market = new Market(companies);
        s1 = market.getCatalogue().get(0);
        s2 = market.getCatalogue().get(1);
        s3 = market.getCatalogue().get(2);

    }

    @Test
    public void testAlternateConstructor() {
        market = new Market();
        assertEquals(market.getCatalogue().size(), 0);
        assertEquals(market.catalogueNames.size(), 0);
    }

    @Test
    public void testLookUpStock() {
        assertEquals(s1, market.lookUpStock("a"));
        assertEquals(s2, market.lookUpStock("b"));
    }

    @Test
    public void testPurchaseSharesWithMoreThanEnoughMoney() {
        Stock stock = market.lookUpStock("a");

        assertTrue(market.purchaseShares("a", 10, portfolio));
        assertEquals(10, stock.getSharesPurchased());
    }

    @Test
    public void testPurchaseSharesWithNotEnoughMoney() {
       Stock stock = market.lookUpStock("b");

        portfolio.addOrSubtractFromBalance(-9000);
        assertFalse(market.purchaseShares("b", 100000, portfolio));
        assertEquals(Portfolio.INITIAL_BALANCE - 9000, portfolio.getBalance());
        assertEquals(0, stock.getSharesPurchased());
    }

    @Test
    public void testPurchaseSharesWithJustEnoughMoney() {
        Stock stock = market.lookUpStock("b");

        portfolio.setBalance(stock.getAskPrice());
        assertTrue(market.purchaseShares("b", 1, portfolio));
        assertEquals(1, stock.getSharesPurchased());
    }

    @Test
    public void testNextDay() {
        String[] names = new String[0];
        Market wellBehavedMarket = new Market(names);

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

        assertEquals(6, s1.getAskPrice());
        assertEquals(3, s1.bidPrice);

        assertEquals(11, s2.getAskPrice());
        assertEquals(6, s2.getBidPrice());

        assertEquals(101, s3.getAskPrice());
        assertEquals(89, s3.getBidPrice());

    }

    @Test
    public void testSellShares() {

        double previousBalance;
        market.purchaseShares("a", 2, portfolio);
        market.purchaseShares("b", 3, portfolio);

        previousBalance = portfolio.getBalance();

        // when there are sufficient shares owned
        assertTrue(market.sellShares("a", 1, portfolio));
        assertEquals(s1.getSharesPurchased(), 1);
        assertEquals(previousBalance + s1.getAskPrice(), portfolio.getBalance());

    }

    @Test
    public void testSellInsufficientShares() {
        market.purchaseShares("b", 2, portfolio);
        double previousBalance = portfolio.getBalance();

        assertFalse(market.sellShares("b", 4, portfolio));
        assertEquals(s2.getSharesPurchased(), 2);
        assertEquals(previousBalance, portfolio.getBalance());

        // when no shares are owned
        assertFalse(market.sellShares("c", 3, portfolio));
        assertEquals(s3.getSharesPurchased(), 0);
        assertEquals(previousBalance, portfolio.getBalance());
    }

    @Test
    public void testSellAllShares() {
        assertFalse(portfolio.getStocksOwned().contains(s1));
        market.purchaseShares("a", 2, portfolio);
        assertTrue(market.sellShares("a", 2, portfolio));
        assertEquals(0, s1.getSharesPurchased());
        assertEquals(100000, Portfolio.INITIAL_BALANCE);
        assertFalse(portfolio.getStocksOwned().contains(s1));
    }

    @Test
    public void testToJson() {
        JSONObject json = market.toJson();
        JSONArray jsonArr = json.getJSONArray("stock catalogue");
        ArrayList<Stock> catalogue = market.getCatalogue();

        assertEquals(jsonArr.length(), 3);

        int count = 0;

        for (Object jo: jsonArr) {
            JSONObject jsonObject = (JSONObject) jo;
            Stock stock = catalogue.get(count);

            assertEquals(stock.getName(), jsonObject.getString("company"));
            assertEquals(jsonObject.getDouble("current value"), stock.getCurrentValue());
            assertEquals(jsonObject.getDouble("ask value"), stock.getAskPrice());
            assertEquals(jsonObject.getDouble("bid value"), stock.getBidPrice());
            assertEquals(jsonObject.getDouble("percentage change"), stock.getPercentChange());
            assertEquals(jsonObject.getInt("shares purchased"), stock.getSharesPurchased());

            count++;
        }

    }

    private static class WellBehavedStock extends Stock {

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
