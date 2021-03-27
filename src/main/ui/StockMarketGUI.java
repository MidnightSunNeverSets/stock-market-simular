package ui;

import model.Market;
import model.Portfolio;
import model.Stock;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// GUI interface for Stock Market Simulator
public class StockMarketGUI implements ActionListener {

    // GUI COMPONENTS
    private static final String[] COMPANIES = {"Shibe Inc.", "Papaya", "Tweety", "panDASH", "MIYO", "Yoko"};
    private static final String BACKGROUND_IMAGE = "./data/bouncy cat.gif";
    private static final Map<String, ImageIcon> IMAGES = createImageMap();

    private JFrame frame;

    private JPanel introPanel;
    private JPanel stockListingsPanel;
    private JPanel commandsPanel;
    private JPanel mainPanel;
    private JPanel purchaseAndSellPanel;
    private JPanel stockDetailPanel;
    private JPanel allStockDetailsPanel;
    private JPanel balancePanel;
    private JPanel portfolioPanel;
    private JPanel backPanel;

    private JLabel background;
    private JLabel companyImage;

    private JTextField inputField;

    private JComboBox companiesBox;

//    private JButton inputBtn;

    // SIMULATOR FIELDS
    private static final String JSON_PORTFOLIO = "./data/portfolio.json";
    private static final String JSON_MARKET = "./data/market.json";

    private final JsonWriter portfolioJsonWriter;
    private final JsonWriter marketJsonWriter;
    private final JsonReader jsonReader;

    private Market stockMarket;
    private Portfolio portfolio;
    private boolean keepPlaying;


