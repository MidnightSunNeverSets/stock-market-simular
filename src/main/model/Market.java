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

    // MODIFIES: this, Stock
    // EFFECTS: has the stock market go to the next day
    public void nextDay() {
        for (Stock s: catalogue) {
            s.nextDay();
        }
    }
    
    // REQUIRES: company name is spelt correctly
    // MODIFIES: this, Portfolio, Stock
    // EFFECTS: if the stock is in the portfolio and there are sufficient shares owned:
    //               - sell the specified number of shares at the ask price
    //               - return receipt confirming transaction and number of shares sold
    //          else if stock is in the portfolio but the amount desiring to be sold exceeds the shares owned:
    //               - return notice that they are selling more than they own
    //          otherwise return statement that holder does not own shares in the specified stock
    public String sellShares(String name, int amount, Portfolio portfolio) {
        Stock stock = lookUpStock(name);
        if (portfolio.getStocksOwned().contains(stock)) {
            if (stock.removeShares(amount)) {
                portfolio.addOrSubtractFromBalance(amount * stock.getAskPrice());
                stock.removeShares(amount);
                return Integer.toString(amount) + " shares from " + name + " have been sold.";
            } else {
                return "Are you sure you entered the correct amount? You currently don't own "
                        + Integer.toString(amount) + " shares in " + name + ".";
            }
        }
        return "Are you sure you entered the correct company? You currently don't own any shares in " + name + ".";
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
