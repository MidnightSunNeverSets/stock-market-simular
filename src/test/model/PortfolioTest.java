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
        ArrayList<Stock> stocksOwned = new ArrayList<>();
        stocksOwned.add(s1);
        stocksOwned.add(s1);

        portfolio = new Portfolio(10, stocksOwned);

        assertEquals(portfolio.getBalance(), 10);
        assertEquals(stocksOwned, portfolio.getStocksOwned());
    }

    @Test
    public void testAddStock() {
        portfolio.addStock(s1);
        portfolio.addStock(s2);

        assertEquals(portfolio.getStocksOwned().get(0), s1);
        assertEquals(portfolio.getStocksOwned().get(1), s2);

        portfolio.addStock(s1);
        assertEquals(portfolio.getStocksOwned().size(), 2);
        assertEquals(portfolio.getStocksOwned().get(0), s1);
        assertEquals(portfolio.getStocksOwned().get(1), s2);

    }

    @Test
    public void testAddOrSubtractFromBalance() {
        portfolio.addOrSubtractFromBalance(-100);
        assertEquals(Portfolio.INITIAL_BALANCE - 100, portfolio.getBalance());

        portfolio.addOrSubtractFromBalance(300);
        assertEquals(Portfolio.INITIAL_BALANCE - 100 + 300, portfolio.getBalance());
    }

    @Test
    public void testRemoveStock() {

    }

    @Test
    public void testToJson() {
        portfolio.addStock(s1);
        portfolio.addStock(s2);

        JSONObject json = portfolio.toJson();
        JSONArray stocksOwnedJsonArr = json.getJSONArray("stocks owned");

        int count = 0; // counter for the arrays of the portfolio

        for (Object jo: stocksOwnedJsonArr) {
            JSONObject jsonObject = (JSONObject) jo;
            assertEquals(portfolio.getStocksOwned().get(count).getName(), jsonObject.getString("name"));
            count++;
        }


        assertEquals(json.getDouble("account balance"), Portfolio.INITIAL_BALANCE);
    }
}
