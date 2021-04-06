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
            market = jsonReader.readMarket();
            portfolio = jsonReader.readPortfolio(market);

            // checks that correct info was written
            assertEquals(market.getCatalogue().size(), 0);
            assertEquals(market.getCatalogueNames().size(), 0);
            assertEquals(portfolio.getBalance(), Portfolio.INITIAL_BALANCE);
            assertEquals(portfolio.getStocksOwned().size(), 0);

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
            market = jsonReader.readMarket();
            portfolio = jsonReader.readPortfolio(market);

            // checks that correct info was written
            Stock readStock = market.getCatalogue().get(0);
            assertEquals(market.getCatalogueNames().size(), 1);
            assertEquals("a", readStock.getName());
            assertEquals(readStock.getCurrentValue(), stock.getCurrentValue());
            assertEquals(readStock.getAskPrice(), stock.getAskPrice());
            assertEquals(readStock.getBidPrice(), stock.getBidPrice());
            assertEquals(readStock.getPercentChange(), stock.getPercentChange());
            assertEquals(readStock.getSharesPurchased(), stock.getSharesPurchased());

            assertEquals(market.getCatalogue().size(), 1);
            assertEquals("a", market.getCatalogueNames().get(0));
            assertEquals((Portfolio.INITIAL_BALANCE - 2 * stock.getAskPrice()), portfolio.getBalance());

        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }

    }



}
