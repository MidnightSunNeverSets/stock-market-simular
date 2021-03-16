package persistence;

import model.Market;
import model.Portfolio;
import model.Stock;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    @Test
    void testWriteToInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0illegal_file");
            writer.open();
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriteStandardPortfolio() {
        Portfolio portfolio = new Portfolio();
        Market market = new Market();

        try {
            JsonWriter portfolioWriter = new JsonWriter("./data/testWriteStandardPortfolio");
            JsonWriter marketWriter = new JsonWriter("./data/testWriteStandardMarket");

            // writes portfolio to file
            portfolioWriter.open();
            portfolioWriter.write(portfolio);
            portfolioWriter.close();
            // writes market to file
            marketWriter.open();
            marketWriter.write(market);
            marketWriter.close();

            // reads market and portfolio from file
            JsonReader jsonReader = new JsonReader("./data/testWriteStandardPortfolio",
                    "./data/testWriteStandardMarket");
            market = jsonReader.readMarket(market);
            portfolio = jsonReader.readPortfolio(market);

            // checks that correct info was written
            assertTrue(market.getCatalogue().size() == 0);
            assertTrue(market.getCatalogueNames().size() == 0);
            assertTrue(portfolio.INITIAL_BALANCE == portfolio.getBalance());
            assertTrue(portfolio.getStocksOwned().size() == 0);
            assertTrue(portfolio.getStocksOwnedInfo().size() == 0);

        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }

    }

    @Test
    void testWriteUsedPortfolioAndMarket() {
        Portfolio portfolio = new Portfolio();
        Market market = new Market();
        Stock stock = new Stock("a");

        market.addStock(stock);
        market.purchaseShares("a", 2, portfolio);

        try {
            JsonWriter portfolioWriter = new JsonWriter("./data/testWriteAdjustedPortfolio");
            JsonWriter marketWriter = new JsonWriter("./data/testWriteAdjustedMarket");

            // writes portfolio to file
            portfolioWriter.open();
            portfolioWriter.write(portfolio);
            portfolioWriter.close();
            // writes market to file
            marketWriter.open();
            marketWriter.write(market);
            marketWriter.close();

            // reads market and portfolio from file
            JsonReader jsonReader = new JsonReader("./data/testWriteAdjustedPortfolio",
                    "./data/testWriteAdjustedMarket");
            market = jsonReader.readMarket(market);
            portfolio = jsonReader.readPortfolio(market);

            // checks that correct info was written
            Stock readStock = market.getCatalogue().get(0);
            assertTrue(market.getCatalogueNames().size() == 1);
            assertEquals("a", readStock.getName());
            assertTrue(stock.getCurrentValue() == readStock.getCurrentValue());
            assertTrue(stock.getAskPrice() == readStock.getAskPrice());
            assertTrue(stock.getBidPrice() == readStock.getBidPrice());
            assertTrue(stock.getPercentChange() == readStock.getPercentChange());
            assertTrue(stock.getSharesPurchased() == readStock.getSharesPurchased());

            assertTrue(market.getCatalogue().size() == 1);
            assertEquals("a", market.getCatalogueNames().get(0));
            assertTrue(portfolio.getBalance() == (portfolio.INITIAL_BALANCE - 2 * stock.getAskPrice()));

        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }

    }



}
