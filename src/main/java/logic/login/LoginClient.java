package logic.login;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
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

    public static void main(String[] args){

        LoginClient rooms = new LoginClient();
        rooms.connectSocket();
        rooms.configSocketEvents();
    }

    public void connectSocket() {
        String ip = "https://sdis-cardsagainsthumanity.herokuapp.com";
        try {
            socket = IO.socket(ip);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            @Override
            public void call(Object... args) {
            }
        }).on("getRooms", new Emitter.Listener() {
//            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    Iterator<String> keys = data.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        JSONObject newRoom = (JSONObject)data.get(key);
                        String id = newRoom.getString("id");
                        JSONArray players = newRoom.getJSONArray("players");

                        int numberPlayers = players.length();
                        boolean gameStarted = newRoom.getBoolean("gameStarted");
                        boolean gameEnded = newRoom.getBoolean("gameEnded");
                        rooms.add(new Room(id,numberPlayers,gameStarted,gameEnded));
                    }
                } catch (JSONException ignored) {
                }

            }
        });
    }
}
