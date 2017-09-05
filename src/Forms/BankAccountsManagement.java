package Forms;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import DataBaseManageAndReport.MyMainConnection;
import TablesDesign.TableCellRendererRight;
import TablesDesign.myTableMode;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by mohab 2 on 12/12/2016.
 */
public class BankAccountsManagement {
    private JPanel panel1;
    private JComboBox drop_company_name;
    private JTextField txt_bankName;
    private JTextField txt_accountNumber;
    private JTextField txt_billSign;
    private JTable tbl_bankAccounts;
    private JButton btn_saveBankAccount;
    private JScrollPane jscrollPane_bankAccounts;
    private JComboBox drop_bankName;
    private JButton حذفتحديدButton;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
    private String sql;
    private PreparedStatement preparedStatement;
    private List companyNamesGetterRS;

    private void optimizeTheLookOfmyMain(JTable currentTable) {
        JTableHeader th = currentTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc;
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i = 0; i < currentTable.getColumnCount(); i++) {
            currentTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        tc = tcm.getColumn(0);
        tc.setHeaderValue("إسم الشركة");
        tc = tcm.getColumn(1);
        tc.setHeaderValue("إسم البنك");
        tc = tcm.getColumn(2);
        tc.setHeaderValue("رقم الحساب");
        tc = tcm.getColumn(3);
        tc.setHeaderValue("رمز بيــان الفاتورة");


        th.repaint();
        TableColumnModel retrMod = currentTable.getColumnModel();
        TableColumn retrCol = retrMod.getColumn(2);

        retrCol.setHeaderRenderer(new TableCellRendererRight());
    }

