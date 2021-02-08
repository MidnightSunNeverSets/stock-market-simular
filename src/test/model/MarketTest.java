package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

}
