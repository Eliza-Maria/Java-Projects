import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class LogIn {
    private JTextField emailText;
    private JPasswordField passwordText;
    private JButton logInButton;
    private JButton goToLogInButton;
    private JButton goToSignInButton;
    private JLabel chatZoneLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JPanel panel2;

    public static JFrame frame;

    public static void main(String[] args) {
        LogIn.openLogIn();
    }

    public static void openLogIn(){
        frame = new JFrame("LogIn");
        frame.setContentPane(new LogIn().panel2);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(500,300);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }


    public LogIn() {

        addListeners();
    }

    private void addListeners(){

        logInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    makeLogIn();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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
    }

    private void makeLogIn() throws SQLException {

        String email=emailText.getText();
        passwordText.setEchoChar('*');
        char[] aux=passwordText.getPassword();
        String parola=new String(aux);

        if(User.exista_user(email,parola))
        {
            JOptionPane.showMessageDialog(frame, "You have successfully logged in!","ChatZone", JOptionPane.PLAIN_MESSAGE);


            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            Client.openCleint(email);

        }
        else
            JOptionPane.showMessageDialog(frame, "Wrong email/password!","ChatZone", JOptionPane.PLAIN_MESSAGE);


    }

    private void makeGoToSignIn(){

        goToLogInButton.setEnabled(true);
        SignIn.openSignIn();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private void makeGoToLogIn(){

    }
}
