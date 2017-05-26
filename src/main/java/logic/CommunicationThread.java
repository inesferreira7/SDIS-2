package logic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by chrx on 5/26/17.
 */
public class CommunicationThread extends Thread{

    private DatagramSocket socket;
    private GameLogic logic;

    public CommunicationThread(DatagramSocket socket, GameLogic logic) {
        this.socket = socket;
        this.logic = logic;
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
        return "";
    }


}
