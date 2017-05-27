package logic;

import logic.login.LoginClient;
import parser.CardDatabase;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

    public void pickBlackCard(){
        blackCard = blackCardsdeck.peek();
        blackCardsdeck.pop();
    }


    public void dealWhiteCards(int whiteCardsPerPlayer){
        for(int i = 0; i < players.size();i++){
            for(int j = 0; j < whiteCardsPerPlayer ; j++){
                WhiteCard topDeckCard = whiteCardsDeck.peek();
                players.get(i).addCard(topDeckCard);
                whiteCardsDeck.pop();
            }
        }
    }

    public boolean allPlayersPicked() {
        return whiteCardPicks.size() == players.size();
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

    public void makePlay(){

        pickBlackCard();
        dealWhiteCards(WHITECARDS_PER_PLAYER);

        if(gameState == PLAYERS_PICKING){
            for(int i = 0; i < players.size(); i++){
                //playerPickedCard(currentPlayer ,wCard); FIXME
            }
        }

        if(gameState == PICK_WINNER){
            selectWinner(wCard);
        }

        if(gameState == END_ROUND){
            endRound();
        }
    }

    /*public void playerPickedCard(Player player, WhiteCard whiteCard){ FIXME
        player.removeCard(whiteCard);
        addWhiteCardsToBoard(whiteCard);
    }*/

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

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public void sendBlackCard() {
        System.out.println("Sending black card...");
        BlackCard chosenCard = blackCardsdeck.pop();

        String cmd = MessageType.BLACKCARD.name();
        cmd += " ";

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(chosenCard);
            oos.close();
            cmd += Base64.getEncoder().encodeToString(baos.toByteArray());
            baos.close();

            DatagramSocket tempSocket = new DatagramSocket();
            for (Player p:
                 players) {
                if(!p.equals(me)) {
                    DatagramPacket packet = new DatagramPacket(cmd.getBytes(), cmd.getBytes().length, p.getIp(), LoginClient.SOCKET_PORT);
                    tempSocket.send(packet);
                }
            }
            tempSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
