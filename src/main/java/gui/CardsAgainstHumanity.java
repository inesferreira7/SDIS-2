package gui;

import logic.login.LoginClient;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ines on 25-05-2017.
 */
public class CardsAgainstHumanity extends JFrame{

    private JButton button1;
    private JTextField textField1;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel leftRoomPanel;
    private JPanel rightRoomPanel;
    private JButton createRoom;
    private JButton joinRoom;
    private JButton refresh;

    private LoginClient client;


    public static void main(String[] args){
        new CardsAgainstHumanity();
    }

    public CardsAgainstHumanity(){
        this.client = new LoginClient();

        this.setSize(900,550);

        Toolkit tk = Toolkit.getDefaultToolkit();

        Dimension dim = tk.getScreenSize();

        int xPos = (dim.width / 2) - (this.getWidth() / 2);
        int yPos = (dim.height / 2) - (this.getHeight() / 2);

        this.setLocation(xPos,yPos);

        this.setResizable(false);

        //terminate process
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Cards Against Humanity");

        //MENU INICIAL

        JPanel thePanel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        thePanel.add( leftPanel, BorderLayout.WEST );
        thePanel.add(rightPanel);

        leftPanel.setBackground(Color.black);
        rightPanel.setBackground(Color.black);
        leftPanel.setBackground(Color.black);

        JLabel label1 = new JLabel("Enter your name");
        label1.setFont(new Font("Georgia", Font.PLAIN, 20));
        label1.setForeground(Color.white);
        rightPanel.add(Box.createRigidArea(new Dimension(0,200)));
        rightPanel.add(label1);
        rightPanel.add(Box.createRigidArea(new Dimension(0,40)));

        textField1 = new JTextField();
        textField1.setMaximumSize(new Dimension(500, textField1.getPreferredSize().height + 100));
        textField1.setFont(new Font("Georgia", Font.PLAIN, 20));
        rightPanel.add(textField1);
        rightPanel.add(Box.createRigidArea(new Dimension(0,70)));

        button1 = new JButton("Continue");
        rightPanel.add(button1);
        rightPanel.add(Box.createRigidArea(new Dimension(0,150)));

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        leftPanel.add(picLabel, BorderLayout.CENTER);

        this.add(thePanel);

        this.setVisible(true);

        textField1.requestFocus();

        //ENTER ROOM MENU

        JPanel roomPanel = new JPanel(new BorderLayout());

        rightRoomPanel = new JPanel(new BorderLayout());
        rightRoomPanel.setBackground(Color.black);
        leftRoomPanel = new JPanel();
        leftRoomPanel.setBackground(Color.black);
        rightRoomPanel.setLayout(new BoxLayout(rightRoomPanel,BoxLayout.Y_AXIS));

        roomPanel.add(leftRoomPanel);
        roomPanel.add(rightRoomPanel, BorderLayout.EAST);


        createRoom = new JButton("Create Room");
        joinRoom = new JButton("Join Room");
        refresh = new JButton("Refresh");

        rightRoomPanel.add(Box.createRigidArea(new Dimension(0,150)));
        rightRoomPanel.add(createRoom);
        rightRoomPanel.add(Box.createRigidArea(new Dimension(0,80)));
        rightRoomPanel.add(joinRoom);
        rightRoomPanel.add(Box.createRigidArea(new Dimension(0,80)));
        rightRoomPanel.add(refresh);


        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.print("OI");
                changePanel(thePanel,roomPanel);
            }
        });

    }

    public void changePanel(JPanel oldPanel, JPanel newPanel){
        this.remove(oldPanel);
        this.setContentPane(newPanel);
        this.validate();
        this.repaint();
    }

}
