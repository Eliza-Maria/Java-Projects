import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRequests {
    private JList friendRequestsList;
    private JButton acceptButton;
    private JButton rejectButton;
    private JLabel chatZoneText;
    private JLabel friendRequestsText;
    private JPanel panel5;

    public static JFrame frame;

    DefaultListModel model;
    ListSelectionModel listSelectionModel;

    private String myUser; //email-ul va fi cheia de accesare

    private int numerotare=0;

    private Client client;

    public FriendRequests() {
        addListeners();


        model = new DefaultListModel();
        friendRequestsList.setModel(model);
        listSelectionModel = friendRequestsList.getSelectionModel();


        friendRequestsList.setVisible(true);

        showRequests();
        addListeners();
    }

    public FriendRequests(String myUser,Client client) {
        this.myUser = myUser;
        this.client=client;

        model = new DefaultListModel();
        friendRequestsList.setModel(model);
        listSelectionModel = friendRequestsList.getSelectionModel();


        friendRequestsList.setVisible(true);

        showRequests();
        addListeners();
    }

    public static void main(String[] args) {

    }


    public static void openFriendRequests(String myUser,Client client) {
        frame = new JFrame("FriendRequests");
        frame.setContentPane(new FriendRequests(myUser,client).panel5);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(300,500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void addListeners(){
        acceptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String select=(String)friendRequestsList.getSelectedValue();
                makeAccept(select);

            }
        });
        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String select=(String)friendRequestsList.getSelectedValue();
                makeReject(select);

            }
        });
    }

    public void makeAccept(String request){

        System.out.println("Accept: "+request);

        Client.insertFriend(request,myUser);
        Client.insertFriend(myUser,request);

        deleteFromDB(request,myUser);

        model.clear();
        showRequests();

        //refresh, ca sa apara noul prieten
        client.model.clear();
        client.showFriends();

    }
    public void makeReject(String request){

        System.out.println("Reject: "+request);

        deleteFromDB(request,myUser);

        model.clear();
        showRequests();
    }

    private void deleteFromDB(String requestFrom, String requestTo){
        String sql2 = "DELETE FROM `chatzone`.`friend_requests` WHERE friend_request_from=? AND friend_request_to=?";
        User.makeConnection();



        try {
            PreparedStatement st = User.con.prepareStatement(sql2);
            st.setString(1, requestFrom);
            st.setString(2, requestTo);
            int nr = st.executeUpdate();
            System.out.println(nr);

            User.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showRequests(){
        String sql3="SELECT * FROM `chatzone`.`friend_requests` WHERE friend_request_to=?";
        User.makeConnection();

        try {
            PreparedStatement st=User.con.prepareStatement(sql3);
            st.setString(1,myUser);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                model.add(numerotare,Client.getNume(rez.getString("friend_request_from")));
                numerotare++;


            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertRequest(String requestFrom, String requestTo){
        String sql2 = "INSERT INTO `chatzone`.`friend_requests` (friend_requests_ID,friend_request_from,friend_request_to) values (?,?,?)";
        User.makeConnection();

        int nextID=giveRequestID();


        try {
            PreparedStatement st = User.con.prepareStatement(sql2);
            st.setInt(1, nextID);
            st.setString(2, requestFrom);
            st.setString(3,requestTo);

            int nr = st.executeUpdate();
            System.out.println(nr);

            User.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int giveRequestID(){
        String sql4="SELECT friend_requests_ID FROM `chatzone`.`friend_requests` WHERE friend_requests_ID=(SELECT MAX(friend_requests_ID) FROM `chatzone`.`friend_requests`)";
        User.makeConnection();

        try{
            PreparedStatement st=User.con.prepareStatement(sql4);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                return (rez.getInt("friend_requests_ID")+1);
            }
            // con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public static void deleteRows(){
        String sql2 = "DELETE FROM `chatzone`.`friend_requests` ";
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
}