    // EFFECTS: sets up an interface
    public StockMarketGUI() {
        super();
        stockMarket = new Market(COMPANIES);
        portfolio = new Portfolio();
        portfolioJsonWriter = new JsonWriter(JSON_PORTFOLIO);
        marketJsonWriter = new JsonWriter(JSON_MARKET);
        jsonReader = new JsonReader(JSON_PORTFOLIO, JSON_MARKET);

        backPanel = new JPanel();
        JButton backBtn = new JButton("BACK");
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(mainPanel, true);
            }
        });
        backPanel.add(backBtn);

        setUpFrame();
        createIntroPanel();
        createMainPanel();

        frame.add(introPanel);
        frame.pack();
    }

    // EFFECTS: adds images photos used to a Map and returns it
    private static Map<String, ImageIcon> createImageMap() {
        Map<String, ImageIcon> imageMap = new HashMap<>();
        imageMap.put(COMPANIES[0], new ImageIcon("./data/shibe.png"));
        imageMap.put(COMPANIES[1], new ImageIcon("./data/papaya.png"));
        imageMap.put(COMPANIES[2], new ImageIcon("./data/seagull.png"));
        imageMap.put(COMPANIES[3], new ImageIcon("./data/raccoonface.png"));
        imageMap.put(COMPANIES[4], new ImageIcon("./data/cat.png"));
        imageMap.put(COMPANIES[5], new ImageIcon("./data/yogurt.png"));

        return imageMap;
    }

    // MODIFIES: this
    // EFFECTS: sets up the frame
    private void setUpFrame() {
        frame = new JFrame("Stock Market Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: switches the current panel to the given new one
    private void switchPanel(JPanel newPanel, boolean isMain) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(newPanel);

        if (!isMain) {
            frame.getContentPane().add(backPanel, BorderLayout.SOUTH);
        }

        frame.repaint();
        frame.pack();
    }

    // MODIFIES: this
    // EFFECTS: loads market from file
    // Citation: method code obtained and modified from JsonSerializationDemo
    //           https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void loadMarket() {
        try {
            stockMarket = jsonReader.readMarket(stockMarket);
            System.out.println("Successfully loaded previous stock market data.");
        } catch (IOException e) {
            System.out.println("Unable to load stock market data.");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads porfolio from file
    // Citation: method code obtained and modified from JsonSerializationDemo
    //           https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void loadPortfolio() {
        try {
            portfolio = jsonReader.readPortfolio(stockMarket);
            System.out.println("Successfully loaded previous portfolio data.\n");
        } catch (IOException e) {
            System.out.println("Unable to load previous portfolio data.");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates starting panel, with options to continue or start a new game
    private void createIntroPanel() {
        introPanel = new JPanel();
        introPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        introPanel.setPreferredSize(new Dimension(180, 100));

        JButton continueGameBtn = new JButton("Continue Game"); // TODO: add action listener
        continueGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMarket();
                loadPortfolio();
                switchPanel(mainPanel, true);
                System.out.println("Market and Portfolio successfully loaded.");
            }
        });
        JButton newGameBtn = new JButton("New Game"); // TODO: add action listener
        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(mainPanel, true);
            }
        });
        introPanel.add(continueGameBtn);
        introPanel.add(newGameBtn);
    }

    // MODIFIES: this
    // EFFECTS: creates panel showcasing the available stocks for purchase
    private void createStockListingsPanel() {
        stockListingsPanel = new JPanel();
        JButton[] stocksBtns = new JButton[COMPANIES.length];
        JLabel listings = new JLabel("Current Stock Listings: ");

        stockListingsPanel.add(listings);

        for (int i = 0; i < COMPANIES.length; i++) {
            stocksBtns[i] = new JButton(COMPANIES[i]);
            stocksBtns[i].addActionListener(this);
            stocksBtns[i].setName("stock listing button");
            stockListingsPanel.add(stocksBtns[i]);
        }

    }

    // MODIFIES: this
    // EFFECTS: creates panel containing all the possible user commands
    private void createCommandsPanel() {
        commandsPanel = new JPanel();

        commandsPanel.setLayout(new BoxLayout(commandsPanel, BoxLayout.Y_AXIS));
//        commandsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        commandsPanel.setPreferredSize(new Dimension(200, 240));

        JButton[] commandBtns = new JButton[7];

        commandBtns[0] = new JButton("SEE ALL STOCK DETAILS");
        commandBtns[1] = new JButton("SEE BALANCE");
        commandBtns[2] = new JButton("PURCHASE SHARES");
        commandBtns[3] = new JButton("SELL SHARES");
        commandBtns[4] = new JButton("SEE PORTFOLIO");
        commandBtns[5] = new JButton("START A NEW DAY");
        commandBtns[6] = new JButton("QUIT GAME");

        commandsPanel.add(Box.createVerticalStrut(10));
        for (JButton btn : commandBtns) {
            btn.setName("commands button");
            btn.addActionListener(this);
            commandsPanel.add(btn);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            commandsPanel.add(Box.createVerticalStrut(5));
        }
    }

    // MODIFIES: this
    // EFFECTS: creates main panel that showcases all the user commands
    private void createMainPanel() {
        createCommandsPanel();
        createStockListingsPanel();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(stockListingsPanel);
        mainPanel.add(commandsPanel);
    }

    // MODIFIES: this
    // EFFECTS: if purchase is true, creates panel for purchasing stock
    //          else creates panel for selling stock
    private void createPurchaseOrSellPanel(boolean purchase) {
        purchaseAndSellPanel = new JPanel();
        purchaseAndSellPanel.setLayout(new BoxLayout(purchaseAndSellPanel, BoxLayout.Y_AXIS));
        companyImage = new JLabel(new ImageIcon("./data/shibe.png"));
        companyImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        companyImage = new JLabel(IMAGES.get("Shibe Inc."));
        companyImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        purchaseAndSellPanel.add(createPurchaseAndSellInputPanel(purchase));
        purchaseAndSellPanel.add(companyImage);
    }

    // MODIFIES: this
    // EFFECTS: creates the input textfield, label and button for purchasing shares
    private JPanel createPurchaseAndSellInputPanel(boolean purchase) {
        JPanel inputPanel = new JPanel();
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        companiesBox = new JComboBox(COMPANIES);
        companiesBox.addActionListener(this);

        inputField = new JTextField(3);
        inputField.addActionListener(this);

        JLabel stockNumQuestion = new JLabel();
        JButton inputBtn = new JButton();
        inputBtn.setName("input button");
        inputBtn.addActionListener(this);

        if (purchase) {
            stockNumQuestion.setText("Number of Shares to Purchase: ");
            inputBtn.setText("BUY");
        } else {
            stockNumQuestion.setText("Number of Shares to Sell: ");
            inputBtn.setText("SELL");
        }

        inputPanel.add(companiesBox);
        inputPanel.add(stockNumQuestion);
        inputPanel.add(inputField);
        inputPanel.add(inputBtn);

        return inputPanel;
    }

    // MODIFIES: this
    // EFFECTS: creates panel that showcases details of all the stocks
    private void createAllStockDetailsPanel() {
        allStockDetailsPanel = new JPanel();
        GridLayout stockDetailsLayout = new GridLayout(3, 2);
        stockDetailsLayout.setHgap(50);
        stockDetailsLayout.setVgap(10);
        allStockDetailsPanel.setLayout(stockDetailsLayout);

        for (String s : COMPANIES) {
            JLabel stockDetails = new JLabel(stockToString(s));
            stockDetails.setFont(new Font("", Font.PLAIN, 20));
            allStockDetailsPanel.add(stockDetails);
        }
    }


    // MODIFIES: this
    // EFFECTS: creates JPanel showcasing the stock details of a company
    private void createStockDetailsPanel(String name) {
        stockDetailPanel = new JPanel();
        JLabel stockDetails = new JLabel(stockToString(name));
        stockDetails.setFont(new Font("", Font.PLAIN, 20));
        stockDetailPanel.add(stockDetails);
    }

    // EFFECTS: returns stock details as a String
    private String stockToString(String name) {
        Stock stock = stockMarket.lookUpStock(name);
        DecimalFormat df = new DecimalFormat("0.##");

        return "<html><u>" + name + "</u><br>Current Value: $" + df.format(stock.getCurrentValue()) + "<br>Bid: $"
                + df.format(stock.getBidPrice()) + "<br>Ask: $" + df.format(stock.getAskPrice()) + "<br>Spread: $"
                + df.format(stock.getAskPrice() - stock.getBidPrice()) + "<br>Change Percentage: "
                + df.format(stock.getPercentChange()) + "%";
    }

    // MODIFIES: this
    // EFFECTS: sets background image of the window
    private void setBackground(String fileName) {
        background = new JLabel(new ImageIcon(fileName));
        background.setLayout(new FlowLayout());
        frame.add(background);
    }

    // MODIFIES: this
    // EFFECTS: creates panel showcasing balance in portfolio
    private void createBalancePanel() {
        DecimalFormat df = new DecimalFormat("0.##");
        balancePanel = new JPanel();
        JLabel balanceLabel = new JLabel("$" + df.format(portfolio.getBalance()));
        balanceLabel.setFont(new Font("", Font.PLAIN, 100));
        balancePanel.add(balanceLabel);
    }

    // MODIFIES: this
    // EFFECTS: if portfolio is empty, prints out "You currently don't won any stocks."
    //          else prints out info of stocks owned, i.e., the name and number of shares owned
    private void createPortfolioPanel() {
        portfolioPanel = new JPanel();
        portfolioPanel.removeAll();
        portfolioPanel.setLayout(new BoxLayout(portfolioPanel, BoxLayout.Y_AXIS));

        String stockInfo = "";
        ArrayList<Stock> stocksOwned = portfolio.getStocksOwned();

        if (stocksOwned.isEmpty()) {
            stockInfo = "You currently don't own any stocks.";
            JLabel stockOwnedInfoLabel = new JLabel(stockInfo);
            stockOwnedInfoLabel.setFont(new Font("", Font.PLAIN, 35));
            portfolioPanel.add(stockOwnedInfoLabel);
        } else {
            for (Stock s : stocksOwned) {
                stockInfo = s.getName() + ", " + s.getSharesPurchased() + "; $"
                        + (s.getSharesPurchased() * s.getCurrentValue());
                JLabel stockOwnedInfoLabel = new JLabel(stockInfo);
                stockOwnedInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                stockOwnedInfoLabel.setFont(new Font("", Font.PLAIN, 20));
                portfolioPanel.add(stockOwnedInfoLabel);
                portfolioPanel.add(Box.createVerticalStrut(10));
            }
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(companiesBox)) {
            JComboBox cb = (JComboBox) source;
            String companyName = (String) cb.getSelectedItem();
            companyImage.setIcon(IMAGES.get(companyName));
        }

        if (source instanceof JButton) {
            JButton btn = (JButton) e.getSource();
            if (btn.getName().equals("stock listing button")) {
                createStockDetailsPanel(e.getActionCommand());
                switchPanel(stockDetailPanel, false);
            } else if (btn.getName().equals("commands button")) {
                doCommands(e);
            } else if (btn.getName().equals("input button")) {
                purchaseOrSellShares(e);
            }
        }


    }

    // MODIFIES: this
    // EFFECTS: purchases shares if user presses BUY button;
    //          sells shares if user presses SELL button
    private void purchaseOrSellShares(ActionEvent event) {
        int sharesToSellOrBuy = 0;

        try {
            sharesToSellOrBuy = Integer.parseInt(inputField.getText());
            String companyName = (String) companiesBox.getSelectedItem();

            if (event.getActionCommand().equals("BUY")) {
                purchaseShares(companyName, sharesToSellOrBuy);

            } else {
                sellShares(companyName, sharesToSellOrBuy);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "The input you entered was invalid.", "Alert",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: stockMarket, portfolio
    // EFFECTS: purchases amount of shares from a company
    //          if purchase is successful sends out confirmation message
    //          else sends out alert
    private void purchaseShares(String companyName, int sharesToBuy) {
        boolean purchaseSuccessful = stockMarket.purchaseShares(companyName, sharesToBuy, portfolio);
        if (purchaseSuccessful) {
            JOptionPane.showMessageDialog(frame, sharesToBuy + " " + companyName
                    + " shares successfully purchased.");
        } else {
            JOptionPane.showMessageDialog(frame, "Insufficient funds. Purchase Unsuccessful.",
                    "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: stockMarket, portfolio
    // EFFECTS: if stock is in the portfolio and there are sufficient shares owned:
    //               - sell the specified number of shares at the ask price
    //               - give confirmation message
    //          else if stock is in the portfolio but the amount desiring to be sold exceeds the shares owned:
    //               - give option to sell all shares of that stock
    //          otherwise give error message
    private void sellShares(String companyName, int sharesToSell) {
        int sellStatus = stockMarket.sellShares(companyName, sharesToSell, portfolio);

        if (sellStatus == 1) {
            JOptionPane.showMessageDialog(frame, sharesToSell + " " + companyName + " shares successfully sold.");
        } else {
            JOptionPane.showMessageDialog(frame, "You currently don't own the specified amount of shares.",
                    "Alert", JOptionPane.ERROR_MESSAGE);
        }

    }


    // MODIFIES: this
    // EFFECTS: processes user's commands
    private void doCommands(ActionEvent event) {
        if (event.getActionCommand().equals("SEE ALL STOCK DETAILS")) {
            createAllStockDetailsPanel();
            switchPanel(allStockDetailsPanel, false);
        } else if (event.getActionCommand().equals("SEE BALANCE")) {
            createBalancePanel();
            switchPanel(balancePanel, false);
        } else if (event.getActionCommand().equals("PURCHASE SHARES")) {
            createPurchaseOrSellPanel(true);
            switchPanel(purchaseAndSellPanel, false);
        } else if (event.getActionCommand().equals("SELL SHARES")) {
            createPurchaseOrSellPanel(false);
            switchPanel(purchaseAndSellPanel, false);
        } else if (event.getActionCommand().equals("SEE PORTFOLIO")) {
            createPortfolioPanel();
            switchPanel(portfolioPanel, false);
        } else if (event.getActionCommand().equals("START A NEW DAY")) {
            stockMarket.nextDay();
            JOptionPane.showMessageDialog(frame, "Time to start a new day!");
        } else {
            endGame();
        }
    }

    // MODIFIES: this
    // EFFECTS: Gives option to save progress and exits the program
    private void endGame() {
        int response = JOptionPane.showConfirmDialog(frame, "Would you like to save your progress?",
                "Bye!", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            saveProgress();
        }
        frame.dispose();
    }

    // EFFECTS: saves the market and portfolio to file
    // Citation: method code obtained and modified from JsonSerializationDemo
    //           https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void saveProgress() {
        try {
            marketJsonWriter.open();
            marketJsonWriter.write(stockMarket);
            marketJsonWriter.close();
            System.out.println("Saved market.");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write market to file.");
        }

        try {
            portfolioJsonWriter.open();
            portfolioJsonWriter.write(portfolio);
            portfolioJsonWriter.close();
            System.out.println("Saved portfolio.");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write portfolio to file.");
        }

    }


    public static void main(String[] args) {
        new StockMarketGUI();
    }

}
