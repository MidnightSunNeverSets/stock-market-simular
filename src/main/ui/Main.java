package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// runs the program
public class Main extends JFrame {

    private JLabel background;
    private IntroPanel introPanel;

    public Main() {
        super("Stock Market Simulator");

        introPanel = new IntroPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground("./data/bouncy cat.gif");
        setVisible(true);
        pack();
        setResizable(false);
        setVisible(true);

        background.add(introPanel);

//        createIntroPanel();


    }

    // REQUIRES: a valid file name
    // MODIFIES: this
    // EFFECTS: sets the background image of the frame
    private void setBackground(String fileName) {
        background = new JLabel(new ImageIcon(fileName));
        background.setLayout(new FlowLayout());
        add(background);
    }

//    private void createIntroPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setOpaque(false);
//
//
//        JButton newGameBtn = new JButton("Start New Game");
//        newGameBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("hello");
//            }
//        });
//
//        JButton continueGame = new JButton("Continue Game");
//        panel.add(continueGame);
//
//        // adds buttons to panel
//        panel.add(Box.createVerticalStrut(20));
//        panel.add(newGameBtn);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(continueGame);
//
//
//
//        background.add(panel);
//        repaint();
//
//    }


    public static void main(String[] args) {

        new Main();

//        new StockMarketSimulator();


    }


}
