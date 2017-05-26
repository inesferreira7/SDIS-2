package logic.login;

import gui.CardsAgainstHumanity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import logic.Card;
import logic.CommunicationThread;
import logic.GameLogic;
import logic.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ines on 25-05-2017.
 */
public class LoginClient {

    private final String myIP = "172.30.27.172";

    private ArrayList<Room> rooms;
    private Socket socket;
    private Player me;
    private String myRoom;

    public LoginClient() {
        rooms = new ArrayList<>();
        connectSocket();
        configSocketEvents();
    }

    public void connectSocket() {
        String ip = "https://sdis-cardsagainsthumanity.herokuapp.com";
//        String ip = "http://localhost:8001";
        try {
//            myRoom = new Room("rooma");
            socket = IO.socket(ip);
            socket.connect();
//            Thread.sleep(2000);
//            JSONObject join = new JSONObject();
//            join.put("room", "rooma");
//            join.put("name", "testname");
//            socket.emit("room", join);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        }).on("getRooms", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    Iterator<String> keys = data.keys();
                    rooms = new ArrayList<Room>();
                    while(keys.hasNext()){
                        String key = keys.next();
                        JSONObject newRoom = (JSONObject)data.get(key);
                        String id = newRoom.getString("id");
                        JSONArray players = newRoom.getJSONArray("players");

                        int numberPlayers = players.length();
                        boolean gameStarted = newRoom.getBoolean("gameStarted");
                        boolean gameEnded = newRoom.getBoolean("gameEnded");

                        Room temp = new Room(id,numberPlayers,gameStarted,gameEnded);
                        if(temp.getId().equals(myRoom))
                            temp.setPlayers(players);
                        rooms.add(temp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(CardsAgainstHumanity.isInstanciated())
                    CardsAgainstHumanity.getInstance().refreshRooms();

            }
        }).on("youAre", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];

                try {
                    String id = (String) data.get("id");
                    String name = (String) data.get("name");

                    me = new Player(id, name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray data = (JSONArray) objects[0];

                try {
                    for(int i = 0; i < data.length(); i++) {
                        String id = (String) ((JSONObject) data.get(i)).get("id");
                        String name = (String) ((JSONObject) data.get(i)).get("name");
                        getMyRoom().addPlayer(new Player(id, name));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];

                try {
                    String id = (String) data.get("id");
                    String name = (String) data.get("name");

                    getMyRoom().addPlayer(new Player(id, name));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0];

                try {
                    String id = (String) data.get("id");

                    getMyRoom().removePlayer(new Player(id, ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("setPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray data = (JSONArray) objects[0];

                getMyRoom().setPlayers(data, true);
                new CommunicationThread(GameLogic.getInstance(), me).run();
            }
        });
    }

    public void sendStartGame() {
        socket.emit("startGame");
    }

    public void sendGetRooms() {
        socket.emit("getRooms");
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public Room getMyRoom(){
        for (Room r:
             rooms) {
            if(r.getId().equals(myRoom))
                return r;
        }
        return null;
    }

    public static void main(String[] args){

        LoginClient rooms = new LoginClient();
        rooms.connectSocket();
        rooms.configSocketEvents();
    }

    public void joinRoom(String name, String roomName){
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("room", roomName);
            json.put("ip", myIP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        myRoom = roomName;
        if(!rooms.contains(new Room(myRoom)))
            rooms.add(new Room(myRoom));

        socket.emit("room", json);
        socket.emit("getRooms");

    }

    public Player getMe() {
        return me;
    }
}
