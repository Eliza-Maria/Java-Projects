
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ClientThread implements Runnable {

    private Socket socket;

    String myUser;
    String myFriend;
    JTextArea conversationText;

    Scanner in;
    PrintWriter out;

   /* public ClientThread(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    public ClientThread(Chat chat) throws IOException {
        this.myUser = chat.getMyUser();
        this.myFriend = chat.getMyFriend();
        this.conversationText = chat.getConversationText();

        socket=new Socket("localhost",59001);

        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
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
}