    public BankAccountsManagement() throws SQLException {
        new Employee();
        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        for (Component c : jscrollPane_bankAccounts.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        tbl_bankAccounts.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        panel1.addHierarchyListener(e -> {
            JComponent component = (JComponent) e.getSource();
            drop_company_name.removeAllItems();
            drop_bankName.removeAllItems();

            if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                    && component.isShowing()) {
                try {
                    companyNamesGetterRS = getCompanyNames();


                    for (int i = 0; i < companyNamesGetterRS.getItemCount(); i++) {
                        if (Objects.equals(LoginPage.deptNameForUser, "النظافة")) {
                            if (!Objects.equals(companyNamesGetterRS.getItem(i), DatabaseManager.COMPANY_NAME_Security)) {
                                drop_company_name.addItem(companyNamesGetterRS.getItem(i));
                            }
                        } else if (Objects.equals(LoginPage.deptNameForUser, "الحراسة")) {
                            if (!Objects.equals(companyNamesGetterRS.getItem(i), DatabaseManager.COMPANY_NAME_DEV)) {
                                drop_company_name.addItem(companyNamesGetterRS.getItem(i));
                            }
                        }

                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


            }

        });
        drop_company_name.addItemListener(e -> {
            ResultSet newRsForBankDetails;
            drop_bankName.removeAllItems();
            if (e.getStateChange() == ItemEvent.SELECTED) {

                try {
//                    bacnkAccount_settingsConn = dbManager.checkConnection(bacnkAccount_settingsConn);
                    newRsForBankDetails = getBanksDetails(drop_company_name.getSelectedItem().toString()
                            , "", false);
                    while (newRsForBankDetails.next()) {
                        drop_bankName.addItem(newRsForBankDetails.getString("Bank_name"));
                    }
                    drop_bankName.addItem("إضافة بنك جديد....");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                try {
                    updateBankAccountsTable();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                optimizeTheLookOfmyMain(tbl_bankAccounts);
            }
        });
        drop_bankName.addItemListener(e -> {
            ResultSet newRsForBankDetails;

            if (e.getStateChange() == ItemEvent.SELECTED) {

                if (drop_bankName.getSelectedItem().equals("إضافة بنك جديد....")) {
                    txt_bankName.setText("");
                    txt_billSign.setText("");
                    txt_accountNumber.setText("");
                } else {
                    try {
                        newRsForBankDetails = getBanksDetails(drop_company_name.getSelectedItem().toString()
                                , drop_bankName.getSelectedItem().toString(), true);
                        while (newRsForBankDetails.next()) {
                            txt_bankName.setText(newRsForBankDetails.getString("Bank_name"));
                            txt_accountNumber.setText(newRsForBankDetails.getString("BankAccount_number"));
                            txt_billSign.setText(newRsForBankDetails.getString("bank_bill_sign"));

                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    updateBankAccountsTable();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                optimizeTheLookOfmyMain(tbl_bankAccounts);
            }
        });
        btn_saveBankAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (drop_bankName.getSelectedItem().equals("إضافة بنك جديد....")) {

                    try {
                        sql = "INSERT INTO " + Employee.company_bank_accountsTbl + " Values(?,?,?,?)";
//                        bacnkAccount_settingsConn = dbManager.checkConnection(bacnkAccount_settingsConn);
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, drop_company_name.getSelectedItem().toString());
                        preparedStatement.setString(2, txt_bankName.getText());
                        preparedStatement.setString(3, txt_accountNumber.getText());
                        preparedStatement.setString(4, txt_billSign.getText());
                        preparedStatement.execute();
                        updateComboBoxOfBankNames();
                        JOptionPane.showMessageDialog(panel1, "تم إضافة الحساب البنكى بنجاح!");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        String oldBillSign = "====";
//                        bacnkAccount_settingsConn = dbManager.checkConnection(bacnkAccount_settingsConn);

                        sql = "SELECT  bank_bill_sign FROM " + Employee.company_bank_accountsTbl + " WHERE Bank_name = ? AND company_name = ? LIMIT 1;";
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, txt_bankName.getText());
                        preparedStatement.setString(2, drop_company_name.getSelectedItem().toString());
                        ResultSet billSignRS = preparedStatement.executeQuery();
                        while (billSignRS.next()) {
                            oldBillSign = billSignRS.getString("bank_bill_sign");
                        }
                        sql = "UPDATE " + Employee.locationsBills + " SET bill_text=REPLACE(bill_text,?,?) ;";
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, oldBillSign);
                        preparedStatement.setString(2, txt_billSign.getText());
                        preparedStatement.execute();

                        sql = "UPDATE " + Employee.company_bank_accountsTbl + " SET Bank_name = ? ,BankAccount_number=?,bank_bill_sign=? WHERE" +
                                " Bank_name = ? AND company_name= ?;";

                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, txt_bankName.getText());
                        preparedStatement.setString(2, txt_accountNumber.getText());
                        preparedStatement.setString(3, txt_billSign.getText());
                        preparedStatement.setString(4, drop_bankName.getSelectedItem().toString());
                        preparedStatement.setString(5, drop_company_name.getSelectedItem().toString());
                        preparedStatement.execute();
                        updateComboBoxOfBankNames();
                        JOptionPane.showMessageDialog(panel1, "تم تحديث بيانات الحساب البنكى بنجاح!");
                        updateBankAccountsTable();
                        optimizeTheLookOfmyMain(tbl_bankAccounts);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
        حذفتحديدButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tbl_bankAccounts.getSelectedRows().length < 1) {
                    JOptionPane.showMessageDialog(panel1, "الرجاء إختيار بنك من الجدول !");
                } else if (tbl_bankAccounts.getSelectedRows().length >= 1) {
                    int deleteReally = JOptionPane.showConfirmDialog(panel1, "هل أنت متأكد من حذف الحسابات البنكية المختارة؟");
                    if (deleteReally == JOptionPane.YES_OPTION) {
                        try {
                            sql = "DELETE FROM " + Employee.company_bank_accountsTbl + " WHERE company_name = ? AND Bank_name =? AND BankAccount_number=?;";
//                            bacnkAccount_settingsConn = dbManager.checkConnection(bacnkAccount_settingsConn);
                            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                            for (int i = 0; i < tbl_bankAccounts.getSelectedRows().length; i++) {

                                preparedStatement.setString(1, String.valueOf(tbl_bankAccounts.getValueAt(tbl_bankAccounts.getSelectedRows()[i], 0)));
                                preparedStatement.setString(2, String.valueOf(tbl_bankAccounts.getValueAt(tbl_bankAccounts.getSelectedRows()[i], 1)));
                                preparedStatement.setString(3, String.valueOf(tbl_bankAccounts.getValueAt(tbl_bankAccounts.getSelectedRows()[i], 2)));
                                preparedStatement.execute();
                            }
                            updateBankAccountsTable();


                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void updateComboBoxOfBankNames() {
        drop_bankName.removeAllItems();
        ResultSet newRsForBankDetails;

        try {
//            bacnkAccount_settingsConn = dbManager.checkConnection(bacnkAccount_settingsConn);
            newRsForBankDetails = getBanksDetails(drop_company_name.getSelectedItem().toString(), "", false);
            while (newRsForBankDetails.next()) {
                drop_bankName.addItem(newRsForBankDetails.getString("Bank_name"));
            }
            drop_bankName.addItem("إضافة بنك جديد....");
            updateBankAccountsTable();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }


    public List getCompanyNames() throws SQLException {
        List companyNamesList = new List();
        sql = "SELECT company_name FROM " + Employee.companyDetails + ";";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        companyNamesList.removeAll();
        while (rs.next()) {
            companyNamesList.add(rs.getString("company_name"));
        }
        return companyNamesList;
    }

    public ResultSet getBanksDetails(String companyName, String BankName, boolean considerBankName) throws SQLException {

//        conn = dbManager.checkConnection(conn);
        if (considerBankName) {
            sql = "SELECT * FROM " + Employee.company_bank_accountsTbl + " WHERE company_name = ? AND Bank_name = ?;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, companyName);
            preparedStatement.setString(2, BankName);
        } else {
            sql = "SELECT * FROM " + Employee.company_bank_accountsTbl + " WHERE company_name = ?;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, companyName);
        }
        return preparedStatement.executeQuery();
    }

    public void updateBankAccountsTable() throws SQLException {
        String companyName = drop_company_name.getSelectedItem().toString();
        sql = "SELECT * FROM " + Employee.company_bank_accountsTbl + " WHERE company_name = ?;";
//        bacnkAccount_settingsConn = dbManager.checkConnection(bacnkAccount_settingsConn);
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1, companyName);
        tbl_bankAccounts.setModel(myTableMode.buildTableModel(preparedStatement.executeQuery()));
    }

    public static void main(String[] args) throws SQLException {
        BankAccountsManagement bankAccountsManagement = new BankAccountsManagement();
        Employee.setLookAndFeelForMyProgram();
        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        JFrame frame = new JFrame();
        frame.add(bankAccountsManagement.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setTitle("إدارة الحسابات البنكية");
        frame.setVisible(true);

        frame.setBounds(screenWidth / 3, screenHeight / 5, 420, 500);
        frame.setSize(600, 620);
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
        panel1.setLayout(new FormLayout("fill:102px:noGrow,left:6dlu:noGrow,fill:312px:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:23px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(4);
        label1.setText("اختار الشركة");
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xy(5, 5));
        drop_company_name = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        drop_company_name.setModel(defaultComboBoxModel1);
        panel1.add(drop_company_name, cc.xy(3, 5));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("أسم البنك");
        panel1.add(label2, cc.xy(5, 9));
        txt_bankName = new JTextField();
        txt_bankName.setEnabled(true);
        txt_bankName.setHorizontalAlignment(4);
        panel1.add(txt_bankName, cc.xy(3, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        label3.setHorizontalTextPosition(4);
        label3.setText("رقم الحساب");
        panel1.add(label3, cc.xy(5, 11));
        txt_accountNumber = new JTextField();
        txt_accountNumber.setEnabled(true);
        txt_accountNumber.setHorizontalAlignment(4);
        panel1.add(txt_accountNumber, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        label4.setHorizontalTextPosition(4);
        label4.setText("رمز بيان الفاتورة");
        panel1.add(label4, cc.xy(5, 13));
        txt_billSign = new JTextField();
        txt_billSign.setEnabled(true);
        txt_billSign.setHorizontalAlignment(4);
        panel1.add(txt_billSign, cc.xy(3, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font(label5.getFont().getName(), Font.BOLD, 18));
        label5.setHorizontalAlignment(0);
        label5.setHorizontalTextPosition(0);
        label5.setText("إدارة الحسابات البنكية");
        panel1.add(label5, cc.xy(3, 3));
        jscrollPane_bankAccounts = new JScrollPane();
        panel1.add(jscrollPane_bankAccounts, cc.xyw(3, 15, 3, CellConstraints.FILL, CellConstraints.FILL));
        tbl_bankAccounts = new JTable();
        jscrollPane_bankAccounts.setViewportView(tbl_bankAccounts);
        btn_saveBankAccount = new JButton();
        btn_saveBankAccount.setText("حفظ");
        panel1.add(btn_saveBankAccount, cc.xy(1, 13));
        drop_bankName = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("");
        defaultComboBoxModel2.addElement("إضافة بنك جديد ...");
        drop_bankName.setModel(defaultComboBoxModel2);
        panel1.add(drop_bankName, cc.xy(3, 7));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(4);
        label6.setHorizontalTextPosition(4);
        label6.setText("اختار إسم البنك");
        panel1.add(label6, cc.xy(5, 7));
        حذفتحديدButton = new JButton();
        حذفتحديدButton.setText("حذف تحديد");
        panel1.add(حذفتحديدButton, cc.xy(1, 15));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
