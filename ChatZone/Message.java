import java.util.Comparator;

public class Message implements Comparator {
    public String message;
    public String message_to;
    public String message_from;
    public int id;

    public Message() {
    }

    public Message(String message, String message_to, String message_from, int id) {
        this.message = message;
        this.message_to = message_to;
        this.message_from = message_from;
        this.id = id;
    }

    public int compare(Object ob1, Object ob2){
        Message m1=(Message)ob1;
        Message m2=(Message)ob2;
        return m1.id-m2.id;
    }
}
