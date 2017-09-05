package FutureUpdates;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import DataBaseManageAndReport.MyMainConnection;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mohab 2 on 11/17/2016.
 */
public class CheckEmpMonthlyPerf {
    private JPanel panel1;
    private JComboBox drop_egptCities;
    private JCheckBox chk_UnallowedAbsence;
    private JTextField txt_unallowedAbsence;
    private JTextField txt_allowedAbsence;
    private JCheckBox chk_allowedAbsence;
    private JTextField txt_ill;
    private JCheckBox chk_ill;
    private JTextField txt_Vacation;
    private JCheckBox chk_vacation;
    private JButton btn_save_monthly_emp;
    private JTextField txt_punish;
    private JCheckBox chk_punish;
    private JTextField txt_unallowedAbs_notes;
    private JTextField txt_allowedAbs_notes;
    private JTextField txt_vacation_notes;
    private JTextField txt_ill_notes;
    private JTextField txt_punish_notes;
    private JLabel lbl_UnallowedAbs_extraNotes;
    private JLabel lbl_Abs_extraNotes;
    private JLabel lbl_Vacation_extraNotes;
    private JLabel lbl_ill_extraNotes;
    private JLabel lbl_punish_notes;
    private JComboBox drop_empName;
    private JComboBox drop_location;
    private JButton btn_exit;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
//    private Connection conn;
//    private Connection checkerConn = dbManager.checkConnection(conn);
    private ResultSet rs;
    private String sql;
    private PreparedStatement preparedStatement;
    private int previousMonth;
    private int previousYear;
    private String[] monthlyData = new String[11];

