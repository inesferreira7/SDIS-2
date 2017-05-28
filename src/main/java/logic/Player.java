package logic;

import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;

public class Player implements Serializable{

    private String id;
    private String name;
    private transient ArrayList<WhiteCard> cards;
    private transient int position;
    private transient int points;
    private transient InetAddress ip;

    public Player(String id, String name){
        this.id=id;
        this.name=name;

        this.points=0;
        this.position=-1;
        this.cards=new ArrayList<>();
    }

    public void setIp(String ip) {
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<WhiteCard> getCards() {
        return cards;
    }

    public void addCard(WhiteCard card) {
        cards.add(card);
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void removeCards(ArrayList<WhiteCard> toRemoveCards){
        for (Card c:
             toRemoveCards) {
            cards.remove(c);
        }
    }

    public InetAddress getIp() {
        return ip;
    }

    public WhiteCard getCardWithText(String text){
        for (WhiteCard c:
             cards) {
            if(c.getText().equals(text)) return c;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cards=" + cards +
                ", position=" + position +
                ", points=" + points +
                ", ip=" + ip +
                '}';
    }
}

