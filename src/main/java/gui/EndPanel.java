package gui;

import logic.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Joao on 28/05/2017.
 */
public class EndPanel extends JFrame {

    private ArrayList<Player> players;

    private static EndPanel instance;

    private JPanel panel;
    private JPanel leftPanel;
    private JPanel rightPanel;

    public EndPanel(){
        this.setSize(800,550);

        panel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 2, 20, 20));

        JTextArea winner = new JTextArea("WINNER:\n    player1");
        winner.setEditable(false);
        winner.setFont(new Font("Georgia", Font.BOLD, 18));
        winner.setBackground(Color.black);
        winner.setForeground(Color.white);

        JTextArea points = new JTextArea("Points:\n\nplayer1: 5\nplayer2: 4\nplayer3: 4\nplayer4: 3\nplayer5: 1\nplayer6: 0\n");
        points.setEditable(false);
        points.setFont(new Font("Georgia", Font.BOLD, 14));
        points.setBackground(Color.black);
        points.setForeground(Color.white);

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
        this.setVisible(true);

    }


    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public JPanel getPanel() {
        return panel;
    }

    public static EndPanel getInstance() {
        if (instance == null)
            instance = new EndPanel();
        return instance;
    }

    public static void main(String[] args){
        EndPanel.getInstance().setVisible(true);
    }
}
