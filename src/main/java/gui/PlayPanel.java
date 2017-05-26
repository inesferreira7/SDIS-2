package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by up201404990 on 26-05-2017.
 */
public class PlayPanel extends JFrame {



    public static void main(String[] args){
        JFrame frame = new JFrame("Grid Layout");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,550);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,7,7,5));
        JLabel card1 = new JLabel("BlackCard",SwingConstants.CENTER);
        card1.setForeground(Color.white);
        JLabel card2 = new JLabel("Player 1 card 1",SwingConstants.CENTER);
        JLabel card3 = new JLabel("Player 2 card 1",SwingConstants.CENTER);
        JLabel card4 = new JLabel("Player 3 card 1",SwingConstants.CENTER);
        JLabel card5 = new JLabel("Player 4 card 1",SwingConstants.CENTER);
        JLabel card6 = new JLabel("Player 5 card 1",SwingConstants.CENTER);
        JLabel card7 = new JLabel("");

        JLabel card8 = new JLabel("");
        JLabel card9 = new JLabel("Player 1 card 2",SwingConstants.CENTER);
        JLabel card10 = new JLabel("Player 2 card 2",SwingConstants.CENTER);
        JLabel card11 = new JLabel("Player 3 card 2",SwingConstants.CENTER);
        JLabel card12 = new JLabel("Player 4 card 2",SwingConstants.CENTER);
        JLabel card13 = new JLabel("Player 5 card 2",SwingConstants.CENTER);
        JLabel card14 = new JLabel("");

        JLabel card15 = new JLabel("",SwingConstants.CENTER);
        JLabel card16 = new JLabel("",SwingConstants.CENTER);
        JLabel card17 = new JLabel("",SwingConstants.CENTER);
        JLabel card18 = new JLabel("",SwingConstants.CENTER);
        JLabel card19 = new JLabel("",SwingConstants.CENTER);
        JLabel card20 = new JLabel("",SwingConstants.CENTER);
        JLabel card21 = new JLabel("");

        JPanel confirm = new JPanel(new GridLayout(3, 1,0, 10));
        confirm.setBackground(Color.black);
        JButton confirmBtn = new JButton("Confirm");
        confirm.add(new JLabel(""));
        confirm.add(confirmBtn);
        confirm.add(new JLabel(""));


        JLabel card22 = new JLabel("Card1",SwingConstants.CENTER);
        JLabel card23 = new JLabel("Card2",SwingConstants.CENTER);
        JLabel card24 = new JLabel("Card3",SwingConstants.CENTER);
        JLabel card25 = new JLabel("Card4",SwingConstants.CENTER);
        JLabel card26 = new JLabel("Card5",SwingConstants.CENTER);
        JLabel card27 = new JLabel("");



        panel.add(card1);
        panel.add(card2);
        panel.add(card3);
        panel.add(card4);
        panel.add(card5);
        panel.add(card6);
        panel.add(card7);

        panel.add(card8);
        panel.add(card9);
        panel.add(card10);
        panel.add(card11);
        panel.add(card12);
        panel.add(card13);
        panel.add(card14);

        panel.add(card15);
        panel.add(card16);
        panel.add(card17);
        panel.add(card18);
        panel.add(card19);
        panel.add(card20);
        panel.add(card21);

        panel.add(confirm);
        panel.add(card22);
        panel.add(card23);
        panel.add(card24);
        panel.add(card25);
        panel.add(card26);
        panel.add(card27);

        panel.setBackground(Color.black);
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.add(panel);

    }

}
