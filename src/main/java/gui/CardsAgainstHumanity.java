package gui;

import logic.Card;
import logic.login.LoginClient;
import logic.login.Room;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private JButton startGame;
    private JList displayRooms;
    private String clientName;

    JScrollPane listScroller;


    private LoginClient client;

    private ArrayList<Room> rooms;

    private String[] roomList;

    private static CardsAgainstHumanity instance = null;

    public static CardsAgainstHumanity getInstance() {
        if(instance == null)
            instance = new CardsAgainstHumanity();
        return instance;
    }

    public static void main(String[] args){
        CardsAgainstHumanity.getInstance();
    }

    private CardsAgainstHumanity(){
        this.client = new LoginClient();



        this.setSize(800,550);

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

        rightRoomPanel = new JPanel(new GridLayout(7, 3, 0, 50));
        rightRoomPanel.setBackground(Color.black);
        leftRoomPanel = new JPanel();
        leftRoomPanel.setBackground(Color.black);
        leftRoomPanel.setLayout(new BoxLayout(leftRoomPanel,BoxLayout.X_AXIS));

        roomPanel.add(leftRoomPanel);
        roomPanel.add(rightRoomPanel, BorderLayout.EAST);

        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));


        createRoom = new JButton("Create Room");
        joinRoom = new JButton("Join Room");
        refresh = new JButton("Refresh");
        startGame = new JButton("Start Game");

        //rightRoomPanel.add(Box.createRigidArea(new Dimension(0,150)));
        rightRoomPanel.add(refresh);
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        //rightRoomPanel.add(Box.createRigidArea(new Dimension(0,80)));
        rightRoomPanel.add(createRoom);
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        //rightRoomPanel.add(Box.createRigidArea(new Dimension(0,80)));
        rightRoomPanel.add(joinRoom);
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(startGame);
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));
        rightRoomPanel.add(new JLabel(""));

        leftRoomPanel.add(Box.createRigidArea(new Dimension(100,900)));

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(textField1.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Insert valid name");
                    return;
                }
                clientName = textField1.getText();
                changePanel(thePanel,roomPanel);
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                client.sendGetRooms();
            }
        });

        createRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String roomName = JOptionPane.showInputDialog(null,"Room name");
                client.joinRoom(clientName, roomName);
            }
        });

        joinRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(displayRooms.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Select the room you want to join");
                    return;
                }
                String temp = displayRooms.getSelectedValue().toString();
                String[] splited = temp.split(" ");
                String roomName = splited[1];
                client.joinRoom(clientName, roomName);
            }
        });
    }

    public void changePanel(JPanel oldPanel, JPanel newPanel){
        this.remove(oldPanel);
        this.setContentPane(newPanel);
        this.validate();
        this.repaint();
    }

    public void refreshRooms() {
        this.rooms = client.getRooms();
        System.out.println("rooms:" + rooms);
        roomList = new String[rooms.size()];
        createData(this.rooms);

        displayRooms = new JList(roomList);
        displayRooms.setFont( displayRooms.getFont().deriveFont(Font.PLAIN) );
        if(listScroller != null)
            leftRoomPanel.remove(listScroller);
        listScroller = new JScrollPane(displayRooms);
        listScroller.setMaximumSize(new Dimension(250,300));
        listScroller.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftRoomPanel.add(listScroller);
        this.validate();
        this.repaint();
    }

    public String[] createData(ArrayList<Room> rooms){

        for(int i = 0; i < rooms.size(); i++){
            String text = rooms.get(i).getId() + " " + rooms.get(i).getNumPlayers() + "/6";
            if(rooms.get(i).equals(client.getMyRoom()))
                text = "<html><b>" + text + "</b></html>";
            roomList[i] = text;
        }
//        System.out.println(roomList[0]);
        return roomList;
    }

}
