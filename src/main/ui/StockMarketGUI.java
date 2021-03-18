package ui;

import javax.swing.*;
import java.awt.*;

public class StockMarketGUI extends StockMarketSimulator {

    private static final String BACKGROUND_IMAGE = "./data/bouncy cat.gif";

    private JFrame frame;

    private JPanel introPanel;
    private JPanel stockListingsPanel;
    private JPanel commandsPanel;
    private JPanel mainPanel;

    private JLabel background;

    // EFFECTS: sets up an interface
    public StockMarketGUI() {
        super();
        setUpFrame();

        createMainPanel();

        frame.pack();
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

        JButton continueGameBtn = new JButton("Continue Game");
        JButton newGameBtn = new JButton("New Game");

        introPanel.add(continueGameBtn);
        introPanel.add(newGameBtn);
    }

    // MODIFIES: this
    // EFFECTS: TODO
    private void createStockListingsPanel() {
        stockListingsPanel = new JPanel();
        JButton[] stocksBtns = new JButton[companies.length];
        JLabel listings = new JLabel("Current Stock Listings: ");
        stockListingsPanel.setPreferredSize(new Dimension(620, 40));

        stockListingsPanel.add(listings);


        for (int i = 0; i < companies.length; i++) {
            stocksBtns[i] = new JButton(companies[i]);
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

        JButton[] commandBtns = new JButton[7];

        commandBtns[0] = new JButton("SEE ALL STOCK DETAILS");
        commandBtns[1] = new JButton("SEE BALANCE");
        commandBtns[2] = new JButton("PURCHASE SHARES");
        commandBtns[3] = new JButton("SELL SHARES");
        commandBtns[4] = new JButton("SEE PORTFOLIO");
        commandBtns[5] = new JButton("START A NEW DAY");
        commandBtns[6] = new JButton("QUIT GAME");

        commandsPanel.add(Box.createVerticalStrut(10));
        for (JButton btn: commandBtns) {
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
    private void setBackground(String fileName) {
        background = new JLabel(new ImageIcon(fileName));
        background.setLayout(new FlowLayout());
        frame.add(background);
    }

    public static void main(String[] args) {
        new StockMarketGUI();
    }

}
