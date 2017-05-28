package gui;

import logic.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Joao on 28/05/2017.
 */
public class EndPanel extends JFrame {

    private ArrayList<Player> players;

    private static EndPanel instance;

    private JPanel panel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextArea winner;
    private JTextArea points;


    public EndPanel(ArrayList<Player> players){
        this.setSize(800,550);

        this.players = players;
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o2.getPoints() - o1.getPoints();
            }
        });

        String winner_name = players.get(0).getName();

        winner = new JTextArea("WINNER:\n    " + winner_name);
        winner.setEditable(false);
        winner.setFont(new Font("Georgia", Font.BOLD, 18));
        winner.setBackground(Color.black);
        winner.setForeground(Color.white);

        StringBuilder sb = new StringBuilder();
        sb.append("Points: \n\n");
        for(int i = 0; i < players.size(); i++){
            sb.append(i + 1);
            sb.append(". ");
            sb.append(players.get(i).getName());
            sb.append(":   ");
            sb.append(players.get(i).getPoints());
            sb.append("\n");
        }

        points = new JTextArea(sb.toString());
        points.setEditable(false);
        points.setFont(new Font("Georgia", Font.PLAIN, 16));
        points.setBackground(Color.black);
        points.setForeground(Color.white);


        panel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 2, 10, 10));

        rightPanel.add(new JLabel(""));
        rightPanel.add(new JLabel(""));

        rightPanel.add(winner);
        rightPanel.add(points);

        rightPanel.add(new JLabel(""));
        rightPanel.add(new JLabel(""));

        panel.add( leftPanel, BorderLayout.WEST );
        panel.add(rightPanel);

        leftPanel.setBackground(Color.black);
        rightPanel.setBackground(Color.black);

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        leftPanel.add(picLabel, BorderLayout.CENTER);

        this.add(panel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getPanel() {
        return panel;
    }

    public static EndPanel getInstance(ArrayList<Player> players) {
        if (instance == null)
            instance = new EndPanel(players);
        return instance;
    }


    public static void main(String[] args){
        //EndPanel.getInstance(p).setVisible(true);
    }
}
