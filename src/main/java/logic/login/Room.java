package logic.login;

import logic.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Room{

    private String id;
    private boolean gameStarted, gameEnded;
    private ArrayList<Player> players;
    private int numPlayers;

    public Room(String id) {
        this(id, 0, false, false);
    }

    public Room(String id, int players, boolean gameStarted, boolean gameEnded) {

        this.id = id;
        this.numPlayers = players;
        this.players = new ArrayList<>();
        this.gameStarted = gameStarted;
        this.gameEnded = gameEnded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public void setPlayers(JSONArray playersArray) {
        setPlayers(playersArray, false);
    }

    public void setPlayers(JSONArray playersArray, boolean detailed) {
        for(int i = 0; i < playersArray.length(); i++){
            try {
                JSONObject player = (JSONObject) (playersArray.get(i));
                String id = player.getString("id");
                String name = player.getString("name");

                Player p = new Player(id, name);

                if(detailed){
                    int position = player.getInt("position");
                    String ip = player.getString("ip");

                    p.setPosition(position);
                    p.setIp(ip);
                }

                players.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Player getOwner() {
        if(this.players == null || this.players.isEmpty()) return null;
        return this.players.get(0);
    }

    public void addPlayer(Player p) {
        if(!players.contains(p))
            this.players.add(p);
        this.numPlayers = players.size();
    }
    
    public void removePlayer(Player p) {
        for(int i = 0; i < this.players.size(); i++)
            if(players.get(i).equals(p)) players.remove(i);
        this.numPlayers = players.size();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        return id.equals(room.id);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", gameStarted=" + gameStarted +
                ", gameEnded=" + gameEnded +
                ", players=" + players +
                ", numPlayers=" + numPlayers +
                '}';
    }
}