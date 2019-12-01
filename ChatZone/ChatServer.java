import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;


public class ChatServer {



    private static Map<String,PrintWriter> users = new HashMap<>();
    private static Map<String,String> conversations = new HashMap<>();


    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        var pool = Executors.newFixedThreadPool(5000);
        try (var listener = new ServerSocket(4444)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }


    /*private static final int portNumber = 59001;

    private int serverPort;
    private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"

    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients(){
        return clients;
    }

    public void startServer(){
        //clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                Handler client = new Handler(socket);
                Thread thread = new Thread(client);
                thread.start();
                //clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }*/

    private static class Handler implements Runnable {
        private String myName;
        private String friendName;

        private Socket socket;
        private Scanner in;
        private PrintWriter out;


        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {

                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    myName = in.nextLine();
                    if (myName == null) {
                        return;
                    }
                    if (!myName.isBlank()) {
                        break;
                    }

                }
                while (true) {
                    out.println("SUBMITFRIEND");
                    friendName = in.nextLine();
                    if (friendName == null) {
                        return;
                    }
                    if (!friendName.isBlank() && friendName != myName) {
                        break;
                    }
                }


                conversations.put(myName,friendName);

                if(!users.containsKey(friendName)) {
                    users.put(friendName, out);
                }

                users.get(friendName).println("NOTIFICATION " + myName + " is online");

                users.put(myName,out);


                while (true) {
                    System.out.println("SERVER");

                    String input = in.nextLine();
                    System.out.println(input);

                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }



                    users.get(myName).println("MESSAGE " + myName + ": " + input);
                    if(conversations.containsKey(friendName))
                        users.get(friendName).println("MESSAGE " + myName + ": " + input);



                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    users.remove(myName);
                }
                if (myName != null) {
                    System.out.println(myName + " is leaving");
                    users.get(friendName).println("MESSAGE " + myName + " has left");
                    users.remove(myName);
                }
                try { socket.close(); } catch (IOException e) {}
            }
        }
    }
}