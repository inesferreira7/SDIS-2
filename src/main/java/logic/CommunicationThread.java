package logic;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.sun.xml.internal.ws.api.model.MEP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by chrx on 5/26/17.
 */
public class CommunicationThread extends Thread{

    private GameLogic logic;
    private Player me;

    public CommunicationThread(GameLogic logic, Player me) {
        this.logic = logic;
        this.me = me;
    }

    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                me.getSocket().receive(packet);

                // figure out response
                String cmd = new String(packet.getData(), 0, packet.getLength());
                buf = processCommand(cmd).getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                me.getSocket().send(packet);
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
            if(cmdSplit.length == 3){
                int numberCards = Integer.parseInt(cmdSplit[1]);
                String blackcard = cmdSplit[2];
                logic.setBlackCard(new BlackCard(blackcard, numberCards));
                return MessageType.ACK.name() + " " + MessageType.BLACKCARD.name();
            }
        }
        else if(cmdSplit[0].equals(MessageType.WHITECARDPICK.name())){
            if(cmdSplit.length < 4){
                int sender = Integer.parseInt(cmdSplit[1]);
                int numberCards = Integer.parseInt(cmdSplit[2]);
                ArrayList<WhiteCard> res = new ArrayList<>();
                for (int j = 3; j < cmdSplit.length; j++){
                    res.add(new WhiteCard(cmdSplit[j], logic.getPlayers().get(sender)));
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
                me.addCard(new WhiteCard(cmdSplit[1], me));
                return MessageType.ACK.name() + " " + MessageType.RETRIEVEWHITECARD.name();
            }
        }
        else{
            return "error";
        }

        return "";
    }


}
