package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Calculator extends Frame implements WindowListener {

    TextField input=new TextField(20);
    TextField result=new TextField(20);
    Button bnum[];
    Button bplus,bminus,bequals,brefresh;

    GridBagConstraints gbc=new GridBagConstraints();

    public static String inputString="";

    public static int number1=0;
    public static int number2=0;

    public static int inputNr=1;

    public static int sign=1;
    public static int prevsign=1;

    public static int total=0;

    public static void main(String[] args) {
        Calculator calculator=new Calculator("My first calculator");
        calculator.setSize(250,250);
        calculator.setVisible(true);
    }

    public Calculator(String title){
        super(title);
        setLayout(new GridBagLayout());


        addWindowListener(this);

        bnum=new Button[10];
        for(int i=0;i<=9;i++){
            bnum[i] = new Button(""+i);
            if(i!=0) {
                gbc.gridx = 5 + (i-1) % 3;
                gbc.gridy = 5 + (i-1) / 3;
            }
            else
            {
                gbc.gridx = 6;
                gbc.gridy = 8;
            }
            add(bnum[i],gbc);
        }
        bplus = new Button("+");
        gbc.gridx = 5;
        gbc.gridy = 9;
        add(bplus,gbc);
        bminus = new Button("-");
        gbc.gridx = 6;
        gbc.gridy = 9;
        add(bminus,gbc);
        bequals = new Button("=");
        gbc.gridx = 7;
        gbc.gridy = 9;
        add(bequals,gbc);
        brefresh=new Button("Refresh");
        gbc.gridx = 6;
        gbc.gridy = 10;
        add(brefresh,gbc);

        gbc.gridx=5;
        gbc.gridy=0;
        gbc.gridheight=4;
        gbc.gridwidth=5;
        gbc.fill=GridBagConstraints.VERTICAL;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        input.setEditable(false);
        add(input,gbc);

        gbc.gridx=5;
        gbc.gridy=11;
        gbc.gridheight=4;
        gbc.gridwidth=5;
        gbc.fill=GridBagConstraints.VERTICAL;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        result.setEditable(false);
        add(result,gbc);


        addListeners();

    }

    private void addListeners(){

        for(int i=0;i<=9;i++)
            bnum[i].addActionListener(new ActionListenersForNumbers(i, input));



        bplus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makePlus();
            }
        });

        bminus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeMinus();
            }
        });

        bequals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeEquals();
            }
        });

        brefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeRefresh();
            }
        });
    }

    private void makePlus(){
        input.setText("");
        sign=1;

        inputString="";
        inputNr++;

        if(inputNr==3){
            Calculator.total=Calculator.number1+Calculator.number2*Calculator.prevsign;
            Calculator.number1=Calculator.total;
            Calculator.number2=0;
            inputNr=2;
        }
    }

   private void makeMinus(){
        input.setText("");
        sign=-1;

        inputString="";
        inputNr++;

       if(inputNr==3){
           Calculator.total=Calculator.number1+Calculator.number2*Calculator.prevsign;
           Calculator.number1=Calculator.total;
           Calculator.number2=0;
           inputNr=2;
       }
   }

   private void makeEquals(){

       Calculator.total=Calculator.number1+Calculator.number2*Calculator.prevsign;
       Calculator.number1=Calculator.total;
       Calculator.number2=0;
       inputNr=1;

        result.setText(""+total);
   }

   private void makeRefresh(){
       total=0;
       input.setText("");
       result.setText("");

       number1=0;
       number2=0;
       inputString="";
   }

    public void windowClosing(WindowEvent e){
        dispose();
        System.exit(0);
    }

    public void windowOpened(WindowEvent e){}
    public void windowActivated(WindowEvent e){}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
}
