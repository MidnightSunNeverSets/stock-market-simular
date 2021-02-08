package ui;

import model.Market;
import model.Portfolio;
import model.Stock;

import java.util.Scanner;

public class Main {

    static final String[] COMPANIES = {"Shibe Inc.", "Papaya", "Tweety", "GuCCe", "MIYO"};

    static Scanner input;

    public static void main(String[] args) {

        input = new Scanner(System.in);

        Market stockMarket = new Market(COMPANIES);
        Portfolio portfolio = new Portfolio();

        System.out.println("The stocks available for trade are:");
        for (String s : COMPANIES) {
            System.out.println("   - " + s);
        }

        System.out.println();

        int response = commands();


        System.out.println();
        doCommand(response, stockMarket, portfolio);
    }

    public static int commands() {
        System.out.println("Here are some things you can do: ");
        System.out.println("   1. Look up the stock details of a company.");
        System.out.println("   2. Check stock details of all the companies");
        System.out.println("   3. Check your balance.");
        System.out.println("   4. Purchase shares.");
        System.out.println("   5. Sell shares.");
        System.out.println("   6. Check your portfolio.");
        System.out.println("   7. Go to tomorrow.");
        System.out.print("What would you like to do? (Enter a command number): ");

        return input.nextInt();

    }

    public static void doCommand(int command, Market stockMarket, Portfolio portfolio) {
        input = new Scanner(System.in);

        if (command == 1) {
            System.out.print("Enter the company name: ");
            String response = input.nextLine();
            System.out.println();

            printStockDetails(response, stockMarket);
        } else if (command == 2) {
            printAllStockDetails(stockMarket);
        } else if (command == 3) {
            System.out.printf("Balance: $%.2f", portfolio.getBalance());
        } else if (command == 4) {
            purchaseShares(stockMarket, portfolio);
        } else if (command == 5) {
            System.out.println("sold");
        } else if (command == 6) {
            System.out.println(portfolio.getStocksOwned());
        } else if (command == 7) {
            stockMarket.nextDay();
        }
    }

    // EFFECTS: prints out stock details in the format:
    //          'company name'
    //          'current value'
    //          'bid price'
    //          'ask price'
    //          'spread'
    //          'change percentage'
    public static void printStockDetails(String name, Market market) {
        Stock stock = market.lookUpStock(name);

        System.out.println(name);
        System.out.printf("   Current Value: $%.2f\n", stock.getCurrentValue());
        System.out.printf("   Bid: $%.2f\n", stock.getBidPrice());
        System.out.printf("   Ask: $%.2f\n", stock.getAskPrice());
        System.out.printf("   Spread: $%.2f\n", stock.getAskPrice() - stock.getBidPrice());
        System.out.printf("   Change Percentage: %.2f%%\n", stock.getPercentChange());
    }

    // EFFECTS: prints out details of all stocks
    public static void printAllStockDetails(Market market) {
        for (String s: COMPANIES) {
            printStockDetails(s, market);
            System.out.println();
        }
    }

    // REQUIRES: orderAmount > 0, companyName is spelled correctly, market and portfolio are not null
    // MODIFIES: Stock, Portfolio
    // EFFECTS:
    public static void purchaseShares(Market market, Portfolio portfolio) {
        input = new Scanner(System.in);

        System.out.print("What company would you like to purchase shares from?: ");
        String companyNameResponse = input.nextLine();
        System.out.print("How many shares would you like to purchase? ");
        int sharesResponse = input.nextInt();

        if (market.purchaseShares(companyNameResponse, sharesResponse, portfolio)) {
            System.out.printf("Successfully purchased %d shares from %s\n", sharesResponse, companyNameResponse);
        } else {
            System.out.println("Purchase was unsuccessful.");
        }

    }

}
