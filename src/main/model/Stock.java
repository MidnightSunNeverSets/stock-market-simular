package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Random;

// Represents a company's stock with its name, bid price, ask price, current value and percentage change
public class Stock implements Writable {
    protected double currentValue;
    protected double askPrice;
    protected double bidPrice;
    protected double percentChange;

    private final String name;
    private final Random rand = new Random();

    private int sharesPurchased;

    // EFFECTS: stock name set to name of company
    //          sets the current value of the stock
    //          sets the sell price of the stock
    //          sets the buy price of the stock
    public Stock(String name) {
        this.name = name;
        askPrice = round(rand.nextDouble() + rand.nextInt(1000));
        bidPrice = round(askPrice - rand.nextInt((int) askPrice) - rand.nextDouble());
        currentValue = round((bidPrice + askPrice) / 2);
        percentChange = 0.0;
        sharesPurchased = 0;
    }

    // EFFECTS: takes in the company name, ask price, bid price, current value, percent change and shares purchased,
    //          and sets the stock to match the values
    public Stock(String name, double askPrice, double bidPrice, double currentValue, double percentChange,
                 int sharesPurchased) {
        this.name = name;
        this.askPrice = askPrice;
        this.bidPrice = bidPrice;
        this.currentValue = currentValue;
        this.percentChange = percentChange;
        this.sharesPurchased = sharesPurchased;
    }

    // getters
    public String getName() {
        return name;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public int getSharesPurchased() {
        return sharesPurchased;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: amount added to the number of purchased shares
    public void purchaseShares(int amount) {
        sharesPurchased += amount;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: if there are sufficient shares
    //            - subtract amount of shares from the number of purchased shares
    //            - return true
    //          otherwise return false
    public boolean removeShares(int amount) {
        if (sharesPurchased >= amount) {
            sharesPurchased -= amount;
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: sets new values for the stock value, ask price, bid price
    //          calculates the percentage change
    public void nextDay() {
        double previousValue = getCurrentValue();
        boolean askRises = rand.nextBoolean();
        boolean bidRises = rand.nextBoolean();
        double changeInAsk = rand.nextInt(3) + round(rand.nextDouble());
        double changeInBid = rand.nextInt(3) + round(rand.nextDouble());

        // generates ask price
        generateAsk(askRises, changeInAsk);

        // generates bid price
        generateBid(bidRises, changeInBid);

        currentValue = round((askPrice + bidPrice) / 2);
        percentChange = round((currentValue - previousValue) / previousValue * 100);
    }

    // for testing

    // REQUIRES: changeInBid is > 0
    // MODIFIES: this
    // EFFECTS: randomly generates a new bid price that is always below the ask price
    protected void generateBid(boolean bidRises, double changeInBid) {
        double spread = askPrice - bidPrice;

        if (bidRises) {
            // ensures that bid is never greater than ask
            while (changeInBid >= spread) {
                changeInBid = rand.nextInt(3) + round(rand.nextDouble());
            }
            bidPrice += changeInBid;
        } else {
            bidPrice -= changeInBid;
        }
    }

    // EFFECTS: see interface writable
    // Citation: method code obtained and modified from JsonSerializationDemo
    //           https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("company", name);
        json.put("current value", currentValue);
        json.put("ask value", askPrice);
        json.put("bid value", bidPrice);
        json.put("percentage change", percentChange);
        json.put("shares purchased", sharesPurchased);

        return json;
    }

    // REQUIRES: changeInAsk > 0
    // MODIFIES: this
    // EFFECTS: randomly generates a new ask price
    protected void generateAsk(boolean askRises, double changeInAsk) {
        if (askRises) {
            askPrice += changeInAsk;
        } else {
            askPrice -= changeInAsk;
        }
    }


    // EFFECTS: rounds numbers to two decimal places
    private double round(double num) {
        return Math.round(num * 100.0) / 100.0;
    }

}
