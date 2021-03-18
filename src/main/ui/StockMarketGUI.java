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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StockMarketGUI implements ActionListener {

    // GUI COMPONENTS
    private static final String[] COMPANIES = {"Shibe Inc.", "Papaya", "Tweety", "GuCCe", "MIYO", "Yoko"};
    private static final String BACKGROUND_IMAGE = "./data/bouncy cat.gif";
    private static final Map<String, ImageIcon> IMAGES = createImageMap();

    private JFrame frame;

    private JPanel introPanel;
    private JPanel stockListingsPanel;
    private JPanel commandsPanel;
    private JPanel mainPanel;
    private JPanel purchaseAndSellPanel;
    private JPanel stockDetailPanel;

    private JLabel background;
    private JLabel companyImage;

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

        setUpFrame();
        createStockDetailsPanel();
        frame.add(stockDetailPanel);
        frame.pack();
    }

    // EFFECTS: returns map of all the images used
    private static Map<String, ImageIcon> createImageMap() {
        Map<String, ImageIcon> imageMap = new HashMap<>();
        imageMap.put(COMPANIES[0], new ImageIcon("./data/shibe.png"));
        imageMap.put("Papaya", new ImageIcon("./data/papaya.png"));
        imageMap.put("Tweety", new ImageIcon("./data/seagull.png"));


        return imageMap;
    }

    // MODIFIES: this
    // EFFECTS: TODO
    private void setUpFrame() {
        frame = new JFrame("Stock Market Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: TODO
    private void createIntroPanel() {
        introPanel = new JPanel();
        introPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        introPanel.setPreferredSize(new Dimension(180, 100));

        JButton continueGameBtn = new JButton("Continue Game"); // TODO: add action listener
        JButton newGameBtn = new JButton("New Game"); // TODO: add action listener

        introPanel.add(continueGameBtn);
        introPanel.add(newGameBtn);
    }

    // MODIFIES: this
    // EFFECTS: TODO
    private void createStockListingsPanel() {
        stockListingsPanel = new JPanel();
        JButton[] stocksBtns = new JButton[COMPANIES.length]; // TODO: add action listener
        JLabel listings = new JLabel("Current Stock Listings: ");
        stockListingsPanel.setPreferredSize(new Dimension(620, 40));

        stockListingsPanel.add(listings);

        for (int i = 0; i < COMPANIES.length; i++) {
            stocksBtns[i] = new JButton(COMPANIES[i]);
            stockListingsPanel.add(stocksBtns[i]);
        }

    }

    // MODIFIES: this
    // EFFECTS: TODO
    private void createCommandsPanel() {
        commandsPanel = new JPanel();

        commandsPanel.setLayout(new BoxLayout(commandsPanel, BoxLayout.Y_AXIS));
//        commandsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        commandsPanel.setPreferredSize(new Dimension(200, 240));

        JButton[] commandBtns = new JButton[7]; // TODO: add action listener

        commandBtns[0] = new JButton("SEE ALL STOCK DETAILS");
        commandBtns[1] = new JButton("SEE BALANCE");
        commandBtns[2] = new JButton("PURCHASE SHARES");
        commandBtns[3] = new JButton("SELL SHARES");
        commandBtns[4] = new JButton("SEE PORTFOLIO");
        commandBtns[5] = new JButton("START A NEW DAY");
        commandBtns[6] = new JButton("QUIT GAME");

        commandsPanel.add(Box.createVerticalStrut(10));
        for (JButton btn : commandBtns) {
            commandsPanel.add(btn);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            commandsPanel.add(Box.createVerticalStrut(5));
        }
    }

    // MODIFIES: this
    // EFFECTS: TODO
    private void createMainPanel() {
        createCommandsPanel();
        createStockListingsPanel();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(stockListingsPanel);
        mainPanel.add(commandsPanel);
    }

    // MODIFIES: this
    // EFFECTS: TODO
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

    // EFFECTS: creates the input textfield, label and button for purchasing shares
    private JPanel createPurchaseAndSellInputPanel(boolean purchase) {
        JPanel inputPanel = new JPanel();
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JComboBox companiesBox = new JComboBox(COMPANIES); // TODO: add action listener
        companiesBox.addActionListener(this);

        JTextField inputField = new JTextField();
        inputField.setColumns(3);

        JLabel stockNumQuestion = new JLabel();
        JButton inputBtn = new JButton();

        if (purchase) { // TODO: add action listener
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
    // EFFECTS: TODO
    private void createStockDetailsPanel() {
        stockDetailPanel = new JPanel();
        JLabel stockDetails = new JLabel(stockToString("Papaya"));
        stockDetailPanel.add(stockDetails);
    }

    private String stockToString(String name) {
        Stock stock = stockMarket.lookUpStock(name);
        DecimalFormat df = new DecimalFormat("0.##");

        return "<html>" + name + "<br>Current Value: $" + df.format(stock.getCurrentValue()) + "<br>Bid: $"
                + df.format(stock.getBidPrice()) + "<br>Ask: $" + df.format(stock.getAskPrice()) + "<br>Spread: $"
                + df.format(stock.getAskPrice() - stock.getBidPrice()) + "<br>Change Percentage: $"
                + df.format(stock.getPercentChange());
    }

    // MODIFIES: this
    // EFFECTS: TODO
    private void setBackground(String fileName) {
        background = new JLabel(new ImageIcon(fileName));
        background.setLayout(new FlowLayout());
        frame.add(background);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JComboBox) {
            JComboBox cb = (JComboBox)e.getSource();
            String companyName = (String)cb.getSelectedItem();
            companyImage.setIcon(IMAGES.get(companyName));
        }

    }

    public static void main(String[] args) {
        new StockMarketGUI();
    }

}
