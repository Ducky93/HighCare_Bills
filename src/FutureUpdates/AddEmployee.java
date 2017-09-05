package FutureUpdates;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by mohab 2 on 11/16/2016.
 */
public class AddEmployee {

    private JPanel panel1;
    private JTextField txt_name;
    private JTextField txt_age;
    private JTextField txt_NationalID;
    private JTextField txt_phoneNumber;
    private JTextField txt_joinDate;
    private JTextArea txt_Notes;
    private JButton btn_addEmp;
    private JLabel lbl;
    private JComboBox drop_newEmp_city;
    private JComboBox drop_newEmp_location;
    private JComboBox drop_department;
    private DatabaseManager dbManager = new DatabaseManager();
//    private Connection conn;
//    private Connection AddEmpConn = dbManager.checkConnection(conn);

    private AddEmployee() throws SQLException, ParseException {
        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        btn_addEmp.addActionListener(e -> {
            try {
                Employee currentEmp = new Employee(txt_name.getText(), Integer.parseInt(txt_age.getText()), txt_NationalID.getText()
                        , txt_phoneNumber.getText(), txt_joinDate.getText(), txt_Notes.getText(), drop_newEmp_city.getSelectedItem().toString()
                        , drop_newEmp_location.getSelectedItem().toString(), drop_department.getSelectedItem().toString());
                JOptionPane.showMessageDialog(panel1, " بنجاح! " + txt_name.getText() + " تم تسجيل الموظف  ");
            } catch (Exception err) {
                JOptionPane.showMessageDialog(panel1, err);
            }
        });

        panel1.addHierarchyListener(e -> {
            JComponent component = (JComponent) e.getSource();

            if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                    && component.isShowing()) {
                java.util.List<String> myCitiesList = null;
                try {
                    myCitiesList = dbManager.retrieveCitiesFROM_DB();
                    myCitiesList.forEach(x -> drop_newEmp_city.addItem(x));
                    drop_department.removeAllItems();

                    drop_department.addItem(DatabaseManager.SECURITY_DEPT);
                    drop_department.addItem(DatabaseManager.CLEANING_DEPT);
                    drop_department.addItem(DatabaseManager.MONEYTRANSFERE_DEPT);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        });
        drop_newEmp_city.addItemListener(e -> {
            drop_newEmp_location.removeAllItems();

            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
//                    AddEmpConn = dbManager.checkConnection(AddEmpConn);
                    java.util.List<String> LocationsList = dbManager.retrieveLocationsFROM_DB(drop_newEmp_city.getSelectedItem().toString());
                    LocationsList.forEach(s -> drop_newEmp_location.addItem(s));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } finally {
                    drop_newEmp_location.setEnabled(true);
                }

            }
        });
    }

    public static void main(String[] args) throws ParseException, SQLException {
        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        AddEmployee emp = new AddEmployee();
        JFrame frame = new JFrame();
        frame.add(emp.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
        frame.setBounds(screenWidth / 3, screenHeight / 5, 420, 500);
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:10dlu:noGrow,fill:57px:noGrow,right:116dlu:noGrow,right:73dlu:noGrow", "center:42px:noGrow,center:34px:noGrow,center:34px:noGrow,center:34px:noGrow,center:34px:noGrow,center:34px:noGrow,center:62dlu:noGrow,center:42px:noGrow,top:4dlu:noGrow,center:26px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.setBackground(new Color(-2962968));
        panel1.setOpaque(true);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font(panel1.getFont().getName(), panel1.getFont().getStyle(), panel1.getFont().getSize())));
        txt_name = new JTextField();
        CellConstraints cc = new CellConstraints();
        panel1.add(txt_name, cc.xy(4, 2, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(4);
        label1.setText("اسم الموظف");
        panel1.add(label1, cc.xy(5, 2));
        txt_age = new JTextField();
        panel1.add(txt_age, cc.xy(4, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("العمر");
        panel1.add(label2, cc.xy(5, 3));
        txt_NationalID = new JTextField();
        panel1.add(txt_NationalID, cc.xy(4, 4, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        label3.setHorizontalTextPosition(4);
        label3.setText("الرقم القومى");
        panel1.add(label3, cc.xy(5, 4));
        txt_phoneNumber = new JTextField();
        panel1.add(txt_phoneNumber, cc.xy(4, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        label4.setHorizontalTextPosition(4);
        label4.setText("تليفون");
        panel1.add(label4, cc.xy(5, 5));
        txt_joinDate = new JTextField();
        panel1.add(txt_joinDate, cc.xy(4, 6, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(4);
        label5.setHorizontalTextPosition(4);
        label5.setText("تاريخ الانضمام للشركة");
        panel1.add(label5, cc.xy(5, 6));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(4);
        label6.setHorizontalTextPosition(4);
        label6.setText("ملحوظات");
        panel1.add(label6, cc.xy(5, 7));
        txt_Notes = new JTextArea();
        txt_Notes.setColumns(0);
        txt_Notes.setDropMode(DropMode.INSERT);
        txt_Notes.setFont(new Font("Andalus", txt_Notes.getFont().getStyle(), 16));
        txt_Notes.setLineWrap(true);
        txt_Notes.setRows(2);
        txt_Notes.setToolTipText("ملحوظات إضافية حول الموظف");
        txt_Notes.setWrapStyleWord(false);
        panel1.add(txt_Notes, cc.xyw(3, 7, 2, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label7 = new JLabel();
        label7.setFont(new Font("Traditional Arabic", Font.BOLD, 22));
        label7.setHorizontalAlignment(0);
        label7.setHorizontalTextPosition(0);
        label7.setText("إضافة موظف جديد");
        panel1.add(label7, cc.xyw(3, 1, 3));
        drop_newEmp_location = new JComboBox();
        drop_newEmp_location.setEnabled(false);
        panel1.add(drop_newEmp_location, cc.xy(4, 12, CellConstraints.FILL, CellConstraints.DEFAULT));
        drop_newEmp_city = new JComboBox();
        panel1.add(drop_newEmp_city, cc.xy(4, 10, CellConstraints.FILL, CellConstraints.DEFAULT));
        btn_addEmp = new JButton();
        btn_addEmp.setBackground(new Color(-2962968));
        btn_addEmp.setHorizontalTextPosition(0);
        btn_addEmp.setText("إضافة الموظف");
        panel1.add(btn_addEmp, cc.xy(5, 14));
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(4);
        label8.setHorizontalTextPosition(4);
        label8.setText("موقع العمل");
        panel1.add(label8, cc.xy(5, 12));
        lbl = new JLabel();
        lbl.setHorizontalAlignment(4);
        lbl.setHorizontalTextPosition(4);
        lbl.setText("المحافظة مقر العمل");
        panel1.add(lbl, cc.xy(5, 10));
        drop_department = new JComboBox();
        panel1.add(drop_department, cc.xy(4, 8, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label9 = new JLabel();
        label9.setHorizontalAlignment(4);
        label9.setHorizontalTextPosition(4);
        label9.setText("القطاع");
        panel1.add(label9, cc.xy(5, 8));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}