package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    Portfolio portfolio;
    Stock s1 = new Stock("A");
    Stock s2 = new Stock("B");

    @BeforeEach
    public void setUp() {
        portfolio = new Portfolio();
    }

    @Test
    public void testAlternateConstructor() {
        ArrayList<String> stockInfo = new ArrayList<>();
        stockInfo.add("A");
        stockInfo.add("B");

        ArrayList<Stock> stocksOwned = new ArrayList<>();
        stocksOwned.add(s1);
        stocksOwned.add(s1);

        portfolio = new Portfolio(10, stockInfo, stocksOwned);

        assertTrue(10 == portfolio.getBalance());
        assertEquals(stockInfo, portfolio.getStocksOwnedInfo());
        assertEquals(stocksOwned, portfolio.getStocksOwned());
    }

    @Test
    public void testAddStock() {
        portfolio.addStock(s1);
        portfolio.addStock(s2);

        assertEquals(s1.getName() + ", " + s1.getSharesPurchased(), portfolio.getStocksOwnedInfo().get(0));
        assertEquals(s2.getName() + ", " + s1.getSharesPurchased(), portfolio.getStocksOwnedInfo().get(1));

        portfolio.addStock(s1);
        assertEquals(2, portfolio.getStocksOwnedInfo().size());
    }

    @Test
    public void addOrSubtractFromBalance() {
        portfolio.addOrSubtractFromBalance(-100);
        assertTrue(portfolio.getBalance() == portfolio.INITIAL_BALANCE - 100);

        portfolio.addOrSubtractFromBalance(300);
        assertTrue(portfolio.getBalance() == portfolio.INITIAL_BALANCE - 100 + 300);
    }

    @Test
    public void testToJson() {
        portfolio.addStock(s1);
        portfolio.addStock(s2);

        JSONObject json = portfolio.toJson();
        JSONArray stockInfoJsonArr = json.getJSONArray("info of stocks owned");
        JSONArray stocksOwnedJsonArr = json.getJSONArray("stocks owned");

        int count = 0; // counter for the arrays of the portfolio

        for (Object jo: stockInfoJsonArr) {
            assertEquals(portfolio.getStocksOwnedInfo().get(count), (String) jo);
            count++;
        }

        count = 0;

        for (Object jo: stocksOwnedJsonArr) {
            JSONObject jsonObject = (JSONObject) jo;
            assertEquals(portfolio.getStocksOwned().get(count).getName(), jsonObject.getString("name"));
            count++;
        }


        assertTrue(portfolio.INITIAL_BALANCE == json.getDouble("account balance"));
    }
}
