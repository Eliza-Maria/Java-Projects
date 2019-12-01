import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class SignIn {


    private JPanel panel1;
    private JTextField numeText;
    private JTextField prenumeText;
    private JPasswordField passwordText;
    private JButton signInButton;
    private JButton goToSignInButton;
    private JButton goToLogInButton;
    private JButton fButton;
    private JButton mButton;
    private JTextField mmText;
    private JTextField yyyyText;
    private JLabel numeLabel;
    private JLabel chatZoneLabel;
    private JLabel prenumeLabel;
    private JLabel genLabel;
    private JLabel dataLabel;
    private JLabel emailLabel;
    private JTextField ddText;
    private JTextField emailText;
    private JLabel passwordLabel;

    private boolean MFenabled;
    private String gen;

    public static JFrame frame;

    public static void main(String[] args) {
        SignIn.openSignIn();
    }

    public SignIn() {

        addListeners();

        MFenabled=false;
    }

    private void addListeners(){

        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeSignIn();
            }
        });

        goToSignInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeGoToSignIn();
            }
        });

        goToLogInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeGoToLogIn();
            }
        });

        fButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeF();
            }
        });

        mButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeM();
            }
        });

    }


    private void makeSignIn(){


        User newUser;

        if(MFenabled==false){
            //TODO: fa o fereastra de eroare
            gen="Male";

        }
        String nume =numeText.getText();
        String prenume=prenumeText.getText();
        String day=ddText.getText();
        String month=mmText.getText();
        String year=yyyyText.getText();
        String data=year+"-"+month+"-"+day;
        String email=emailText.getText();

        passwordText.setEchoChar('*');
        char[] aux=passwordText.getPassword();
        String parola=new String(aux);

        newUser=new User(nume,prenume,gen,data,email,parola);


        System.out.println(newUser.toString());

        //dialogue frame
        if(!newUser.exista_email()){
            newUser.insertBD();
            JOptionPane.showMessageDialog(frame, "You have successfully signed in!","ChatZone", JOptionPane.PLAIN_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(frame, "Email already in use, try again!","ChatZone", JOptionPane.PLAIN_MESSAGE);


        System.out.println(newUser.toString());

        resetFrame();

    }

    public void resetFrame(){

        numeText.setText("");
        prenumeText.setText("");
        ddText.setText("");
        mmText.setText("");
        yyyyText.setText("");
        emailText.setText("");
        passwordText.setText("");
        ddText.setText("");
        mmText.setText("");
        yyyyText.setText("");
        fButton.setEnabled(true);
        mButton.setEnabled(true);

    }
    private void makeGoToSignIn(){

    }

    private void makeGoToLogIn(){
        goToSignInButton.setEnabled(true);
        LogIn.openLogIn();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));


    }

    private void makeF(){
        gen="Female";
        fButton.setEnabled(false);
        mButton.setEnabled(true);
        MFenabled=true;
    }

    private void makeM(){
        gen="Male";
        mButton.setEnabled(false);
        fButton.setEnabled(true);
        MFenabled=true;
    }

    public static void openSignIn(){
        frame = new JFrame("SignIn");
        frame.setContentPane(new SignIn().panel1);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}
