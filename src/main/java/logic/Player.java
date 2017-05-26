package logic;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Player{

    private final int SOCKET_PORT = 4123;

    private String id;
    private String name;
    private ArrayList<WhiteCard> cards;
    private int position;
    private int points;
    private DatagramSocket socket;

    public Player(String id, String name){
        this.id=id;
        this.name=name;

        this.points=0;
        this.position=-1;
        this.cards=new ArrayList<>();
    }

    public void createSocket(String address) {
        try {
            socket = new DatagramSocket(SOCKET_PORT);
        } catch (IOException e) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}

