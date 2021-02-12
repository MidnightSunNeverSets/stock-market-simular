package ui;

import model.Market;
import model.Portfolio;
import model.Stock;

import java.util.Scanner;

// Stock market simulator application
public class StockMarketSimulator {

    private final String[] companies = {"Shibe Inc.", "Papaya", "Tweety", "GuCCe", "MIYO"};
    private Scanner input;
    private Market stockMarket;
    private Portfolio portfolio;
    private boolean keepPlaying;

    // EFFECTS: runs the simulator
    public StockMarketSimulator() {
        input = new Scanner(System.in);
        stockMarket = new Market(companies);
        portfolio = new Portfolio();
        runSimulator();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runSimulator() {
        int command;
        keepPlaying = true;

        System.out.println("The stocks available for trade are:");
        for (String s : companies) {
            System.out.println("   - " + s);
        }
        System.out.println();
        while (keepPlaying) {
            command = getCommands();
            input.nextLine();

            System.out.println();
            processCommand(command);

        }
    }

    // EFFECTS: returns user input of what they would like to do with the simulator
    private int getCommands() {
        System.out.println("Here are some things you can do: ");
        System.out.println("   1. Look up the stock details of a company.");
        System.out.println("   2. Check stock details of all the companies");
        System.out.println("   3. Check your balance.");
        System.out.println("   4. Purchase shares.");
        System.out.println("   5. Sell shares.");
        System.out.println("   6. Check your portfolio.");
        System.out.println("   7. Go to tomorrow.");
        System.out.println("   8. Quit.");
        System.out.print("What would you like to do? (Enter a command number): ");

        return input.nextInt();
    }

    // REQUIRES: 1=< command <= 8
    // MODIFIES: Stock, Market, Portfolio
    // EFFECTS: processes the user command
    private void processCommand(int command) {
        if (command == 1) {
            System.out.print("Enter the company name: ");
            printStockDetails(input.nextLine());
            System.out.println();
        } else if (command == 2) {
            printAllStockDetails();
        } else if (command == 3) {
            System.out.printf("Balance: $%.2f\n\n", portfolio.getBalance());
        } else if (command == 4) {
            purchaseShares();
        } else if (command == 5) {
            sellShares();
        } else if (command == 6) {
            System.out.println(portfolio.getStocksOwnedInfo());
        } else if (command == 7) {
            stockMarket.nextDay();
            System.out.println("Ready to start a new day?");
        } else if (command == 8) {
            System.out.println("Thanks for playing! :)");
            keepPlaying = false;
        } else {
            System.out.println("The command you entered doesn't exist. Please enter another one.");
        }

    }

    // EFFECTS: prints out stock details in the format:
    //          'company name'
    //          'current value'
    //          'bid price'
    //          'ask price'
    //          'spread'
    //          'change percentage'
    private void printStockDetails(String name) {
        Stock stock = stockMarket.lookUpStock(name);

        System.out.println(name);
        System.out.printf("   Current Value: $%.2f\n", stock.getCurrentValue());
        System.out.printf("   Bid: $%.2f\n", stock.getBidPrice());
        System.out.printf("   Ask: $%.2f\n", stock.getAskPrice());
        System.out.printf("   Spread: $%.2f\n", stock.getAskPrice() - stock.getBidPrice());
        System.out.printf("   Change Percentage: %.2f%%\n", stock.getPercentChange());
    }

    // EFFECTS: prints stock details of all companies
    private void printAllStockDetails() {
        for (String s: companies) {
            printStockDetails(s);
        }
        System.out.println();
    }

    // REQUIRES: orderAmount > 0, companyName is spelled correctly, market and portfolio are not null
    // MODIFIES: this, Market, Stock, Portfolio
    // EFFECTS:
    private void purchaseShares() {
        System.out.print("What company would you like to purchase shares from?: ");
        String companyNameResponse = input.nextLine();
        System.out.print("How many shares would you like to purchase? ");
        int sharesResponse = input.nextInt();

        input.nextLine();

        if (stockMarket.purchaseShares(companyNameResponse, sharesResponse, portfolio)) {
            System.out.printf("Successfully purchased %d shares from %s\n", sharesResponse, companyNameResponse);
        } else {
            System.out.println("Purchase unsuccessful. Insufficient funds. Check your account balance.");
        }
        System.out.println();
    }

    // REQUIRES: orderAmount > 0, companyName is spelled correctly, market and portfolio are not null
    // MODIFIES: this, Market, Stock, Portfolio
    // EFFECTS: sells shares at the ask price
    private void sellShares() {
        System.out.print("Which stock would you like to sell? (Enter name of company): ");
        String companyNameResponse = input.nextLine();
        System.out.print("How many shares would you like to sell?: ");
        int sharesResponse = input.nextInt();

        input.nextLine();

        System.out.println(stockMarket.sellShares(companyNameResponse, sharesResponse, portfolio));
        System.out.println();
    }

}
