package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents the stocks the user has purchased and their money
public class Portfolio implements Writable {

    public static final double INITIAL_BALANCE = 100000;

    private ArrayList<String> stockInfo; // stores the info of the stock owned
    private ArrayList<Stock> stocksOwned; // stores the stock owned
    private double balance;

    // EFFECTS: instantiates stockInfo and stocksOwned; sets initial balance
    public Portfolio() {
        stockInfo = new ArrayList<>();
        stocksOwned = new ArrayList<>();
        balance = INITIAL_BALANCE;
    }

    public Portfolio(double balance, ArrayList<String> stockInfo, ArrayList<Stock> stocksOwned) {
        this.balance = balance;
        this.stockInfo = stockInfo;
        this.stocksOwned = stocksOwned;
    }

    // getters
    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getStocksOwnedInfo() {
        return stockInfo;
    }

    public ArrayList<Stock> getStocksOwned() {
        return stocksOwned;
    }

    // setters
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // REQUIRES: amount != 0; if amount < 0, amount <= balance
    // MODIFIES: this
    // EFFECTS: if amount > 0, add to balance
    //          if amount < 0, subtract from balance
    public void addOrSubtractFromBalance(double amount) {
        balance += amount;
    }

    // MODIFIES: this
    // EFFECTS: if stock not in portfolio, adds new entry
    //          if stock already in, updates the shares information
    //          entry in format: Company Name Number of Shares
    public void addStock(Stock stock) {
        if (stocksOwned.contains(stock)) {
            int index = stocksOwned.indexOf(stock);
            stockInfo.set(index, stock.getName() + ", " + stock.getSharesPurchased());
        } else {
            stockInfo.add(stock.getName() + ", " + stock.getSharesPurchased());
            stocksOwned.add(stock);
        }
    }

    // EFFECTS: returns and converts stockInfo to a JSONArray
    private JSONArray stockInfoToJson() {
        JSONArray jsonArr = new JSONArray();

        for (String s: stockInfo) {
            jsonArr.put(s);
        }
        return jsonArr;
    }

    // EFFECTS: returns and converts stocksOwned to a JSONArray
    private JSONArray stocksOwnedToJson() {
        JSONArray jsonArr = new JSONArray();
        for (Stock s: stocksOwned) {
            jsonArr.put(s.getName());
        }
        return jsonArr;
    }

    // EFFECTS: adds stocksOwned, stockInfo and balance to a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("account balance", balance);
        json.put("stocks owned", stocksOwnedToJson());
        json.put("info of stocks owned", stockInfoToJson());

        return json;
    }
}
