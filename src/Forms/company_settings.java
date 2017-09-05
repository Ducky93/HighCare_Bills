package Forms;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import DataBaseManageAndReport.MyMainConnection;
import TablesDesign.myTableMode;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mohab 2 on 11/29/2016.
 */
public class company_settings {
    private JPanel panel1;
    private JComboBox drop_existingCompany;
    private JTextField txt_taxesCard;
    private JTextField txt_commercialLog;
    private JTextField txt_taxesFile;
    private JTextField txt_salesTaxes;
    private JTextField txt_currentAddedValue;
    private JButton btn_save;
    private JTextField txt_newCompany;
    private JLabel lbl_newCompany;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
    private ResultSet rs;
    private String sql;
    private PreparedStatement preparedStatement;
    Map<String, String> companyDetails = new HashMap<>();
    Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();
    NumberFormat numberFormat = NumberFormat.getNumberInstance(arabicLocale);

    public company_settings() throws SQLException {
        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        panel1.addHierarchyListener(e -> {
            JComponent component = (JComponent) e.getSource();
            drop_existingCompany.removeAllItems();
            if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                    && component.isShowing()) {
                try {
                    ResultSet companyNamesRS = getCompaniesNames();
                    while (companyNamesRS.next()) {
                        drop_existingCompany.addItem(companyNamesRS.getString("company_name"));
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            drop_existingCompany.addItem("إاضافة شركة جديدة ...");
        });
        drop_existingCompany.addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (Objects.equals(drop_existingCompany.getSelectedItem().toString(), "إاضافة شركة جديدة ...")) {
                    lbl_newCompany.setEnabled(true);
                    txt_newCompany.setEnabled(true);
                } else {
                    lbl_newCompany.setEnabled(false);
                    txt_newCompany.setEnabled(false);
                }
                try {
                    companyDetails = getCompanyDetails(drop_existingCompany.getSelectedItem().toString());

                    txt_commercialLog.setText(arabitizeMyNumbers(companyDetails.get("commercialLog")));
                    txt_taxesCard.setText(arabitizeMyNumbers(companyDetails.get("taxesCard")));
                    txt_salesTaxes.setText(arabitizeMyNumbers(companyDetails.get("salesTax")));
                    txt_taxesFile.setText(arabitizeMyNumbers(companyDetails.get("taxesFile")));
                    txt_currentAddedValue.setText(arabitizeMyNumbers(companyDetails.get("currentAddedValue")));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }


        });


        btn_save.addActionListener(e -> {
            try {
//                comp_settingsConn = dbManager.checkConnection(comp_settingsConn);


                sql = "UPDATE " + Employee.companyDetails + " SET taxes_card=? , commercial_log=?" +
                        ",taxes_file = ?,sales_tax =? ,current_sales_taxes_percent = ? WHERE company_name = ?;";
                preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                preparedStatement.setString(1, txt_taxesCard.getText());
                preparedStatement.setString(2, txt_commercialLog.getText());
                preparedStatement.setString(3, txt_taxesFile.getText());
                preparedStatement.setString(4, txt_salesTaxes.getText());
                preparedStatement.setInt(5, Integer.parseInt(txt_currentAddedValue.getText()));
                preparedStatement.setString(6, drop_existingCompany.getSelectedItem().toString());
                preparedStatement.execute();
                JOptionPane.showMessageDialog(panel1, "تم تعديل بيانات الشركة بنجاح!");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        });
    }

    public ResultSet getCompaniesNames() throws SQLException {

//        conn = dbManager.checkConnection(conn);
        sql = "SELECT company_name FROM " + Employee.companyDetails + ";";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    public Map<String, String> getCompanyDetails(String companyName) throws SQLException {
        Map<String, String> map = new HashMap<>();
//        conn = dbManager.checkConnection(conn);
        sql = "SELECT * FROM " + Employee.companyDetails + " WHERE company_name = ?;";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1, companyName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            map.put("taxesCard", resultSet.getString("taxes_card"));
            map.put("commercialLog", resultSet.getString("commercial_log"));
            map.put("taxesFile", resultSet.getString("taxes_file"));
            map.put("salesTax", resultSet.getString("sales_tax"));
            map.put("currentAddedValue", resultSet.getString("current_sales_taxes_percent"));
        }
        return map;
    }

    public String arabitizeMyNumbers(String word) {
        String arabitizedWord = "";
        try {
            for (int i = 0; i < word.length(); i++) {
                if (myTableMode.isNum(String.valueOf((String.valueOf(word.charAt(i)))))) {

                    arabitizedWord += numberFormat.format(Integer.parseInt(String.valueOf(word.charAt(i))));
                } else {
                    arabitizedWord += word.charAt(i);
                }
            }
        } catch (NullPointerException ignored) {

        }

        return arabitizedWord;
    }

    public static void main(String[] args) throws SQLException {
        company_settings companySettings = new company_settings();

        new Employee();


        Employee.setLookAndFeelForMyProgram();
//        checkEmp.checkerConn = checkEmp.dbManager.checkConnection(checkEmp.checkerConn);
        JFrame frame = new JFrame();
        frame.add(companySettings.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        panel1.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:49px:noGrow,left:4dlu:noGrow,fill:d:grow,left:5dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:27px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 18));
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("إعدادات الشركات");
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xyw(1, 1, 9));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, 14));
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("إختر الشركة");
        panel1.add(label2, cc.xy(9, 3));
        drop_existingCompany = new JComboBox();
        panel1.add(drop_existingCompany, cc.xyw(3, 3, 5));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font(label3.getFont().getName(), Font.BOLD, 14));
        label3.setHorizontalAlignment(4);
        label3.setHorizontalTextPosition(4);
        label3.setText("بطاقة ضريبية(ب.ض)");
        panel1.add(label3, cc.xy(9, 7));
        final JLabel label4 = new JLabel();
        label4.setFont(new Font(label4.getFont().getName(), Font.BOLD, 14));
        label4.setHorizontalAlignment(4);
        label4.setHorizontalTextPosition(4);
        label4.setText("سجل تجارى (س.ت)");
        panel1.add(label4, cc.xy(9, 9));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font(label5.getFont().getName(), Font.BOLD, 14));
        label5.setHorizontalAlignment(4);
        label5.setHorizontalTextPosition(4);
        label5.setText("ملف ضريبي (م.ض)");
        panel1.add(label5, cc.xy(9, 11));
        final JLabel label6 = new JLabel();
        label6.setFont(new Font(label6.getFont().getName(), Font.BOLD, 14));
        label6.setHorizontalAlignment(4);
        label6.setHorizontalTextPosition(4);
        label6.setText("ضريبة مبيعات (ض.م)");
        panel1.add(label6, cc.xy(9, 13));
        final JLabel label7 = new JLabel();
        label7.setFont(new Font(label7.getFont().getName(), Font.BOLD, 14));
        label7.setHorizontalAlignment(4);
        label7.setHorizontalTextPosition(4);
        label7.setText("القيمة المضافة (%)");
        panel1.add(label7, cc.xy(9, 15));
        txt_taxesCard = new JTextField();
        panel1.add(txt_taxesCard, cc.xyw(4, 7, 4, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_commercialLog = new JTextField();
        panel1.add(txt_commercialLog, cc.xyw(4, 9, 4, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_taxesFile = new JTextField();
        panel1.add(txt_taxesFile, cc.xyw(3, 11, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_salesTaxes = new JTextField();
        panel1.add(txt_salesTaxes, cc.xyw(3, 13, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        txt_currentAddedValue = new JTextField();
        panel1.add(txt_currentAddedValue, cc.xy(7, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        btn_save = new JButton();
        btn_save.setHorizontalTextPosition(0);
        btn_save.setText("حفظ");
        panel1.add(btn_save, cc.xy(9, 17));
        lbl_newCompany = new JLabel();
        lbl_newCompany.setEnabled(false);
        lbl_newCompany.setFont(new Font(lbl_newCompany.getFont().getName(), Font.BOLD, 14));
        lbl_newCompany.setHorizontalAlignment(4);
        lbl_newCompany.setHorizontalTextPosition(4);
        lbl_newCompany.setText("أسم الشركة الجديدة");
        panel1.add(lbl_newCompany, cc.xy(9, 5));
        txt_newCompany = new JTextField();
        txt_newCompany.setEnabled(false);
        panel1.add(txt_newCompany, cc.xyw(3, 5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
