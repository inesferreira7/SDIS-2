package gui;

import logic.BlackCard;
import logic.GameLogic;
import logic.Player;
import logic.WhiteCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by up201404990 on 26-05-2017.
 */
public class PlayPanel extends JFrame {

    private JPanel panel;

    private static final int NUM_LINES = 4;
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_GRID = NUM_COLUMNS*NUM_LINES;

    private static PlayPanel instance = null;

    private ArrayList<JTextArea> cards;

    public static PlayPanel getInstance() {
        if(instance == null)
            instance = new PlayPanel();
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    public JTextArea createBlackCard(String text) {
        JTextArea card = new JTextArea(text);

        card.setEditable(false);
        card.setHighlighter(null);
        card.setBackground(Color.black);
        card.setForeground(Color.white);
        card.setLineWrap(true);

        return card;
    }

    public void initializeCards(){
        cards = new ArrayList<>();
        JTextArea card1 = createBlackCard("Black Card");
        cards.add(card1);

        for(int i = 1; i< NUM_GRID; i++) {
            if(i == 6 || i == 7 || (i > 12 && i < 22) || i == 27){
                cards.add(createBlackCard(""));
            }
            else
                cards.add(createWhiteCard(""));
        }
    }

    public void refreshInterface() {
        System.out.println("updating interface");
        GameLogic logic = GameLogic.getInstance();
        Component[] components = new Component[NUM_GRID];

        for (int i = 0; i < components.length; i++) components[i] = createBlackCard("");

        String text = "";
        if(logic.getBlackCard() != null) text = logic.getBlackCard().getText();
            components[0] = createBlackCard(text);

        int i = 1;
        for (Map.Entry<Player, ArrayList<WhiteCard>> entry : logic.getWhiteCardPicks().entrySet()) {
            int j = i;
            for(WhiteCard card : entry.getValue()){
                String cardText = "Waiting for\nAll Players\nto pick...";
                if(logic.allPlayersPicked()) cardText = card.getText();
                components[j] = createWhiteCard(cardText);
                j +=  NUM_COLUMNS;
            }
            i++;
        }

        i = (NUM_LINES-1)*NUM_COLUMNS + 1;
        for (WhiteCard card : logic.getMe().getCards()) {
            components[i] = createWhiteCard(card.getText());
            i++;
        }

        panel.removeAll();
        for (i = 0; i < components.length; i++) panel.add(components[i]);
        panel.revalidate();
//        panel.repaint();

    }

    public JTextArea createWhiteCard(String text){
        JTextArea card = new JTextArea(text);
        card.setHighlighter(null);
        card.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    System.out.println("double clicked");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        card.setLineWrap(true);
        card.setWrapStyleWord(true);
        card.setBackground(Color.white);
        card.setForeground(Color.black);
        card.setFont(new Font("Georgia", Font.BOLD, 14));
        card.setEditable(false);

        return card;
    }

    public JPanel getPanel(){
        return panel;
    }

    public void initializePanel(){
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        panel.setLayout(new GridLayout(4,7,20,15));
        panel.setBackground(Color.black);

        for(int i = 0; i < NUM_GRID; i++)
            panel.add(cards.get(i));

        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    public PlayPanel(){
        initializeCards();
        initializePanel();
        refreshInterface();
    }

}
