package logic;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by up201404990 on 25-05-2017.
 */

public class Board {
    ArrayList<Player> players;
    ArrayList<WhiteCard> whiteCards;
    BlackCard blackCard;
    Stack<WhiteCard> whiteCardsDeck;
    Stack<BlackCard> blackCardsdeck;



    public void Board(){
        players = new ArrayList<>();
        whiteCards = new ArrayList<>();
        whiteCardsDeck = new Stack<>();
        blackCardsdeck = new Stack<>();
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
}
