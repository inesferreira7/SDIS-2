package logic.login;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
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

    public ArrayList<Room> rooms;
    private Socket socket;
    private Player me;
    private Room myRoom;

    public static void main(String[] args){

        LoginClient rooms = new LoginClient();
        rooms.connectSocket();
        rooms.configSocketEvents();
    }

    public void connectSocket() {
        String ip = "https://sdis-cardsagainsthumanity.herokuapp.com";
//        String ip = "http://localhost:8001";
        try {
            myRoom = new Room("rooma");
            socket = IO.socket(ip);
            socket.connect();
            Thread.sleep(2000);
            JSONObject join = new JSONObject();
            join.put("room", "rooma");
            join.put("name", "testname");
            socket.emit("room", join);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
                    while(keys.hasNext()){
                        String key = keys.next();
                        JSONObject newRoom = (JSONObject)data.get(key);
                        String id = newRoom.getString("id");
                        JSONArray players = newRoom.getJSONArray("numPlayers");

                        int numberPlayers = players.length();
                        boolean gameStarted = newRoom.getBoolean("gameStarted");
                        boolean gameEnded = newRoom.getBoolean("gameEnded");
                        rooms.add(new Room(id,numberPlayers,gameStarted,gameEnded));
                    }
                } catch (JSONException ignored) {
                }

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
                        myRoom.addPlayer(new Player(id, name));
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

                    myRoom.addPlayer(new Player(id, name));
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

                    myRoom.removePlayer(new Player(id, ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
