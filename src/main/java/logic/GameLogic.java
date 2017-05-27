package logic;

import gui.PlayPanel;
import logic.login.LoginClient;
import parser.CardDatabase;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

/**
 * Created by up201404990 on 25-05-2017.
 */
public class GameLogic {
    /*test variables*/
    WhiteCard wCard;
    Player currentPlayer;
    private ArrayList<Player> players;
    private HashMap<Player, ArrayList<WhiteCard> > whiteCardPicks;
    private BlackCard blackCard;
    private Stack<WhiteCard> whiteCardsDeck;
    private Stack<BlackCard> blackCardsdeck;
    private int czar;

    private int[] leaders = new int[]{0,1,2};

    private Player me;

    /*test variables*/
    public static final int MIN_NUM_PLAYERS = 3, MAX_NUM_PLAYERS = 6;
    public static final int PLAYERS_PICKING = 0, PICK_WINNER = 1, END_ROUND = 2;
    public static final String[] lastMessage = {"NEWCZAR", "BLACKCARD", "PICKINGCARDS", "ELECTBEST", "RETRIEVECARD"};
    private int gameState;
    public static final int WHITECARDS_PER_PLAYER = 5;

    private static GameLogic instance = null;

    public static GameLogic getInstance() {
        if(instance == null) instance = new GameLogic();
        return instance;
    }

    //CZARCHANGE [INT]
    //BLACKCARD [INT] [TEXT]
    //WHITECARDPICK [INT] [INT] "[STRING]"
    //WINNERPICK [INT]
    //RETRIEVEWHITECARD [TEXT]

    private GameLogic(){
        this.gameState = -1;
        players = new ArrayList<>();
        whiteCardPicks = new HashMap<>();
        whiteCardsDeck = new Stack<>();
        blackCardsdeck = new Stack<>();
        czar = 0;

        createDecks();
    }

    public  void createDecks(){
        CardDatabase.parseCards();
        long seed = System.nanoTime();
        Collections.shuffle(CardDatabase.getWhiteCards(), new Random(seed));
        Collections.shuffle(CardDatabase.getBlackCards(), new Random(seed));
        whiteCardsDeck.addAll(CardDatabase.getWhiteCards());
        blackCardsdeck.addAll(CardDatabase.getBlackCards());

    }

    public boolean playerPicked(Player p) {
        for(Map.Entry<Player, ArrayList<WhiteCard>> entry : whiteCardPicks.entrySet()) {
            if(entry.getKey().equals(p)) return true;
        }
        return false;
    }

    public boolean allPlayersPicked() {
        return whiteCardPicks.size() == (players.size() - 1);
    }

    public void addWhiteCardsToBoard(ArrayList<WhiteCard> whiteCard) {
        whiteCardPicks.put(whiteCard.get(0).getOwner(), whiteCard);
    }

    public void clearBoard(){
        whiteCardPicks.clear();
        blackCard = null;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void changeCzar(int previousCzar){
        czar = czar++ % players.size();
    }

    public void setBlackCard(BlackCard b){
        blackCard = b;
    }

    public void playerPickedCard(ArrayList<WhiteCard> whiteCards){
        me.removeCards(whiteCards);
        try {
            DatagramSocket tempSocket = new DatagramSocket();
            StringBuilder builder = new StringBuilder();
            builder.append(MessageType.WHITECARDPICK.name());
            builder.append(" ");
            builder.append(whiteCards.size());
            for (Card c:
                 whiteCards) {
                builder.append(" ");
                builder.append(c.toSerializedString());
            }
            String cmd = builder.toString();
            for(Player p : players) {
                DatagramPacket packet = new DatagramPacket(cmd.getBytes(), cmd.getBytes().length, p.getIp(), LoginClient.SOCKET_PORT);
                tempSocket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getMe() {
        return me;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void selectWinner(WhiteCard winnerCard){
        winnerCard.getOwner().addPoints(1);
    }


    public void endRound(){
        clearBoard();
    }

    public void setMe(Player me) {
        this.me = me;
    }

    public int getGameState() {
        return gameState;
    }

    public void sendWhiteCards() {
        try {
            DatagramSocket tempSocket = new DatagramSocket();
            for (Player p:
                    players) {
//                if(!p.equals(me)) { TODO: Check if can send message to himself

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(MessageType.RETRIEVEWHITECARD.name());
                    stringBuilder.append(" ");
                    ArrayList<WhiteCard> cards = new ArrayList<>();
                    int numCards;
                    if(blackCard != null) numCards = blackCard.getPick();
                    else numCards = WHITECARDS_PER_PLAYER;
                    for (int i = 0; i < numCards; i++)
                        cards.add(whiteCardsDeck.pop());
                    stringBuilder.append(Integer.toString(cards.size()));
                    for (WhiteCard c: cards) {
                        stringBuilder.append(" ");
                        stringBuilder.append(c.toSerializedString());
                    }

                    String cmd = stringBuilder.toString();
                    byte[] buf = cmd.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, p.getIp(), LoginClient.SOCKET_PORT);
//                System.out.println(packet.getLength());
                    tempSocket.send(packet);
//                }
            }
            tempSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGameState(int gameState) {
        if(gameState == PLAYERS_PICKING) {
            //TODO: Update interface
            whiteCardPicks = new HashMap<>();
        }
        else if(gameState == PICK_WINNER) {
            //TODO: Update interface - reveal choices
        }
        else if(gameState == END_ROUND) {
            if(players.indexOf(me) == getLeaderIndex()) {
                sendWhiteCards();
            }
            blackCard = null;
            whiteCardPicks = new HashMap<>();
            czar++;
            if(players.indexOf(me) == getLeaderIndex())
                sendBlackCard();
        }
        PlayPanel.getInstance().refreshInterface();
        this.gameState = gameState;
    }

    public void sendBlackCard() {
        System.out.println("Sending black card...");
        BlackCard chosenCard = blackCardsdeck.pop();

        String cmd = MessageType.BLACKCARD.name();
        cmd += " ";

        try {
            cmd += chosenCard.toSerializedString();

            DatagramSocket tempSocket = new DatagramSocket();
            for (Player p:
                 players) {
//                if(!p.equals(me)) {
                    DatagramPacket packet = new DatagramPacket(cmd.getBytes(), cmd.getBytes().length, p.getIp(), LoginClient.SOCKET_PORT);
                    tempSocket.send(packet);
//                }
            }
            tempSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public Player getWhiteCardOwner(WhiteCard card) {
        for (Map.Entry<Player, ArrayList<WhiteCard>> entry : whiteCardPicks.entrySet()) {
            for (WhiteCard c:
                 entry.getValue()) {
                if(c.equals(card))
                    return entry.getKey();
            }
        }
        return null;
    }

    public boolean isCzar(Player p) {
        return players.indexOf(p) == czar;
    }
}
