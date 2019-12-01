import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {
    private JLabel chatZoneLabel;
    private JPanel panel4;
    private JTextField addFriendsText;
    private JButton addButton;
    private JList friendListText;
    private JRadioButton nameRadioButton;
    private JRadioButton emailRadioButton;
    private JTextField searchText;
    private JButton logOffButton;
    private JButton friendRequestsButton;
    private JLabel nameText;
    private JButton searchButton;

    public static JFrame frame;

    DefaultListModel model;
    ListSelectionModel listSelectionModel;

    private String myUser; //email-ul va fi cheia de accesare

    private int numerotare=0;



    public Client() {
        addListeners();


        model = new DefaultListModel();
        friendListText.setModel(model);
        listSelectionModel = friendListText.getSelectionModel();


        friendListText.setVisible(true);

        showFriends();

        nameText.setText(getNume(myUser));

    }

    public Client(String myUser) {
        this.myUser = myUser;
        addListeners();

        model = new DefaultListModel();
        friendListText.setModel(model);


        friendListText.setVisible(true);

        showFriends();

        nameText.setText(getNume(myUser));
    }

    public static void main(String[] args) throws SQLException {


    }

    public static void openCleint(String email) throws SQLException {
        frame = new JFrame("Client");
        frame.setContentPane(new Client(email).panel4);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(300,500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        System.out.println("OPEN CLIENT");

        //buildProfile();
    }

    private void addListeners(){
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeAdd();

            }
        });
        logOffButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeLogOff();

            }
        });
        nameRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emailRadioButton.setSelected(false);
            }
        });
        emailRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nameRadioButton.setSelected(false);;

            }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nameRadioButton.isSelected())
                    searchByName();
                else
                    searchByEmail();

            }
        });
        friendRequestsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeFriendRequests();

            }
        });



        friendListText.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    String friendSelected=(String)friendListText.getModel().getElementAt(index);
                    //Chat.openChat(myUser,friendSelected);


                    Chat chat=new Chat(myUser,friendSelected);
                    chat.openChat();
                   /*Thread thread=new Thread(chat);
                    thread.start();*/
                    chat.run();


                    System.out.println(friendSelected);
                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = list.locationToIndex(evt.getPoint());
                }
            }
        });
    }


    private void makeAdd(){
        System.out.println("ADD");

        String newFriend=addFriendsText.getText();


        if(!existaFriend(newFriend,myUser)){

            if(existaFriendAutentificat(newFriend)) {
                //model.add(numerotare, getNume(newFriend));
                //insertFriend(newFriend);
                FriendRequests.insertRequest(myUser,newFriend);
            }
            else
                JOptionPane.showMessageDialog(frame, "There is no such user!","ChatZone", JOptionPane.PLAIN_MESSAGE);

        }
        else
            JOptionPane.showMessageDialog(frame, "Friend has already been added!","ChatZone", JOptionPane.PLAIN_MESSAGE);


        addFriendsText.setText("");

        /*friends.add(newFriend);
        for(int i=0;i<friends.size();i++)
            model.add(i,friends.get(i));*/
    }

    private void makeFriendRequests(){
        FriendRequests.openFriendRequests(myUser,this);
    }

    private void makeLogOff(){
        System.out.println("LOG OFF");

        LogIn.openLogIn();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));


    }

    private void searchByName(){
        System.out.println(searchText.getText()+" by name");
        model.clear();

        String nameToSearch=searchText.getText();
        model.add(0,nameToSearch);

        if(nameToSearch.equals("")){
            model.clear();
            numerotare=0;
            showFriends();
        }

    }

    private void searchByEmail(){
        System.out.println(searchText.getText()+" by email");

        System.out.println(searchText.getText()+" by name");
        model.clear();

        String emailToSearch=searchText.getText();
        model.add(0,getNume(emailToSearch));

        if(emailToSearch.equals("")){
            model.clear();
            numerotare=0;
            showFriends();
        }
    }

    //SQL functions
    //
    //


    public void showFriends(){
        String sql3="SELECT * FROM `chatzone`.`friends` WHERE user_email=?";
        User.makeConnection();
        numerotare=0;

        try {
            PreparedStatement st=User.con.prepareStatement(sql3);
            st.setString(1,myUser);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                model.add(numerotare,getNume(rez.getString("friend_email")));
                numerotare++;


            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean existaFriend(String friendEmail,String myUser){
        String sql3="SELECT * FROM `chatzone`.`friends` WHERE friend_email=? AND user_email=?";
        User.makeConnection();

        try {
            PreparedStatement st=User.con.prepareStatement(sql3);
            st.setString(1,friendEmail);
            st.setString(2,myUser);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                if(rez.getString("friend_email").equals(friendEmail)&&rez.getString("user_email").equals(myUser))

                {
                    return true;
                }
            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean existaFriendAutentificat(String friendEmail){
        String sql3="SELECT * FROM `chatzone`.`users` WHERE email=?";
        User.makeConnection();

        try {
            PreparedStatement st=User.con.prepareStatement(sql3);
            st.setString(1,friendEmail);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                if(rez.getString("email").equals(friendEmail))

                {
                    return true;
                }
            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public static void insertFriend(String friendEmail, String myEmail){
        String sql2 = "INSERT INTO `chatzone`.`friends` (user_email,friend_email,friendship_id) values (?,?,?)";
        User.makeConnection();

        int nextID=giveID();


            try {
                PreparedStatement st = User.con.prepareStatement(sql2);
                st.setString(1, myEmail);
                st.setString(2, friendEmail);
                st.setInt(3,nextID);

                int nr = st.executeUpdate();
                System.out.println(nr);

                User.con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }

    public static String getNume(String email){

        String sql3="SELECT prenume FROM `chatzone`.`users` WHERE email=?";
        User.makeConnection();

        try {
            PreparedStatement st=User.con.prepareStatement(sql3);
            st.setString(1,email);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                return rez.getString("prenume");

            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void deleteRows(){
        String sql2 = "DELETE FROM `chatzone`.`friends` ";
        User.makeConnection();


                try {
                    PreparedStatement st = User.con.prepareStatement(sql2);

                    int nr = st.executeUpdate();
                    System.out.println(nr);

                    User.con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
    }

    public static int giveID(){
        String sql4="SELECT friendship_ID FROM `chatzone`.`friends` WHERE friendship_ID=(SELECT MAX(friendship_ID) FROM `chatzone`.`friends`)";
        User.makeConnection();

        try{
            PreparedStatement st=User.con.prepareStatement(sql4);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                return (rez.getInt("friendship_ID")+1);
            }
            // con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }
}
