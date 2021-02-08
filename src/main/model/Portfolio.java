package model;

import java.util.ArrayList;

// Represents the stocks the user has purchased
public class Portfolio {

    public static final double INITIAL_BALANCE = 100000;

    private ArrayList<String> stockInfo; // stores the info of the stock owned
    private ArrayList<Stock> stocksOwned; // stores the stock owned
    private double balance;

    public Portfolio() {
        stockInfo = new ArrayList<>();
        stocksOwned = new ArrayList<>();
        balance = INITIAL_BALANCE;
    }

    // getters
    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getStocksOwned() {
        return stockInfo;
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

//    // EFFECTS: prints stocks owned in format:
//    //          'Company Name, Shares Owned'
//    public String printStocksOwned() {
//        if (stocks.isEmpty()) {
//            System.out.println("Your portfolio is currently empty.");
//        } else {
//            for (Stock s : stocks) {
//                System.out.printf("%s, %d Shares\n", s.getName(), s.getSharesPurchased());
//            }
//        }
//    }

//    public void printDetailsOfStocksOwned() {
//        for (Stock s : stocks) {
//            s.printStockDetails();
//            System.out.println(s.getSharesPurchased());
//            System.out.println();
//        }
//    }

}
