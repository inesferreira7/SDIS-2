package logic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chrx on 5/26/17.
 */
public class CommunicationThread extends Thread {

    private GameLogic logic;
    private DatagramSocket socket;

    private static CommunicationThread instance = null;

    private HashSet<String> receivedCommands;

    public static CommunicationThread getInstance() {
        return instance;
    }

    public CommunicationThread(GameLogic logic, DatagramSocket socket) {
        this.logic = logic;
        this.socket = socket;
        this.receivedCommands = new HashSet<String>();
        instance = this;
    }

    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[2048];

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

    public String processCommand(String cmd) {
        String[] cmdSplit = cmd.split(" ");
        if(receivedCommands.contains(cmd))
            return MessageType.ACK.name() + " " + cmdSplit[0];
        receivedCommands.add(cmd);

        if (cmdSplit[0].equals(MessageType.BLACKCARD.name())) {
            if (cmdSplit.length == 2) {
                BlackCard bc = (BlackCard) Card.getFromSerializedString(cmdSplit[1]);
                logic.setBlackCard(bc);
                logic.setGameState(GameLogic.PLAYERS_PICKING);
                return MessageType.ACK.name() + " " + MessageType.BLACKCARD.name();
            }
        } else if (cmdSplit[0].equals(MessageType.WHITECARDPICK.name())) {
            if(cmdSplit.length > 2) {
                int noCards = Integer.parseInt(cmdSplit[1]);
                if (cmdSplit.length == 2 + noCards) {
                    ArrayList<WhiteCard> res = new ArrayList<>();

                    for (int j = 2; j < cmdSplit.length; j++) {
                        WhiteCard wc = (WhiteCard) Card.getFromSerializedString(cmdSplit[j]);

                        int ownerIndex = logic.getPlayers().indexOf(wc.getOwner()); //the player that comes in the card owner
                        Player realOwner = logic.getPlayers().get(ownerIndex); //is a copy of the owner, here we are getting
                        wc.setOwner(realOwner); //the real object

                        res.add(wc);
                    }

                    logic.addWhiteCardsToBoard(res);
                    if (logic.allPlayersPicked())
                        logic.setGameState(GameLogic.PICK_WINNER);
                    return MessageType.ACK.name() + " " + MessageType.WHITECARDPICK.name();
                }
            }
        } else if (cmdSplit[0].equals(MessageType.WINNERPICK.name())) {
            if (cmdSplit.length == 3) {
                logic.getPlayers().get(Integer.parseInt(cmdSplit[1])).addPoints(1);
                logic.setGameState(GameLogic.END_ROUND);
                return MessageType.ACK.name() + " " + MessageType.WINNERPICK.name();
            }
        } else if (cmdSplit[0].equals(MessageType.RETRIEVEWHITECARD.name())) {
            if (cmdSplit.length > 2) {
                int numCards = Integer.parseInt(cmdSplit[1]);
                if(!logic.isCzar(logic.getMe())) {
                    if (cmdSplit.length == 2 + numCards) {
                        for (int i = 2; i < cmdSplit.length; i++) {
                            WhiteCard wc = (WhiteCard) Card.getFromSerializedString(cmdSplit[i]);
                            wc.setOwner(logic.getMe());
                            logic.getMe().addCard(wc);
                        }
                    }
                }
//                System.out.println(logic.getMe());

                logic.prepareNewRound();
                if(numCards == 5 && logic.getLeaderIndex() == logic.getPlayers().indexOf(logic.getMe()))
                    logic.sendBlackCard();
                return MessageType.ACK.name() + " " + MessageType.RETRIEVEWHITECARD.name();
            }
        } else {
            return "error";
        }

        return "";
    }


}
