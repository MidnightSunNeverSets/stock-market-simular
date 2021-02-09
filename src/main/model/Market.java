package model;

import java.util.ArrayList;

// represents the market from which stocks are purchased from, includes all the stocks
public class Market {

    private ArrayList<Stock> catalogue;
    private ArrayList<String> catalogueNames;

    // REQUIRES: companyNames is non-empty
    // MODIFIES: this
    // EFFECTS: instantiates a stock for each company, and adds name of company
    public Market(String[] companyNames) {
        catalogue = new ArrayList<>();
        catalogueNames = new ArrayList<>();
        for (String s: companyNames) {
            catalogue.add(new Stock(s));
            catalogueNames.add(s);
        }
    }


    // REQUIRES: company name is spelled correctly and a corresponding stock exists
    // EFFECTS: returns the stock given the company name
    public Stock lookUpStock(String name) {
        int index = catalogueNames.indexOf(name);
        return catalogue.get(index);
    }


    // REQUIRES: orderAmount > 0, companyName is spelled correctly
    // MODIFIES: Stock, Portfolio
    // EFFECTS:
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

    // MODIFIES: this, STOCK
    // EFFECTS: has the stock market go to the next day
    public void nextDay() {
        for (Stock s: catalogue) {
            s.nextDay();
        }
    }

    // for testing

    // EFFECTS: returns all the stocks in the market
    public ArrayList<Stock> getCatalogue() {
        return catalogue;
    }


    // MODIFIES: this
    // EFFECTS: adds a stock to the market
    public void addStock(Stock stock) {
        catalogue.add(stock);
        catalogueNames.add(stock.getName());
    }
}
