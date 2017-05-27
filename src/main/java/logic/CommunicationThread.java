package logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by chrx on 5/26/17.
 */
public class CommunicationThread extends Thread{

    private GameLogic logic;
    private DatagramSocket socket;

    public CommunicationThread(GameLogic logic, DatagramSocket socket) {
        this.logic = logic;
        this.socket = socket;
    }

    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // figure out response
                String cmd = new String(packet.getData(), 0, packet.getLength());
                System.out.println(cmd);
                buf = processCommand(cmd).getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        socket.close();
    }

    private String  processCommand(String cmd){
        String[] cmdSplit= cmd.split(" ");
        if(cmdSplit[0].equals(MessageType.CZARCHANGE.name())){
            if(cmdSplit.length == 2){
                logic.changeCzar(Integer.parseInt(cmdSplit[1]));
                return MessageType.ACK.name() + " " + MessageType.CZARCHANGE.name();
            }
        }
        else if(cmdSplit[0].equals(MessageType.BLACKCARD.name())){
            if(cmdSplit.length == 2){
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(cmdSplit[1]));
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    BlackCard bc = (BlackCard) ois.readObject();
                    logic.setBlackCard(bc);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return MessageType.ACK.name() + " " + MessageType.BLACKCARD.name();
            }
        }
        else if(cmdSplit[0].equals(MessageType.WHITECARDPICK.name())){
            int noCards = Integer.parseInt(cmdSplit[1]);
            if(cmdSplit.length == 2 + noCards){
                ArrayList<WhiteCard> res = new ArrayList<>();
                try {
                    for (int j = 2; j < cmdSplit.length; j++){
                        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(cmdSplit[j]));
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        WhiteCard wc = (WhiteCard) ois.readObject();

                        int ownerIndex = logic.getPlayers().indexOf(wc.getOwner()); //the player that comes in the card owner
                        Player realOwner = logic.getPlayers().get(ownerIndex); //is a copy of the owner, here we are getting
                        wc.setOwner(realOwner); //the real object

                        res.add(wc);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                logic.addWhiteCardToBoard(res);
                return MessageType.ACK.name() + " " + MessageType.WHITECARDPICK.name();
            }
        }
        else if(cmdSplit[0].equals(MessageType.WINNERPICK.name())){
            if(cmdSplit.length == 2){
                logic.getPlayers().get(Integer.parseInt(cmdSplit[1])).addPoints(1);
                return MessageType.ACK.name() + " " + MessageType.WINNERPICK.name();
            }
        }
        else if(cmdSplit[0].equals(MessageType.RETRIEVEWHITECARD.name())){
            if(cmdSplit.length == 2){
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(cmdSplit[1]));
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    WhiteCard wc = (WhiteCard) ois.readObject();
                    wc.setOwner(logic.getMe());
                    logic.getMe().addCard(wc);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return MessageType.ACK.name() + " " + MessageType.RETRIEVEWHITECARD.name();
            }
        }
        else{
            return "error";
        }

        return "";
    }


}
