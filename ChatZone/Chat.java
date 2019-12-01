import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Chat implements Runnable{
    private JTextArea conversationText;
    private JTextField messageText;
    private JButton sendButton;
    private JButton attachButton;
    private JLabel chatZoneLabel;
    private JPanel panel3;
    private JLabel nameText;
    private JButton showConversationButton;

    public static JFrame frame;

    private String myUser; //email
    private String myFriend; //email
    private String myFriendName; //nume
    private String myName;

    String serverAddress="localhost";
    Scanner in;
    PrintWriter out;

    Socket socket = null;

    public Chat(String myUser, String myFriendName) {
        this.myUser = myUser;
        this.myFriendName = myFriendName;
        addListeners();
        nameText.setText(myFriendName);
        myFriend=getEmail(myFriendName);
        myName=Client.getNume(myUser);

       /* try {
            Socket socket = new Socket(serverAddress, 4444);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void openChat(){

        System.out.println("OPEN CHAT");

        frame = new JFrame("Chat");
        Chat chat=new Chat(myUser,myFriend);
        frame.setContentPane(chat.panel3);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(500,400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        showOldConversation(myUser,myFriend);

        /*System.out.println("RUN");
        chat.run();*/

    }

    public JTextArea getConversationText() {
        return conversationText;
    }

    public String getMyUser() {
        return myUser;
    }

    public String getMyFriend() {
        return myFriend;
    }

    /*public void run() {


        try {

            while (in.hasNextLine()) {
                var line = in.nextLine();

                String friendName = null;
                String myName = null;
                if (line.startsWith("SUBMITNAME")) {
                    myName = Client.getNume(myUser);
                    out.println(myName);
                    System.out.println("me: " + myName);
                } else if (line.startsWith("SUBMITFRIEND")) {
                    friendName = Client.getNume(myFriend);
                    out.println(friendName);
                    System.out.println("friend: " + friendName);

                } else if (line.startsWith("NOTIFICATION")) {
                    conversationText.append(line.substring(13) + "\n");

                } else if (line.startsWith("MESSAGE")) {
                    conversationText.append(line.substring(8) + "\n");
                    insertConversation(myFriend, myUser, line.substring((8)) + "\n");
                }
            }
        }
        finally {
            frame.setVisible(false);
            frame.dispose();
        }

    }*/

   public void run(){
       showOldConversation(myUser,myFriend);
   }

    private void addListeners(){
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeSend();
            }
        });
        attachButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeAttach();
            }
        });
        showConversationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                conversationText.setText("");
                showOldConversation(myUser,myFriend);
            }
        });
    }

    private void makeSend(){

        System.out.println("SEND");
        String message=messageText.getText();
       // System.out.println(message);

        insertConversation(myFriend,myUser,message);

        //out.println(message);
        messageText.setText("");
        conversationText.setText("");

        showOldConversation(myUser,myFriend);
    }

    private void makeAttach(){

    }


    public static String getEmail(String nume){

        String sql3="SELECT email FROM `chatzone`.`users` WHERE prenume=?";
        User.makeConnection();

        try {
            PreparedStatement st=User.con.prepareStatement(sql3);
            st.setString(1,nume);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                return rez.getString("email");

            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }




    public int giveID(){
        String sql4="SELECT conversation_ID FROM `chatzone`.`conversations` WHERE conversation_ID=(SELECT MAX(conversation_ID) FROM `chatzone`.`conversations`)";
        User.makeConnection();

        try{
            PreparedStatement st=User.con.prepareStatement(sql4);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                return (rez.getInt("conversation_ID")+1);
            }
            // con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public void insertConversation(String messageTo, String messageFrom, String message) {
        String sql2 = "INSERT INTO `chatzone`.`conversations` (conversation_id,message_from,message_to,message) values (?,?,?,?)";
        User.makeConnection();

        int nextID = giveID();


        try {
            PreparedStatement st = User.con.prepareStatement(sql2);
            st.setInt(1, nextID);
            st.setString(2, messageFrom);
            st.setString(3, messageTo);
            st.setString(4, message);

            int nr = st.executeUpdate();
            System.out.println(nr);

            User.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



    public void showOldConversation(String myUser,String myFriend){
        String sql3="SELECT message, conversation_id FROM `chatzone`.`conversations` WHERE message_from=? AND message_to=? ORDER BY conversation_id";
        ArrayList<Message> messages=new ArrayList<>();
        User.makeConnection();

        try {
            PreparedStatement st1=User.con.prepareStatement(sql3);
            st1.setString(1,myUser);
            st1.setString(2,myFriend);
            ResultSet rez=st1.executeQuery();
            while(rez.next()){

                //conversationText.append(Client.getNume(myUser)+": "+rez.getString("message"));
                String message=rez.getString("message");
                int id=rez.getInt("conversation_id");
                Message m=new Message(message,myFriend,myUser,id);
                messages.add(m);


                //System.out.println(rez.getString("message_from")+": "+rez.getString("message"));
            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement st2=User.con.prepareStatement(sql3);
            st2.setString(1,myFriend);
            st2.setString(2,myUser);
            ResultSet rez=st2.executeQuery();
            while(rez.next()){

                //conversationText.append(Client.getNume(myUser)+": "+rez.getString("message"));
                String message=rez.getString("message");
                int id=rez.getInt("conversation_id");
                Message m=new Message(message,myUser,myFriend,id);
                messages.add(m);


                //System.out.println(rez.getString("message_from")+": "+rez.getString("message"));
            }
            //con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        messages.sort(new Message());

        for(Message m:messages){
            conversationText.append(m.message_from+": "+m.message+"\n");
            System.out.println(m.message_from+": "+m.message+"\n");
        }

    }

    public static void deleteRows(){
        String sql2 = "DELETE FROM `chatzone`.`conversations` ";
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

    //TODO: gaseste o metoda de a pune un flag isOnline
}
