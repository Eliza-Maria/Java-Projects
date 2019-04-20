package Calculator;

import Calculator.Calculator;
import org.w3c.dom.Text;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenersForNumbers implements ActionListener {

   private int value;
   private TextField tf;

   ActionListenersForNumbers(int value, TextField tf){
        this.value=value;
        this.tf=tf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Calculator.inputString+=value;
        tf.setText(Calculator.inputString);

        if(Calculator.inputNr==1) {
            Calculator.number1 *= 10;
            Calculator.number1 += value;
        }
        else if(Calculator.inputNr==2){
            Calculator.prevsign=Calculator.sign;

            Calculator.number2 *= 10;
            Calculator.number2 += value;

        }

    }
}
