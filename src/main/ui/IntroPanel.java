package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroPanel extends JPanel implements ActionListener {

    public IntroPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        setPreferredSize(new Dimension(180, 100));

        JButton startBtn = new JButton("Continue Previous Game");
        startBtn.addActionListener(this);
        JButton newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(this);

        add(Box.createVerticalStrut(20));
        add(startBtn);
        add(newGameBtn);

        //        panel.add(Box.createVerticalStrut(20));
//        panel.add(newGameBtn);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(continueGame);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Continue Previous Game")) {
            System.out.println("hello");
        } else if (e.getActionCommand().equals("New Game")) {
            System.out.println("lol");
        }
    }


}
