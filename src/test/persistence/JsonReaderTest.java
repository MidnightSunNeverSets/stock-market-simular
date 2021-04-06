package persistence;

import model.Market;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    String[] lst = {"lol", "wut"};
    JsonReader reader;

    @Test
    void testReaderNonExistentFile() {
        // when both portfolio and market files are non-existent
        reader = new JsonReader("./data/noPortfolioFile", "./data/noMarketFile");
        Market market = new Market(lst);
        try {
            reader.readPortfolio(market);
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // pass
        }
    }

}
