package model;

import java.util.ArrayList;

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

    // REQUIRES: company name is spelled correctly
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

    public void nextDay() {
        for (Stock s: catalogue) {
            s.nextDay();
        }
    }

//    // EFFECTS: prints out the specified stock's details
//    public void printStockDetails(String name) {
//        getStock(name).printStockDetails();
//    }

}
