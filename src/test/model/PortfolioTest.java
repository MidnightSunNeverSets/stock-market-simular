package model;

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
    public void testAddStock() {
        portfolio.addStock(s1);
        portfolio.addStock(s2);

        assertEquals(s1.getName() + ", " + s1.getSharesPurchased(), portfolio.getStocksOwned().get(0));
        assertEquals(s2.getName() + ", " + s1.getSharesPurchased(), portfolio.getStocksOwned().get(1));

        portfolio.addStock(s1);
        assertEquals(2, portfolio.getStocksOwned().size());
    }

    @Test
    public void addOrSubtractFromBalance() {
        portfolio.addOrSubtractFromBalance(-100);
        assertTrue(portfolio.getBalance() == portfolio.INITIAL_BALANCE - 100);

        portfolio.addOrSubtractFromBalance(300);
        assertTrue(portfolio.getBalance() == portfolio.INITIAL_BALANCE - 100 + 300);
    }
}
