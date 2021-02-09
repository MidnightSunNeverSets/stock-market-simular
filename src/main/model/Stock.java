package model;

import java.util.Random;

// Represents a company's stock with its name, bid price, ask price, current value and percentage change
public class Stock {
    protected double currentValue;
    protected double askPrice;
    protected double bidPrice;
    protected double percentChange;

    private String name;
    private int sharesPurchased;
    private Random rand = new Random();

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

    // MODIFIES: this
    // EFFECTS: sets new values for the stock value, ask price, bid price
    //          calculates the percentage change
    public void nextDay() {
        double previousValue = getCurrentValue();
        boolean askRises = rand.nextBoolean();
        boolean bidRises = rand.nextBoolean();
        double changeInAsk = rand.nextInt(3) + round(rand.nextDouble());
        double changeInBid = rand.nextInt(3) + round(rand.nextDouble());
        double spread; // the bid-ask spread

        if (askRises) {
            askPrice += changeInAsk;
        } else {
            askPrice -= changeInAsk;
        }

        spread = askPrice - bidPrice;

        if (bidRises) {
            // ensures that bid is never greater than ask
            while (changeInBid >= spread) {
                changeInBid = rand.nextInt(3) + round(rand.nextDouble());
            }
            bidPrice += changeInBid;
        } else {
            bidPrice -= changeInBid;
        }

        currentValue = round((askPrice + bidPrice) / 2);
        percentChange = round((currentValue - previousValue) / previousValue * 100);
    }

    // EFFECTS: rounds numbers to two decimal places
    private double round(double num) {
        return Math.round(num * 100.0) / 100.0;
    }


}
