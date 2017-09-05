package Utils;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class MyDatePicker extends JFrame
{
    JLabel CheckDate;
    public UtilDateModel model;
    public JDatePanelImpl datePanel;
    public JDatePickerImpl datePicker;
    private String datePattern = "dd/MM/yyyy";
    public static boolean done=false;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
    public static JTextField textField ;
    public MyDatePicker(JTextField textFieldUnderProcess)
    {
        done=false;
        model = new UtilDateModel();


        datePanel = new JDatePanelImpl(model,new Properties());
        datePicker = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
            @Override
            public Object stringToValue(String text) throws ParseException {
                return dateFormatter.parseObject(text);
            }

            @Override
            public String valueToString(Object value) throws ParseException {
                if (value != null) {
                    Calendar cal = (Calendar) value;
                    textFieldUnderProcess.setText(dateFormatter.format(cal.getTime()));
                    pickedDate(textFieldUnderProcess.getText());
                    return dateFormatter.format(cal.getTime());
                }
                return "";
            }
        });

        JPanel panel=new JPanel();
        CheckDate=new JLabel("Date:");


        panel.add(CheckDate);
        panel.add(datePicker);
        add(panel);


        setBounds(200,150,300,70);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setResizable(false);
        setVisible(true);

    }
    private void pickedDate(String text){
        datePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(text!=null){
                    done=true;
                }
            }
        });
    }
    public static void main(String[] args){
        new MyDatePicker(textField);
    }
}