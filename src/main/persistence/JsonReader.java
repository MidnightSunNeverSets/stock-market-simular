package persistence;

import model.Market;
import model.Portfolio;
import model.Stock;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader that reads portfolio and market from file
// Citation: code obtained and modified from JsonSerializationDemo
//           https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {

    private String portfolioSource;
    private String marketSource;

    public JsonReader(String portfolioSource, String marketSource) {
        this.portfolioSource = portfolioSource;
        this.marketSource = marketSource;
    }

    // EFFECTS: reads workroom from file and returns it; throws IOException if there is an error
    //          when reading from file
    public Portfolio readPortfolio(Market market) throws IOException {
        String jsonData = readFile(portfolioSource);
        JSONObject json = new JSONObject(jsonData);
        return parsePortfolio(market, json);
    }

    // EFFECTS: reads market from file and returns it; throws IOException if there is an error when
    //          reading from file
    public Market readMarket(Market market) throws IOException {
        String jsonData = readFile(marketSource);
        JSONObject json = new JSONObject(jsonData);

        return parseMarket(json);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // MODIFIES: market
    // EFFECTS: parses the info of stocks stored in JSONObject and adds them to the market
    private Market parseMarket(JSONObject jsonObject) {
        Market market = new Market();
        JSONArray jsonArr = jsonObject.getJSONArray("stock catalogue");
        for (Object json: jsonArr) {
            addStock(market, (JSONObject) json);
        }
        return market;
    }

    // MODIFIES: market, stock
    // EFFECTS: parses stock info from JSON object into stock and adds it into market
    private void addStock(Market market, JSONObject jsonObject) {
        String name = jsonObject.getString("company");
        double percentageChange = Math.round(jsonObject.getDouble("percentage change") * 100.0) / 100.0;
        double currentValue =  Math.round(jsonObject.getDouble("current value") * 100.0) / 100.0;
        double bidValue = Math.round(jsonObject.getDouble("bid value") * 100.0) / 100.0;
        double askValue = Math.round(jsonObject.getDouble("ask value") * 100.0) / 100.0;
        int sharesPurchased = jsonObject.getInt("shares purchased");

        // adds stock to the catalogue
        market.addStock(new Stock(name, askValue, bidValue, currentValue, percentageChange, sharesPurchased));
    }

    // MODIFIES: portfolio, market, stock
    // EFFECTS: parses portfolio from JSONObject and returns it
    private Portfolio parsePortfolio(Market market, JSONObject jsonObject) {
        JSONArray stockInfoJsonArr = jsonObject.getJSONArray("info of stocks owned");
        JSONArray stocksOwnedJsonArr = jsonObject.getJSONArray("stocks owned");

        double balance = Math.round(jsonObject.getDouble("account balance") * 100.0) / 100.0;
        ArrayList<String> stockInfo = new ArrayList<>();
        ArrayList<Stock> stocksOwned = new ArrayList<>();

        // parses to stock info
        for (Object json: stockInfoJsonArr) {
            String info = (String) json;
            stockInfo.add(info);
        }

        // parses to stocks owned, objects in stocksOwnedJsonArr already stored as String
        for (Object jo: stocksOwnedJsonArr) {
            JSONObject json = (JSONObject) jo;
            stocksOwned.add(market.lookUpStock(((JSONObject) jo).getString("name")));
        }

        return new Portfolio(balance, stockInfo, stocksOwned);
    }


}
