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
    private static final int SCORES_POS = 13;
    private static final int GAME_STATE_POS = 6;
    private static PlayPanel instance = null;
    private int noSelectedCards = 0;
    private JPanel panel;
    private JEditorPane scores;
    private JTextArea state;
    private Border selectedBorder = BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black);
    private Border deselectedBorder = null;
    private ArrayList<WhiteCard> selectedCards;
    private ArrayList<JEditorPane> cards;
    private MouseListener handCardListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            GameLogic logic = GameLogic.getInstance();
            if (mouseEvent.getClickCount() >= 2) {
                if (logic.getGameState() == GameLogic.PLAYERS_PICKING && !logic.playerPicked(logic.getMe()) && !logic.isCzar(logic.getMe())) {
                    JEditorPane clickedCard = (JEditorPane) mouseEvent.getSource();
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
                        int cardIndex = getComponentIndex(clickedCard) % NUM_COLUMNS - 1;
                        selectedCards.add(logic.getMe().getCards().get(cardIndex));
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

    public int getComponentIndex(Component c) {
        Component[] components = panel.getComponents();
        for (int i = 0; i < components.length; i++)
            if(c.equals(components[i])) return i;
        return -1;
    }

    private MouseListener toPickCardListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

            GameLogic logic = GameLogic.getInstance();
            if (logic.getGameState() == GameLogic.PICK_WINNER && logic.isCzar(logic.getMe())) {
                JEditorPane clickedCard = (JEditorPane) mouseEvent.getSource();
                if (mouseEvent.getClickCount() >= 2) {
                    int playerIndex = getComponentIndex(clickedCard) % NUM_COLUMNS -1;
                    Player winner = logic.getPlayerByAnswerOrder(playerIndex);
                        String cmd = MessageType.WINNERPICK.name() +
                                " " +
                                logic.getPlayers().indexOf(winner);
                    GameLogic.getInstance().broadcastMessage(cmd);
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
        initializeScores_and_State();
        refreshInterface();
    }

    public void initializeScores_and_State(){
        scores = new JEditorPane("text/html", "Scores");
        scores.setBackground(Color.black);
        scores.setForeground(Color.white);
        scores.setFont(new Font("Georgia", Font.BOLD, 14));
        scores.setEditable(false);
        state = new JTextArea("");
        state.setBackground(Color.darkGray);
        state.setForeground(Color.white);
        state.setFont(new Font("Georgia", Font.BOLD, 14));
        state.setLineWrap(true);
        state.setWrapStyleWord(true);
        state.setEditable(false);
    }

    public static PlayPanel getInstance() {
        if (instance == null)
            instance = new PlayPanel();
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    private void deselectCard(JEditorPane area) {
        area.setBorder(deselectedBorder);

        int cardIndex = getComponentIndex(area) % NUM_COLUMNS - 1;
        WhiteCard card = GameLogic.getInstance().getMe().getCards().get(cardIndex);

        int index = selectedCards.indexOf(card);

        for (int i = index; i < selectedCards.size() - 1; i++)
            selectedCards.set(i, selectedCards.get(i + 1));
        selectedCards.set(selectedCards.size() - 1, null);

        noSelectedCards--;
    }

    private void selectCard(JEditorPane area) {
        area.setBorder(selectedBorder);
        int cardIndex = getComponentIndex(area) % NUM_COLUMNS - 1;
        selectedCards.add(GameLogic.getInstance().getMe().getCards().get(cardIndex));
        noSelectedCards++;
    }

    public JEditorPane createBlackCard(String text) {
        JEditorPane card = new JEditorPane("text/html", "<span style=color:white;>" + text + "</span>");

        card.setEditable(false);
        card.setHighlighter(null);
        card.setBackground(Color.black);
        card.setForeground(Color.WHITE);
        if (text.isEmpty())
            card.setBackground(Color.darkGray);
        
        return card;
    }

    public void initializeCards() {
        cards = new ArrayList<>();
        JEditorPane card1 = createBlackCard("Black Card");
        cards.add(card1);

        for (int i = 1; i < NUM_GRID; i++) {
            if (i == 6 || i == 7 || (i > 12 && i < 22) || i == 27) {
                cards.add(createBlackCard(""));
            } else
                cards.add(createWhiteCard("", null));
        }
    }

    public void refreshInterface() {
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

        StringBuilder sb = new StringBuilder(512);
        sb.append("<span style color=white>Points:<br><br>");
        for(i = 0; i < GameLogic.getInstance().getPlayers().size(); i++){
            sb.append(GameLogic.getInstance().getPlayers().get(i).getName());
            sb.append(": ");
            sb.append(GameLogic.getInstance().getPlayers().get(i).getPoints());
            sb.append("<br>");
        }
        sb.append("</span>");

        scores.setText(sb.toString());

        if(logic.getGameState() == logic.PLAYERS_PICKING){
            if(logic.isCzar(logic.getMe())){
                state.setText("You are the czar. Wait for all players to pick");
            }
            else
                state.setText("Pick your cards");
        }
        else if(logic.getGameState() == logic.PICK_WINNER){
            if(logic.isCzar(logic.getMe())){
                state.setText("You are the czar. Pick the winner card");
            }
            else
                state.setText("Waiting for czar to pick winner card");
        }


        panel.removeAll();
        for (i = 0; i < components.length; i++){
            if(i == SCORES_POS)
                panel.add(scores);
            else if(i == GAME_STATE_POS)
                panel.add(state);
            else
                panel.add(components[i]);
        }
        panel.revalidate();
//        panel.repaint();
    }

    public JEditorPane createWhiteCard(String text, MouseListener listener) {
        JEditorPane card = new
                JEditorPane("text/html", text);
        card.setHighlighter(null);
        card.addMouseListener(listener);
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
