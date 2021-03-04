package persistence;

import model.Market;
import model.Portfolio;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    String[] lst = {"lol", "wut"};


    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noPortfolioFile", "./data/noMarketFile");
        Market market = new Market(lst);
        try {
            Portfolio portfolio = reader.readPortfolio(market);
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // pass
        }

    }
}
