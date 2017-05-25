package logic;

import java.util.ArrayList;

public class Player{

    String name;
    ArrayList<WhiteCard> cards;
    int points;

    public Player(String name, int points){
        cards  = new ArrayList<>();
        this.name=name;
        this.points=points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getCards() {
        return cards;
    }

    public void addCard(WhiteCard card) {
        cards.add(card);
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points =+ points;
    }

    public void removeCard(WhiteCard card){
        cards.remove(card);
    }
}

