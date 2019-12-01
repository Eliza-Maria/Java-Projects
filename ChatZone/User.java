import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class User {

    private int ID;
    private String nume;
    private String prenume;
    private String gen;
    private String data_nasterii;
    private String email;
    private String parola;

    public static Connection con = null;
    static String databaseName="chatzone";
    static String url="jdbc:mysql://127.0.0.1:3306/"+databaseName+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static String username="root";
    static String password="tiMMy?2019";


    public User() {
        int nextID=giveID();
        if(nextID!=0)
            setID(nextID);
        this.ID=nextID;
    }

    public User( String nume, String prenume, String gen, String data_nasterii, String email, String parola) {

        int nextID=giveID();
        if(nextID!=0)
            setID(nextID);
        this.ID=nextID;
        this.nume = nume;
        this.prenume = prenume;
        this.gen = gen;
        this.data_nasterii = data_nasterii;
        this.email = email;
        this.parola = parola;

    }

    public static void makeConnection(){
        try {

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            con=DriverManager.getConnection(url,username,password);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getGen() {
        return gen;
    }

    public String getData_nasterii() {
        return data_nasterii;
    }

    public String getEmail() {
        return email;
    }

    public String getParola() {
        return parola;
    }

    public int getID(){return ID;}

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public void setData_nasterii(String data_nasterii) {
        this.data_nasterii = data_nasterii;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public static void setCon(Connection con) {
        User.con = con;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "User{" + "ID=" + ID +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", gen='" + gen + '\'' +
                ", data_nasterii='" + data_nasterii + '\'' +
                ", email='" + email + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }

    public void insertBD(){

        String sql2 = "INSERT INTO `chatzone`.`users` (userID,nume,prenume,gen,data_nasterii,email,parola) values (?,?,?,?,?,?,?)";
        makeConnection();

        if(!exista_ID()) {
            try {
                PreparedStatement st = con.prepareStatement(sql2);
                st.setInt(1, ID);
                st.setString(2, nume);
                st.setString(3, prenume);
                st.setString(4, gen);


                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = sdf1.parse(data_nasterii);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                st.setDate(5, sqlDate);

                st.setString(6, email);
                st.setString(7, parola);
                int nr = st.executeUpdate();
                System.out.println(nr);

                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean exista_user(String email, String parola){


        String sql3="SELECT * FROM `chatzone`.`users` WHERE email=? AND parola=?";
        makeConnection();

        try {
            PreparedStatement st=con.prepareStatement(sql3);
            st.setString(1,email);
            st.setString(2,parola);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                if(rez.getString("email").equals(email)&&rez.getString("parola").equals(parola))

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

    public boolean exista_email(){


        String sql3="SELECT * FROM `chatzone`.`users` WHERE email=?";
        makeConnection();

        try {
            PreparedStatement st=con.prepareStatement(sql3);
            st.setString(1,email);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                if(rez.getString("email").equals(email))

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

    public boolean exista_ID(){


        String sql3="SELECT * FROM `chatzone`.`users` WHERE userID=?";
        makeConnection();

        try {
            PreparedStatement st=con.prepareStatement(sql3);
            st.setInt(1,this.ID);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                if(rez.getInt("userID")==this.ID)
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

    public static int giveID(){
        String sql4="SELECT userID FROM `chatzone`.`users` WHERE userID=(SELECT MAX(userID) FROM `chatzone`.`users`)";
        makeConnection();

        try{
            PreparedStatement st=con.prepareStatement(sql4);
            ResultSet rez=st.executeQuery();
            while(rez.next()){
                return (rez.getInt("userID")+1);
            }
           // con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public static void main(String[] args) {
        /*User user=new User("Zlotea","Eliza","female","12-02-1998","elizazlotea12@yahoo.com","parola");
        user.insertBD();
        System.out.println(exista_user("elizazlotea12@yahoo.com","parola"));*/


        User dummy1=new User();
        //User dummy2=new User();
        System.out.println(dummy1.getID());

    }
}
