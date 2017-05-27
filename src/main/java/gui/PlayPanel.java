package gui;

import logic.GameLogic;
import logic.Player;
import logic.login.LoginClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by up201404990 on 26-05-2017.
 */
public class PlayPanel extends JFrame {

    private JPanel panel;
    private GameLogic logic;

    private static final int NUM_GRID = 28;

    private static PlayPanel instance = null;

    private ArrayList<JTextArea> cards;

    public static PlayPanel getInstance() {
        if(instance == null)
            instance = new PlayPanel();
        return instance;
    }

    public static boolean isInstanciated() {
        return instance != null;
    }

    public void initializeCards(){
        cards = new ArrayList<>();
        JTextArea card1 = new JTextArea("BlackCard");
        card1.setBackground(Color.black);
        card1.setForeground(Color.white);

        cards.add(card1);

        for(int i = 1; i< NUM_GRID; i++) {
            if(i == 6 || i == 7 || (i > 12 && i < 22) || i == 27){
                JTextArea temp = new JTextArea("");
                temp.setEditable(false);
                temp.setBackground(Color.black);
                cards.add(temp);
            }
            else
                cards.add(createVoidCard());
        }
    }

    public JTextArea createVoidCard(){
        JTextArea card = new JTextArea("empty text card");
        card.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        byte[] buf = "Gay".getBytes();
                        for (Player p : logic.getPlayers()) {
                            if(!p.equals(logic.getMe())){
                                System.out.println(p.getIp());
                                DatagramPacket dp = new DatagramPacket(buf, buf.length, p.getIp(), LoginClient.SOCKET_PORT);
                                socket.send(dp);
                            }
                        }
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        card.setLineWrap(true);
        card.setWrapStyleWord(true);
        card.setBackground(Color.white);
        card.setForeground(Color.black);
        card.setFont(new Font("Georgia", Font.BOLD, 14));
        card.setEditable(false);

        return card;
    }

    public JPanel getPanel(){
        return panel;
    }

    public void initializePanel(){
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        panel.setLayout(new GridLayout(4,7,20,15));
        panel.setBackground(Color.black);

        for(int i = 0; i < NUM_GRID; i++)
            panel.add(cards.get(i));

        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    public PlayPanel(){
        initializeCards();
        initializePanel();
        logic = GameLogic.getInstance();
    }

    public void main(String[] args){
        PlayPanel.getInstance();
    }

}
