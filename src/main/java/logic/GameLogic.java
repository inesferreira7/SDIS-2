package logic;

import gui.CardsAgainstHumanity;
import gui.EndPanel;
import gui.PlayPanel;
import logic.login.LoginClient;
import parser.CardDatabase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by up201404990 on 25-05-2017.
 */
public class GameLogic {
    public static final int MIN_NUM_PLAYERS = 1, MAX_NUM_PLAYERS = 6;
    public static final int PLAYERS_PICKING = 0, PICK_WINNER = 1, END_ROUND = 2;
    public static final String[] lastMessage = {"NEWCZAR", "BLACKCARD", "PICKINGCARDS", "ELECTBEST", "RETRIEVECARD"};
    public static final int WHITECARDS_PER_PLAYER = 5;
    private static GameLogic instance = null;
    
    private final int POINTS_TO_END = 5;
    private ArrayList<Player> players;
    private LinkedHashMap<Player, ArrayList<WhiteCard>> whiteCardPicks;
    private BlackCard blackCard;
    private Stack<WhiteCard> whiteCardsDeck;
    private Stack<BlackCard> blackCardsdeck;
    private int czar;
    private int[] leaders = new int[]{0, 1, 2};
    private Player me;
    private int gameState;

    private GameLogic() {
        this.gameState = -1;
        players = new ArrayList<>();
        whiteCardPicks = new LinkedHashMap<>();
        whiteCardsDeck = new Stack<>();
        blackCardsdeck = new Stack<>();
        czar = -1;

        createDecks();
    }

    //CZARCHANGE [INT]
    //BLACKCARD [INT] [TEXT]
    //WHITECARDPICK [INT] [INT] "[STRING]"
    //WINNERPICK [INT]
    //RETRIEVEWHITECARD [TEXT]

    public static GameLogic getInstance() {
        if (instance == null) instance = new GameLogic();
        return instance;
    }

    public void createDecks() {
        CardDatabase.parseCards();
        long seed = System.nanoTime();
        Collections.shuffle(CardDatabase.getWhiteCards(), new Random(seed));
        Collections.shuffle(CardDatabase.getBlackCards(), new Random(seed));
        whiteCardsDeck.addAll(CardDatabase.getWhiteCards());
        blackCardsdeck.addAll(CardDatabase.getBlackCards());

    }

    public boolean playerPicked(Player p) {
        for (Map.Entry<Player, ArrayList<WhiteCard>> entry : whiteCardPicks.entrySet()) {
            if (entry.getKey().equals(p)) return true;
        }
        return false;
    }

    public boolean allPlayersPicked() {
        return whiteCardPicks.size() == (players.size() - 1);
    }

    public void addWhiteCardsToBoard(ArrayList<WhiteCard> whiteCard) {
        whiteCardPicks.put(whiteCard.get(0).getOwner(), whiteCard);
    }

