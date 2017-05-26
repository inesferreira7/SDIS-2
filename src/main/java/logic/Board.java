package logic;

import parser.CardDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * Created by up201404990 on 25-05-2017.
 */

public class Board {
    private ArrayList<Player> players;
    private ArrayList<WhiteCard> whiteCards;
    private BlackCard blackCard;
    private Stack<WhiteCard> whiteCardsDeck;
    private Stack<BlackCard> blackCardsdeck;


    public Board(){
        players = new ArrayList<>();
        whiteCards = new ArrayList<>();
        whiteCardsDeck = new Stack<>();
        blackCardsdeck = new Stack<>();
    }

    public static void main(String[] args){
        Board b = new Board();
        b.createDecks();
    }


    public  void createDecks(){
        CardDatabase.parseCards();
        long seed = System.nanoTime();
        Collections.shuffle(CardDatabase.getWhiteCards(), new Random(seed));
        Collections.shuffle(CardDatabase.getBlackCards(), new Random(seed));
        whiteCardsDeck.addAll(CardDatabase.getWhiteCards());
        blackCardsdeck.addAll(CardDatabase.getBlackCards());
//        System.out.println(whiteCardsDeck.peek());
//        System.out.println(blackCardsdeck.peek());

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

    public void addWhiteCardToBoard(WhiteCard whiteCard) {
        whiteCards.add(whiteCard);
    }

    public void clearBoard(){
        whiteCards.clear();
        blackCard = null;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

}
