package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by up201404990 on 26-05-2017.
 */
public class PlayPanel extends JFrame {

    public static JTextArea createVoidCard(){
        JTextArea card = new JTextArea("empty text card");
        card.setLineWrap(true);
        card.setWrapStyleWord(true);
        card.setBackground(Color.white);
        card.setForeground(Color.black);
        card.setFont(new Font("Georgia", Font.BOLD, 14));
        card.setEditable(false);

        return card;
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Grid Layout");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,550);
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        panel.setLayout(new GridLayout(4,7,20,15));
        JLabel card1 = new JLabel("BlackCard",SwingConstants.CENTER);
        card1.setForeground(Color.white);

        JTextArea card2 = createVoidCard();
        JTextArea card3 = createVoidCard();
        JTextArea card4 = createVoidCard();
        JTextArea card5 = createVoidCard();
        JTextArea card6 = createVoidCard();
        JLabel card7 = new JLabel("");

        JLabel card8 = new JLabel("");
        JTextArea card9 = createVoidCard();
        JTextArea card10 = createVoidCard();
        JTextArea card11 = createVoidCard();
        JTextArea card12 = createVoidCard();
        JTextArea card13 = createVoidCard();
        JLabel card14 = new JLabel("");

        JLabel card15 = new JLabel("");
        JLabel card16 = new JLabel("");
        JLabel card17 = new JLabel("");
        JLabel card18 = new JLabel("");
        JLabel card19 = new JLabel("");
        JLabel card20 = new JLabel("");

        JLabel card21 = new JLabel("");
        JLabel card22 = new JLabel("");
        JTextArea card23 = createVoidCard();
        JTextArea card24 = createVoidCard();
        JTextArea card25 = createVoidCard();
        JTextArea card26 = createVoidCard();
        JTextArea card27 = createVoidCard();

        JLabel card28 = new JLabel("");

        panel.setBackground(Color.black);

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

        panel.add(card22);
        panel.add(card23);
        panel.add(card24);
        panel.add(card25);
        panel.add(card26);
        panel.add(card27);
        panel.add(card28);

        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.add(panel);
        frame.setVisible(true);
    }

}