    public void clearBoard() {
        whiteCardPicks.clear();
        blackCard = null;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void changeCzar() {
        czar = (czar + 1) % players.size();
    }

    public void playerPickedCard(ArrayList<WhiteCard> whiteCards) {
        me.removeCards(whiteCards);
        StringBuilder builder = new StringBuilder();
        builder.append(MessageType.WHITECARDPICK.name());
        builder.append(" ");
        builder.append(whiteCards.size());
        for (Card c :
                whiteCards) {
            builder.append(" ");
            builder.append(c.toSerializedString());
        }
        String cmd = builder.toString();
        broadcastMessage(cmd);
    }

    public Player getMe() {
        return me;
    }

    public void setMe(Player me) {
        this.me = me;
    }

    public void selectWinner(WhiteCard winnerCard) {
        winnerCard.getOwner().addPoints(1);
    }


    public void endRound() {
        clearBoard();
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        if (gameState == PLAYERS_PICKING) {
            //TODO: Update interface
            whiteCardPicks = new LinkedHashMap<>();
        } else if (gameState == PICK_WINNER) {
            //TODO: Update interface - reveal choices
        } else if (gameState == END_ROUND) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPoints() >= POINTS_TO_END) {
                    CardsAgainstHumanity.getInstance().changePanel(PlayPanel.getInstance().getPanel(), EndPanel.getInstance(players).getPanel());
                }
            }
            if (players.indexOf(me) == getLeaderIndex()) {
                sendWhiteCards();
            }
            blackCard = null;
            whiteCardPicks = new LinkedHashMap<>();
            if (players.indexOf(me) == getLeaderIndex())
                sendBlackCard();
        }
        this.gameState = gameState;
        PlayPanel.getInstance().refreshInterface();
    }

    public void sendWhiteCards() {
        try {
            DatagramSocket tempSocket = new DatagramSocket();
            for (Player p :
                    players) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MessageType.RETRIEVEWHITECARD.name());
                stringBuilder.append(" ");
                ArrayList<WhiteCard> cards = new ArrayList<>();
                int numCards;
                if (blackCard != null) numCards = blackCard.getPick();
                else numCards = WHITECARDS_PER_PLAYER;
                for (int i = 0; i < numCards; i++)
                    cards.add(whiteCardsDeck.pop());
                stringBuilder.append(Integer.toString(cards.size()));
                for (WhiteCard c : cards) {
                    stringBuilder.append(" ");
                    stringBuilder.append(c.toSerializedString());
                }

                String cmd = stringBuilder.toString();
                byte[] buf = cmd.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, p.getIp(), LoginClient.SOCKET_PORT);
                tempSocket.send(packet);
            }
            tempSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBlackCard() {
        System.out.println("Sending black card...");
        BlackCard chosenCard = blackCardsdeck.pop();

        String cmd = MessageType.BLACKCARD.name();
        cmd += " ";
        cmd += chosenCard.toSerializedString();
        broadcastMessage(cmd);

    }

    public HashMap<Player, ArrayList<WhiteCard>> getWhiteCardPicks() {
        return whiteCardPicks;
    }

    public int getLeaderIndex() {
        return getLeaderIndex(0);
    }

    public int getLeaderIndex(int leaderNo) {
        return leaders[leaderNo];
    }

    public BlackCard getBlackCard() {
        return blackCard;
    }

    public void setBlackCard(BlackCard b) {
        blackCard = b;
    }

    public Player getWhiteCardOwner(WhiteCard card) {
        for (Map.Entry<Player, ArrayList<WhiteCard>> entry : whiteCardPicks.entrySet()) {
            for (WhiteCard c :
                    entry.getValue()) {
                if (c.equals(card))
                    return entry.getKey();
            }
        }
        return null;
    }

    public boolean isCzar(Player p) {
        return players.indexOf(p) == czar;
    }

    public void broadcastMessage(String message, ArrayList<Player> playersList) {
        ArrayList<Player> missingACK = new ArrayList<>(playersList);
        ArrayList<Thread> threads = new ArrayList<>();
        int noTriesLeft = 3;
        try {
            DatagramSocket tempSocket = new DatagramSocket();
            tempSocket.setSoTimeout(3000);
            while (noTriesLeft > 0 && !missingACK.isEmpty()) {
                for (Player p :
                        missingACK) {

                    byte[] buf = message.getBytes();

                    DatagramPacket packet = new DatagramPacket(buf, buf.length, p.getIp(), LoginClient.SOCKET_PORT);
                    tempSocket.send(packet);
                    Thread t = new Thread(() -> {
                        try {
                            byte[] receiveBuffer = new byte[256];
                            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                            tempSocket.receive(receivePacket);

                            // TODO: check ACK
                            InetAddress ackAddress = receivePacket.getAddress();
                            for (Iterator<Player> it = missingACK.iterator(); it.hasNext();) {
                                Player player = it.next();
                                if (p.getIp().equals(ackAddress)) {
                                    System.out.println("Received ACK from " + player);
                                    it.remove();
                                }
                            }
                        } catch (SocketTimeoutException e) {
//                                    e.printStackTrace();
                            System.out.println("Socket timed out");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    t.start();
                    threads.add(t);
                }
                for(Thread t : threads) t.join();
                noTriesLeft--;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if(missingACK.size() > 0)
            System.err.println("NOT ALL PLAYERS SENT ACK");
    }

    public void broadcastMessage(String message) {
        broadcastMessage(message, players);
    }

    public Player getPlayerByAnswerOrder(int i) {
        int j = 0;
        for (Map.Entry<Player, ArrayList<WhiteCard>> entry : whiteCardPicks.entrySet()) {
            if (i == j) return entry.getKey();
            j++;
        }
        return null;
    }
}
