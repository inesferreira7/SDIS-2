package logic;

import java.net.*;
import java.util.ArrayList;

public class Player{

    private String id;
    private String name;
    private ArrayList<WhiteCard> cards;
    private int position;
    private int points;
    private InetAddress ip;

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

    public InetAddress getIp() {
        return ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);
    }

}

