package FutureUpdates;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by mohab 2 on 11/20/2016.
 */
public class AddNewLocationContract {
    private JPanel panel1;
    private JComboBox drop_chooseCity;
    private JTextField txt_contractName;
    private JFormattedTextField txt_LaborNumber;
    private JFormattedTextField txt_ContractEndDate;
    private JFormattedTextField txt_annualIncrement;
    private JButton btn_saveLocation;
    private JFormattedTextField txt_contractValue;
    private JFormattedTextField txt_ContractStartDate;
    private JComboBox drop_chooseDept;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
    private boolean enableSaveBtn = true;
    private boolean proceedSavingLocation = true;


    public AddNewLocationContract() throws SQLException, ParseException {
        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        MaskFormatter dateFormatText = new MaskFormatter("##-##-####");
        dateFormatText.setPlaceholderCharacter('_');

        MaskFormatter percentFormat = new MaskFormatter("%##");
        percentFormat.setPlaceholderCharacter('_');


        Object[] myTextBoxList = {
                txt_ContractStartDate,
                txt_ContractEndDate,
                txt_annualIncrement,
                txt_contractValue,
                txt_LaborNumber
        };


        txt_ContractStartDate.setFormatterFactory(new DefaultFormatterFactory(dateFormatText));
        txt_ContractEndDate.setFormatterFactory(new DefaultFormatterFactory(dateFormatText));
        txt_annualIncrement.setFormatterFactory(new DefaultFormatterFactory(percentFormat));
//        txt_LaborNumber.setFormatterFactory(new DefaultFormatterFactory(laborNumber));
//        txt_contractValue.setFormatterFactory(new DefaultFormatterFactory(numberFormat));


        panel1.addHierarchyListener(e -> {
            JComponent component = (JComponent) e.getSource();

            if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                    && component.isShowing()) {
                java.util.List<String> myCitiesList = null;
                try {
                    myCitiesList = dbManager.retrieveCitiesFROM_DB();
                    myCitiesList.forEach(x -> drop_chooseCity.addItem(x));
                    drop_chooseDept.addItem(DatabaseManager.SECURITY_DEPT);
                    drop_chooseDept.addItem(DatabaseManager.CLEANING_DEPT);
                    drop_chooseDept.addItem(DatabaseManager.MONEYTRANSFERE_DEPT);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }

        });


        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (Object obj : myTextBoxList) {
                    enableSaveBtn = !((JFormattedTextField) obj).getText().replaceAll("[_%-]", "").isEmpty();
                    if (!enableSaveBtn) {
                        break;
                    }
                }
                if (txt_contractName.getText().isEmpty()) {
                    enableSaveBtn = false;
                }
                if (enableSaveBtn) {
                    btn_saveLocation.setEnabled(true);
                } else {
                    btn_saveLocation.setEnabled(false);
                }
            }
        });
        btn_saveLocation.addActionListener(e -> {
            try {
                Integer.parseInt(txt_LaborNumber.getText());
                proceedSavingLocation = true;
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(panel1, "يجب ان تكون خانة عدد الأفراد مكونة من أرقام فقط");
                proceedSavingLocation = false;
            }
            try {
                Double.parseDouble(txt_contractValue.getText());
                proceedSavingLocation = true;
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(panel1, "يجب ان تكون خانة قيمة التعاقد مكونة من أرقام فقط");
                proceedSavingLocation = false;
            }
            try {
                if (proceedSavingLocation) {
                    dbManager.insertNewContractDataInDB(
                            drop_chooseCity.getSelectedItem().toString(),
                            txt_contractName.getText(), Integer.parseInt(txt_LaborNumber.getText()),
                            Double.parseDouble(txt_contractValue.getText()),
                            String.valueOf(txt_ContractStartDate.getText()),
                            String.valueOf(txt_ContractEndDate.getText()),
                            Integer.parseInt(txt_annualIncrement.getText().replace("%", "")), drop_chooseDept.getSelectedItem().toString()
                            , panel1, "insert");
                    JOptionPane.showMessageDialog(panel1, " بنجاح! " + txt_contractName.getText() + " تم تسجيل تعاقد ");
                } else {
                    JOptionPane.showMessageDialog(panel1, "الرجاء التأكد من صحة المدخلات ");
                }
            } catch (MySQLIntegrityConstraintViolationException e1) {
                JOptionPane.showMessageDialog(panel1, "إسم التعاقد مكرر , برجاء إدخال اسم التعاقد الصحيح أو تعديل التعاقد الحالى في إدارة التعاقدات");
            } catch (SQLException | ParseException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(panel1, e1);
            }
        });


    }

    public static void main(String[] args) throws ParseException, SQLException {

        new Employee();
        AddNewLocationContract newLocationContract = new AddNewLocationContract();

        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        Employee.setLookAndFeelForMyProgram();
//        newLocationContract.NewLocationConn = newLocationContract.dbManager.checkConnection(newLocationContract.NewLocationConn);

        JFrame frame = new JFrame();
        frame.add(newLocationContract.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);

        frame.setBounds(screenWidth / 3, screenHeight / 5, 620, 400);
        frame.setResizable(false);


    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:12px:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:69px:noGrow,top:4dlu:noGrow,center:15dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:31px:noGrow,top:4dlu:noGrow,center:27px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:6dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:35px:noGrow"));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), label1.getFont().getStyle(), 14));
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("عدد الأفراد بالتعاقد");
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xy(7, 9));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font(label2.getFont().getName(), label2.getFont().getStyle(), 14));
        label2.setHorizontalAlignment(0);
        label2.setHorizontalTextPosition(0);
        label2.setText("تاريخ بداية التعاقد");
        panel1.add(label2, cc.xy(7, 11));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font(label3.getFont().getName(), label3.getFont().getStyle(), 14));
        label3.setHorizontalAlignment(0);
        label3.setHorizontalTextPosition(0);
        label3.setText("تاريخ نهاية التعاقد");
        panel1.add(label3, cc.xy(7, 13));
        final JLabel label4 = new JLabel();
        label4.setFont(new Font(label4.getFont().getName(), label4.getFont().getStyle(), 14));
        label4.setHorizontalAlignment(0);
        label4.setHorizontalTextPosition(0);
        label4.setText("قيمة الزيادة السنوية");
        panel1.add(label4, cc.xy(7, 15));
        drop_chooseCity = new JComboBox();
        panel1.add(drop_chooseCity, cc.xy(5, 3));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font(label5.getFont().getName(), label5.getFont().getStyle(), 14));
        label5.setHorizontalAlignment(0);
        label5.setHorizontalTextPosition(0);
        label5.setText("إختار المحافظة");
        panel1.add(label5, cc.xy(7, 3));
        final JLabel label6 = new JLabel();
        label6.setFont(new Font(label6.getFont().getName(), label6.getFont().getStyle(), 14));
        label6.setHorizontalAlignment(0);
        label6.setHorizontalTextPosition(0);
        label6.setText("إسم التعاقد الجديد");
        panel1.add(label6, cc.xy(7, 7));
        txt_contractName = new JTextField();
        panel1.add(txt_contractName, cc.xyw(3, 7, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label7 = new JLabel();
        label7.setFont(new Font(label7.getFont().getName(), Font.BOLD, 18));
        label7.setHorizontalAlignment(0);
        label7.setText("إضافة تعاقد جديد");
        panel1.add(label7, cc.xyw(3, 1, 5));
        btn_saveLocation = new JButton();
        btn_saveLocation.setEnabled(false);
        btn_saveLocation.setHorizontalTextPosition(0);
        btn_saveLocation.setText("حفظ");
        panel1.add(btn_saveLocation, cc.xy(7, 19, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label8 = new JLabel();
        label8.setFont(new Font(label8.getFont().getName(), label8.getFont().getStyle(), 14));
        label8.setHorizontalAlignment(0);
        label8.setHorizontalTextPosition(0);
        label8.setText("قيمة التعاقد");
        panel1.add(label8, cc.xy(7, 17));
        txt_ContractStartDate = new JFormattedTextField();
        panel1.add(txt_ContractStartDate, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_ContractEndDate = new JFormattedTextField();
        txt_ContractEndDate.setText("");
        panel1.add(txt_ContractEndDate, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_LaborNumber = new JFormattedTextField();
        panel1.add(txt_LaborNumber, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_annualIncrement = new JFormattedTextField();
        panel1.add(txt_annualIncrement, cc.xy(5, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_contractValue = new JFormattedTextField();
        panel1.add(txt_contractValue, cc.xy(5, 17, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label9 = new JLabel();
        label9.setFont(new Font(label9.getFont().getName(), label9.getFont().getStyle(), 14));
        label9.setHorizontalAlignment(0);
        label9.setHorizontalTextPosition(0);
        label9.setText("إختار القطاع");
        panel1.add(label9, cc.xy(7, 5));
        drop_chooseDept = new JComboBox();
        panel1.add(drop_chooseDept, cc.xy(5, 5));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
