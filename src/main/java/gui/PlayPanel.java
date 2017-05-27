package gui;

import logic.GameLogic;
import logic.MessageType;
import logic.Player;
import logic.WhiteCard;
import logic.login.LoginClient;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by up201404990 on 26-05-2017.
 */
public class PlayPanel extends JFrame {

    private static final int NUM_LINES = 4;
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_GRID = NUM_COLUMNS * NUM_LINES;
    private static PlayPanel instance = null;
    int noSelectedCards = 0;
    private JPanel panel;
    private Border selectedBorder = BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black);
    private Border deselectedBorder = null;
    private ArrayList<WhiteCard> selectedCards;
    private ArrayList<JTextArea> cards;
    private MouseListener handCardListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

            GameLogic logic = GameLogic.getInstance();
            if (logic.getGameState() == GameLogic.PLAYERS_PICKING && !logic.playerPicked(logic.getMe()) && !logic.isCzar(logic.getMe())) {
                JTextArea clickedCard = (JTextArea) mouseEvent.getSource();
                if (logic.getBlackCard().getPick() > 1) {
                    if (clickedCard.getBorder().equals(selectedBorder))
                        deselectCard(clickedCard);
                    else
                        selectCard(clickedCard);
                    if (noSelectedCards == logic.getBlackCard().getPick()) {
                        logic.playerPickedCard(selectedCards);
                        noSelectedCards = 0;
                        selectedCards = new ArrayList<>();
                    }
                } else {
                    if (mouseEvent.getClickCount() >= 2) {
                        selectedCards.add(logic.getMe().getCardWithText(clickedCard.getText()));
                        logic.playerPickedCard(selectedCards);
                        selectedCards = new ArrayList<>();
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    };

    private MouseListener toPickCardListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

            GameLogic logic = GameLogic.getInstance();
            if (logic.getGameState() == GameLogic.PICK_WINNER && logic.isCzar(logic.getMe())) {
                JTextArea clickedCard = (JTextArea) mouseEvent.getSource();
                if (mouseEvent.getClickCount() >= 2) {
                    Player winner = logic.getWhiteCardOwner(new WhiteCard(clickedCard.getText(), null));

                    try {
                        DatagramSocket tempSocket = new DatagramSocket();
                        String cmd = MessageType.WINNERPICK.name() +
                                " " +
                                logic.getPlayers().indexOf(winner);
                        for (Player p :
                                logic.getPlayers()) {
                            DatagramPacket packet = new DatagramPacket(cmd.getBytes(), cmd.getBytes().length, p.getIp(), LoginClient.SOCKET_PORT);
                            tempSocket.send(packet);
                        }
                        tempSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    };

    public PlayPanel() {
        selectedCards = new ArrayList<>();
        initializeCards();
        initializePanel();
        refreshInterface();
    }

    public static PlayPanel getInstance() {
        if (instance == null)
            instance = new PlayPanel();
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    private void deselectCard(JTextArea area) {
        area.setBorder(deselectedBorder);
        WhiteCard card = new WhiteCard(area.getText(), null);

        int index = selectedCards.indexOf(card);

        for (int i = index; i < selectedCards.size() - 1; i++)
            selectedCards.set(i, selectedCards.get(i + 1));
        selectedCards.set(selectedCards.size() - 1, null);

        noSelectedCards--;
    }

    private void selectCard(JTextArea area) {
        area.setBorder(selectedBorder);
        selectedCards.add(GameLogic.getInstance().getMe().getCardWithText(area.getText()));
        noSelectedCards++;
    }

    public JTextArea createBlackCard(String text) {
        JTextArea card = new JTextArea(text);

        card.setEditable(false);
        card.setHighlighter(null);
        card.setBackground(Color.black);
        if (text.isEmpty())
            card.setBackground(Color.darkGray);
        card.setForeground(Color.white);
        card.setLineWrap(true);

        return card;
    }

    public void initializeCards() {
        cards = new ArrayList<>();
        JTextArea card1 = createBlackCard("Black Card");
        cards.add(card1);

        for (int i = 1; i < NUM_GRID; i++) {
            if (i == 6 || i == 7 || (i > 12 && i < 22) || i == 27) {
                cards.add(createBlackCard(""));
            } else
                cards.add(createWhiteCard("", null));
        }
    }

    public void refreshInterface() {
        System.out.println("updating interface");
        GameLogic logic = GameLogic.getInstance();
        Component[] components = new Component[NUM_GRID];

        for (int i = 0; i < components.length; i++) components[i] = createBlackCard("");

        String text = "";
        if (logic.getBlackCard() != null) text = logic.getBlackCard().getText();
        components[0] = createBlackCard(text);

        int i = 1;
        for (Map.Entry<Player, ArrayList<WhiteCard>> entry : logic.getWhiteCardPicks().entrySet()) {
            int j = i;
            for (WhiteCard card : entry.getValue()) {
                String cardText = "Waiting for\nAll Players\nto pick...";
                if (logic.allPlayersPicked()) cardText = card.getText();
                components[j] = createWhiteCard(cardText, toPickCardListener);
                j += NUM_COLUMNS;
            }
            i++;
        }

        i = (NUM_LINES - 1) * NUM_COLUMNS + 1;
        for (WhiteCard card : logic.getMe().getCards()) {
            components[i] = createWhiteCard(card.getText(), handCardListener);
            i++;
        }

        panel.removeAll();
        for (i = 0; i < components.length; i++) panel.add(components[i]);
        panel.revalidate();
//        panel.repaint();

    }

    public JTextArea createWhiteCard(String text, MouseListener listener) {
        JTextArea card = new
                JTextArea(text);
        card.setHighlighter(null);
        card.addMouseListener(listener);
        card.setLineWrap(true);
        card.setWrapStyleWord(true);
        card.setBackground(Color.white);
        card.setForeground(Color.black);
        card.setFont(new Font("Georgia", Font.BOLD, 14));
        card.setEditable(false);

        return card;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void initializePanel() {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        panel.setLayout(new GridLayout(4, 7, 20, 15));
        panel.setBackground(Color.darkGray);

        for (int i = 0; i < NUM_GRID; i++)
            panel.add(cards.get(i));

        deselectedBorder = cards.get(0).getBorder();
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

}