    private CheckEmpMonthlyPerf() throws SQLException {

        getPrevMonthAndYearForSalaryRecords();

        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        chk_UnallowedAbsence.addActionListener(e -> {
            if (chk_UnallowedAbsence.isSelected()) {
                txt_unallowedAbs_notes.setEnabled(true);
                txt_unallowedAbsence.setEnabled(true);
            } else {
                txt_unallowedAbs_notes.setEnabled(false);
                txt_unallowedAbsence.setEnabled(false);
            }
        });
        chk_allowedAbsence.addActionListener(e -> {
            if (chk_allowedAbsence.isSelected()) {
                txt_allowedAbs_notes.setEnabled(true);
                txt_allowedAbsence.setEnabled(true);
            } else {
                txt_allowedAbs_notes.setEnabled(false);
                txt_allowedAbsence.setEnabled(false);
            }
        });
        chk_vacation.addActionListener(e -> {
            if (chk_vacation.isSelected()) {
                txt_vacation_notes.setEnabled(true);
                txt_Vacation.setEnabled(true);
            } else {
                txt_vacation_notes.setEnabled(false);
                txt_Vacation.setEnabled(false);
            }
        });
        chk_ill.addActionListener(e -> {
            if (chk_ill.isSelected()) {
                txt_ill_notes.setEnabled(true);
                txt_ill.setEnabled(true);
            } else {
                txt_ill_notes.setEnabled(false);
                txt_ill.setEnabled(false);
            }
        });
        chk_punish.addActionListener(e -> {
            if (chk_punish.isSelected()) {
                txt_punish_notes.setEnabled(true);
                txt_punish.setEnabled(true);
            } else {
                txt_punish_notes.setEnabled(false);
                txt_punish.setEnabled(false);
            }
        });
        drop_egptCities.addItemListener(e -> {
            drop_location.removeAllItems();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
//                    checkerConn = dbManager.checkConnection(checkerConn);
                    java.util.List<String> LocationsList = dbManager.retrieveLocationsFROM_DB(drop_egptCities.getSelectedItem().toString());
                    LocationsList.forEach(s -> drop_location.addItem(s));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } finally {
                    drop_location.setEnabled(true);
                    if (drop_empName.getItemCount() != 0) {
                        drop_empName.setEnabled(true);
                        chk_allowedAbsence.setEnabled(true);
                        chk_ill.setEnabled(true);
                        chk_UnallowedAbsence.setEnabled(true);
                        chk_vacation.setEnabled(true);
                        chk_punish.setEnabled(true);
                        btn_save_monthly_emp.setEnabled(true);
                    } else {
                        chk_allowedAbsence.setEnabled(false);
                        chk_ill.setEnabled(false);
                        chk_UnallowedAbsence.setEnabled(false);
                        chk_vacation.setEnabled(false);
                        chk_punish.setEnabled(false);
                        btn_save_monthly_emp.setEnabled(false);
                    }
                }

            }
        });

        drop_location.addItemListener(e -> {
            drop_empName.removeAllItems();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
//                    checkerConn = dbManager.checkConnection(checkerConn);
                    java.util.List<String> myEmployeesList = dbManager.retrieve_EmployeesInLocationsFROM_DB(drop_location.getSelectedItem().toString());
                    myEmployeesList.forEach(s -> drop_empName.addItem(s));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        panel1.addHierarchyListener(e -> {
            JComponent component = (JComponent) e.getSource();

            if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                    && component.isShowing()) {
                java.util.List<String> myCitiesList = null;
                try {
                    myCitiesList = dbManager.retrieveCitiesFROM_DB();
                    myCitiesList.forEach(x -> drop_egptCities.addItem(x));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btn_save_monthly_emp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
// Name	Unallowed_Absence	unallowed_Absence_notes	Allowed_Absence	allowed_Absence_notes
// Vacation	vacation_notes	Ill_absence	ill_notes	Punishment	punishment_notes	Month	Year
                monthlyData[0] = drop_empName.getSelectedItem().toString();
                monthlyData[1] = txt_unallowedAbsence.getText();
                monthlyData[2] = txt_unallowedAbs_notes.getText();
                monthlyData[3] = txt_allowedAbsence.getText();
                monthlyData[4] = txt_allowedAbs_notes.getText();
                monthlyData[5] = txt_Vacation.getText();
                monthlyData[6] = txt_vacation_notes.getText();
                monthlyData[7] = txt_ill.getText();
                monthlyData[8] = txt_ill_notes.getText();
                monthlyData[9] = txt_punish.getText();
                monthlyData[10] = txt_punish_notes.getText();
                try {
                    storeMonthlyCheckOnEmp(monthlyData);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(panel1, "يجب تسجيل ارقام فقط في خانات الغياب , الجزاءات والأجازات");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void getPrevMonthAndYearForSalaryRecords() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        previousMonth = Integer.parseInt(monthFormat.format(cal.getTime()));
        previousYear = Integer.parseInt(yearFormat.format(cal.getTime()));
    }

    public void storeMonthlyCheckOnEmp(String[] arrayOfData) throws SQLException {
// Name	Unallowed_Absence	unallowed_Absence_notes	Allowed_Absence	allowed_Absence_notes
// Vacation	vacation_notes	Ill_absence	ill_notes	Punishment	punishment_notes	Month	Year
        sql = "INSERT INTO " + Employee.empMonthlyCheck + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
//        checkerConn = dbManager.checkConnection(checkerConn);
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1, arrayOfData[0]);
        preparedStatement.setInt(2, Integer.parseInt(arrayOfData[1]));
        preparedStatement.setString(3, arrayOfData[2]);
        preparedStatement.setInt(4, Integer.parseInt(arrayOfData[3]));
        preparedStatement.setString(5, arrayOfData[4]);
        preparedStatement.setInt(6, Integer.parseInt(arrayOfData[5]));
        preparedStatement.setString(7, arrayOfData[6]);
        preparedStatement.setInt(8, Integer.parseInt(arrayOfData[7]));
        preparedStatement.setString(9, arrayOfData[8]);
        preparedStatement.setInt(10, Integer.parseInt(arrayOfData[9]));
        preparedStatement.setString(11, arrayOfData[10]);
        preparedStatement.setInt(12, previousMonth);
        preparedStatement.setInt(13, previousYear);
        try {
            preparedStatement.execute();
        } catch (MySQLIntegrityConstraintViolationException exception) {
            JOptionPane.showMessageDialog(panel1, "لقد تم تسجيل المتابعة الشهرية لهذا الموظف بالفعل ! الرجاء أختيار أسم موظف آخر ");
        }
    }

    public static void main(String[] args) throws ParseException, SQLException {

        new Employee();
        CheckEmpMonthlyPerf checkEmp = new CheckEmpMonthlyPerf();

        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        Employee.setLookAndFeelForMyProgram();
//        checkEmp.checkerConn = checkEmp.dbManager.checkConnection(checkEmp.checkerConn);
        JFrame frame = new JFrame();
        frame.add(checkEmp.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(620, 500);
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
        panel1.setLayout(new FormLayout("center:57px:noGrow,center:78px:noGrow,right:4dlu:noGrow,fill:51px:noGrow,left:4dlu:noGrow,center:108dlu:noGrow,center:4dlu:noGrow,left:20dlu:noGrow,left:4dlu:noGrow,center:168px:noGrow,left:4dlu:noGrow,fill:12px:noGrow", "center:71px:noGrow,center:17dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,center:4dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,center:4dlu:noGrow,center:20dlu:noGrow,center:4dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:10dlu:noGrow,center:16px:noGrow"));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 22));
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("متابعة الموظفين");
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xyw(1, 1, 12, CellConstraints.CENTER, CellConstraints.DEFAULT));
        drop_egptCities = new JComboBox();
        panel1.add(drop_egptCities, cc.xy(10, 3, CellConstraints.FILL, CellConstraints.CENTER));
        final JLabel label2 = new JLabel();
        label2.setText("المحافظة");
        panel1.add(label2, cc.xy(10, 2));
        chk_UnallowedAbsence = new JCheckBox();
        chk_UnallowedAbsence.setEnabled(false);
        chk_UnallowedAbsence.setHorizontalAlignment(0);
        chk_UnallowedAbsence.setText("غياب");
        panel1.add(chk_UnallowedAbsence, cc.xy(10, 5));
        chk_allowedAbsence = new JCheckBox();
        chk_allowedAbsence.setEnabled(false);
        chk_allowedAbsence.setText("غياب بأذن");
        panel1.add(chk_allowedAbsence, cc.xy(10, 7));
        chk_ill = new JCheckBox();
        chk_ill.setEnabled(false);
        chk_ill.setText("أجازة مرضي");
        panel1.add(chk_ill, cc.xy(10, 11));
        chk_vacation = new JCheckBox();
        chk_vacation.setEnabled(false);
        chk_vacation.setText("أجازة");
        panel1.add(chk_vacation, cc.xy(10, 9));
        txt_unallowedAbsence = new JTextField();
        txt_unallowedAbsence.setEnabled(false);
        panel1.add(txt_unallowedAbsence, cc.xy(8, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_allowedAbsence = new JTextField();
        txt_allowedAbsence.setEnabled(false);
        panel1.add(txt_allowedAbsence, cc.xy(8, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_Vacation = new JTextField();
        txt_Vacation.setEnabled(false);
        panel1.add(txt_Vacation, cc.xy(8, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_ill = new JTextField();
        txt_ill.setEnabled(false);
        panel1.add(txt_ill, cc.xy(8, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_punish = new JTextField();
        txt_punish.setEnabled(false);
        panel1.add(txt_punish, cc.xy(8, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        chk_punish = new JCheckBox();
        chk_punish.setEnabled(false);
        chk_punish.setText("جزاءات");
        panel1.add(chk_punish, cc.xy(10, 13));
        txt_unallowedAbs_notes = new JTextField();
        txt_unallowedAbs_notes.setEnabled(false);
        panel1.add(txt_unallowedAbs_notes, cc.xyw(2, 5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_vacation_notes = new JTextField();
        txt_vacation_notes.setEnabled(false);
        panel1.add(txt_vacation_notes, cc.xyw(2, 9, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_ill_notes = new JTextField();
        txt_ill_notes.setEnabled(false);
        panel1.add(txt_ill_notes, cc.xyw(2, 11, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_punish_notes = new JTextField();
        txt_punish_notes.setEnabled(false);
        panel1.add(txt_punish_notes, cc.xyw(2, 13, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        lbl_UnallowedAbs_extraNotes = new JLabel();
        lbl_UnallowedAbs_extraNotes.setText("ملحوظات اضافية");
        panel1.add(lbl_UnallowedAbs_extraNotes, cc.xy(6, 5));
        lbl_Abs_extraNotes = new JLabel();
        lbl_Abs_extraNotes.setText("ملحوظات اضافية");
        panel1.add(lbl_Abs_extraNotes, cc.xy(6, 7));
        lbl_Vacation_extraNotes = new JLabel();
        lbl_Vacation_extraNotes.setText("ملحوظات اضافية");
        panel1.add(lbl_Vacation_extraNotes, cc.xy(6, 9));
        lbl_ill_extraNotes = new JLabel();
        lbl_ill_extraNotes.setText("ملحوظات اضافية");
        panel1.add(lbl_ill_extraNotes, cc.xy(6, 11));
        lbl_punish_notes = new JLabel();
        lbl_punish_notes.setText("ملحوظات اضافية");
        panel1.add(lbl_punish_notes, cc.xy(6, 13));
        txt_allowedAbs_notes = new JTextField();
        txt_allowedAbs_notes.setEnabled(false);
        panel1.add(txt_allowedAbs_notes, cc.xyw(2, 7, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("أسم الموظف");
        panel1.add(label3, cc.xyw(2, 2, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        drop_empName = new JComboBox();
        drop_empName.setEnabled(false);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        drop_empName.setModel(defaultComboBoxModel1);
        panel1.add(drop_empName, cc.xyw(2, 3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("الموقع");
        panel1.add(label4, cc.xy(6, 2));
        drop_location = new JComboBox();
        drop_location.setEnabled(false);
        panel1.add(drop_location, cc.xy(6, 3, CellConstraints.FILL, CellConstraints.CENTER));
        btn_save_monthly_emp = new JButton();
        btn_save_monthly_emp.setEnabled(false);
        btn_save_monthly_emp.setText("حفظ");
        panel1.add(btn_save_monthly_emp, cc.xy(10, 15));
        btn_exit = new JButton();
        btn_exit.setEnabled(true);
        btn_exit.setPreferredSize(new Dimension(100, 32));
        btn_exit.setText("خروج");
        panel1.add(btn_exit, cc.xy(6, 15));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
