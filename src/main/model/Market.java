package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents the market from which stocks are purchased from, includes all the stocks
public class Market implements Writable {
    private final ArrayList<Stock> catalogue;
    protected ArrayList<String> catalogueNames;

    // REQUIRES: companyNames is non-empty
    // MODIFIES: this
    // EFFECTS: instantiates a stock for each company, and adds name of company
    public Market(String[] companyNames) {
        catalogue = new ArrayList<>();
        catalogueNames = new ArrayList<>();
        for (String s : companyNames) {
            catalogue.add(new Stock(s));
            catalogueNames.add(s);
        }
    }

    // REQUIRES: companyNames is empty
    // EFFECTS: creates an empty catalogue, sets catalogueNames
    public Market() {
        catalogue = new ArrayList<>();
        catalogueNames = new ArrayList<>();
    }


    // REQUIRES: company name is spelled correctly and a corresponding stock exists
    // EFFECTS: returns the stock given the company name
    public Stock lookUpStock(String name) {
        int index = catalogueNames.indexOf(name);
        return catalogue.get(index);
    }


    // REQUIRES: orderAmount > 0, companyName is spelled correctly
    // MODIFIES: this, portfolio
    // EFFECTS: determines the cost of the shares for purchase
    //          if user has enough balance
    //               - adds stock to portfolio
    //               - returns true
    //          else return false
    public boolean purchaseShares(String companyName, int orderAmount, Portfolio portfolio) {
        double cost = Math.round(100.0 * lookUpStock(companyName).getAskPrice() * orderAmount) / 100.0;

        if (cost <= portfolio.getBalance()) {
            lookUpStock(companyName).purchaseShares(orderAmount);
            portfolio.addOrSubtractFromBalance(-cost);
            portfolio.addStock(lookUpStock(companyName));
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: has the stock market go to the next day
    public void nextDay() {
        for (Stock s : catalogue) {
            s.nextDay();
        }
    }

    // REQUIRES: company name is spelt correctly
    // MODIFIES: this, portfolio
    // EFFECTS: if the stock is in the portfolio and there are sufficient shares owned:
    //               - sell the specified number of shares at the ask price
    //               - return true
    //          else return false
    public boolean sellShares(String name, int amount, Portfolio portfolio) {
        Stock stock = lookUpStock(name);
        if (portfolio.getStocksOwned().contains(stock) && stock.removeShares(amount)) {
            portfolio.addOrSubtractFromBalance(amount * stock.getAskPrice());

            // removes stock from portfolio when there are 0 shares owned
            if (stock.getSharesPurchased() == 0) {
                portfolio.removeStock(name);
            }

            return true;

        }
        return false;
    }


    // EFFECTS: See interface Writable
    // Citation: method code obtained and modified from JsonSerializationDemo
    //           https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        for (Stock s : catalogue) {
            jsonArr.put(s.toJson());
        }

        json.put("stock catalogue", jsonArr);
        return json;
    }

    // for testing

    // EFFECTS: returns all the stocks in the market
    public ArrayList<Stock> getCatalogue() {
        return catalogue;
    }

    // EFFECTS: returns names of the stocks in the market
    public ArrayList<String> getCatalogueNames() {
        return catalogueNames;
    }

    // MODIFIES: this
    // EFFECTS: adds a stock to the market
    public void addStock(Stock stock) {
        catalogue.add(stock);
        catalogueNames.add(stock.getName());
    }
}