package Forms;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import DataBaseManageAndReport.MyMainConnection;
import Utils.MyDatePicker;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import tafqeet.tafqeetFactory;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Created by mohab 2 on 11/27/2016.
 */
public class CompanyBills_MainSettings {
    private JPanel panel1;
    private JTree tree_contracts;
    private JTextField txt_searchContracts;
    private JCheckBox chk_activeBill;
    private JTextField txt_billValue;
    private JTextArea txt_billDetailsTextArea;
    private JButton button1;
    private JLabel lbl_taxesValue;
    private JLabel lbl_totalBillValue;
    private JScrollPane scrl_tree;
    private JCheckBox chk_newContract;
    private JTextField txt_contractName;
    private JComboBox drop_sector;
    private JButton btn_delete;
    private JList list_bankAccounts;
    private JTextField txt_startDate;
    private JTextField txt_endDate;
    private JTextField txt_numberLabor;
    private JTextField txt_yearlyInc;
    private DatabaseManager dbMangager = new DatabaseManager();
    private PreparedStatement preparedStatement;
    private String secDept = DatabaseManager.SECURITY_DEPT;
    private boolean alreadyExistingContract = false;
    private String currentLocationName;
    private String currentDept;
    private String currentCity;
    private double currentTaxes = dbMangager.getTaxes();
    public int taxes_percent = 0;
    public String companyName = "";
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public CompanyBills_MainSettings() throws SQLException {
        new Employee();


//        mainSettings_contractsConn = dbMangager.checkConnection(mainSettings_contractsConn);
        String sqlInsertingData;

        if (Objects.equals(LoginPage.deptNameForUser, "النظافة")) {
            companyName = DatabaseManager.COMPANY_NAME_DEV;
        } else if (Objects.equals(LoginPage.deptNameForUser, "الحراسة")) {
            companyName = DatabaseManager.COMPANY_NAME_Security;
        }
        sqlInsertingData = "SELECT current_sales_taxes_percent FROM " + Employee.companyDetails + "" +
                " WHERE company_name = ?;";
        ResultSet rsInsertingData;
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sqlInsertingData);
        preparedStatement.setString(1, companyName);
        rsInsertingData = preparedStatement.executeQuery();

        while (rsInsertingData.next()) {
            taxes_percent = rsInsertingData.getInt("current_sales_taxes_percent");
        }

        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        scrl_tree.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        tree_contracts.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        decideWhichNodesWillAppearOnMyTree(companyName);

        txt_searchContracts.addActionListener(evt -> {
            TreeModel treeModel = tree_contracts.getModel();
            tree_contracts.setModel(null);
            tree_contracts.setModel(treeModel);

        });

        tree_contracts.setCellRenderer(new DefaultTreeCellRenderer() {
            private JLabel lblNull = new JLabel();

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                          boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

                Component c = super.getTreeCellRendererComponent(tree, value, arg2, arg3, arg4, arg5, arg6);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (matchesFilter(node)) {
                    c.setForeground(Color.BLACK);
                    return c;
                } else if (containsMatchingChild(node)) {
                    c.setForeground(Color.GRAY);
                    return c;
                } else {
                    return lblNull;
                }
            }

            private boolean matchesFilter(DefaultMutableTreeNode node) {
                return node.toString().contains(txt_searchContracts.getText());
            }

            private boolean containsMatchingChild(DefaultMutableTreeNode node) {
                Enumeration<DefaultMutableTreeNode> e = node.breadthFirstEnumeration();
                while (e.hasMoreElements()) {
                    if (matchesFilter(e.nextElement())) {
                        return true;
                    }
                }

                return false;
            }
        });


        button1.addActionListener(e -> {
            String sql;
            try {
//                mainSettings_contractsConn = dbMangager.checkConnection(mainSettings_contractsConn);

                if (alreadyExistingContract && !chk_newContract.isSelected()) {
                    sql = "UPDATE  " + Employee.locationsBills + " SET activeBill = ?, bill_value = ?,taxes=?, bill_Text= ? WHERE location_name = ? AND Department = ?;";
                    preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                    preparedStatement.setBoolean(1, chk_activeBill.isSelected());
                    try {
                        preparedStatement.setDouble(2, Double.parseDouble(txt_billValue.getText()));
                        preparedStatement.setDouble(3, (currentTaxes / 100) * Double.parseDouble(txt_billValue.getText()));
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(panel1, "الرجاء إدخال أرقام فقط في خانة قيمة الفاتورة .. ");
                    }
                    preparedStatement.setString(4, txt_billDetailsTextArea.getText());
                    preparedStatement.setString(5, currentLocationName);
                    preparedStatement.setString(6, currentDept);
                    preparedStatement.execute();
                    dbMangager.insertNewContractDataInDB("محافظة القاهرة", currentLocationName, Integer.parseInt(txt_numberLabor.getText()), 0,
                            txt_startDate.getText(), txt_endDate.getText(),
                            Integer.parseInt(txt_yearlyInc.getText()), currentDept, panel1, "update");


                } else if (!alreadyExistingContract && !chk_newContract.isSelected()) {
                    sql = "SELECT * FROM " + Employee.locationsTable + " Where location_name = ? AND Department = ?;";
                    preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                    preparedStatement.setString(1, currentLocationName);
                    preparedStatement.setString(2, currentDept);
                    ResultSet rs;
                    rs = preparedStatement.executeQuery();
                    rs.next();
                    currentCity = rs.getString("city_name");
                    sql = "INSERT INTO " + Employee.locationsBills + " VALUES (?,?,?,?,?,?,?,?);";
                    preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                    preparedStatement.setString(1, currentCity);
                    preparedStatement.setString(2, currentLocationName);
                    preparedStatement.setString(3, currentDept);
                    try {
                        preparedStatement.setDouble(4, Double.parseDouble(txt_billValue.getText()));
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(panel1, "الرجاء إدخال أرقام فقط في خانة قيمة الفاتورة .. ");
                    }
                    String sum = "";
                    DecimalFormat df = new DecimalFormat("#.##");
                    sum = String.valueOf((Double.parseDouble(txt_billValue.getText())));
                    sum = String.valueOf(df.format(Double.parseDouble(sum) + (Double.parseDouble(sum) * taxes_percent * .01)));
                    preparedStatement.setDouble(5, (currentTaxes / 100) * Double.parseDouble(txt_billValue.getText()));
                    preparedStatement.setString(6, txt_billDetailsTextArea.getText());
                    preparedStatement.setBoolean(7, chk_activeBill.isSelected());
                    preparedStatement.setString(8, new tafqeetFactory(sum).ArabicNumber);
                    preparedStatement.execute();
                    JOptionPane.showMessageDialog(panel1, "تم الحفظ بنجاح!");

                    TreeModel treeModel = tree_contracts.getModel();
                    tree_contracts.setModel(null);
                    tree_contracts.setModel(treeModel);
                    tree_contracts.removeAll();
                    decideWhichNodesWillAppearOnMyTree(companyName);
//                    	city_name	location_name	Department	bill_value	taxes	bill_Text	activeBill
                } else if (!alreadyExistingContract && chk_newContract.isSelected()) {
//
                    dbMangager.insertNewContractDataInDB("محافظة القاهرة", txt_contractName.getText(), Integer.parseInt(txt_numberLabor.getText()), 0,
                            txt_startDate.getText(), txt_endDate.getText(),
                            Integer.parseInt(txt_yearlyInc.getText()), drop_sector.getSelectedItem().toString(), panel1, "insert");

                    sql = "INSERT INTO " + Employee.locationsBills + " VALUES (?,?,?,?,?,?,?,?);";
//                    mainSettings_contractsConn = dbMangager.checkConnection(mainSettings_contractsConn);
                    //******************
                    preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                    preparedStatement.setString(1, "محافظة القاهرة");
                    preparedStatement.setString(2, txt_contractName.getText());
                    preparedStatement.setString(3, drop_sector.getSelectedItem().toString());
                    try {
                        preparedStatement.setDouble(4, Double.parseDouble(txt_billValue.getText()));
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(panel1, "الرجاء إدخال أرقام فقط في خانة قيمة الفاتورة .. ");
                    }
                    String sum = "";
                    DecimalFormat df = new DecimalFormat("#.##");
                    sum = String.valueOf((Double.parseDouble(txt_billValue.getText())));
                    sum = String.valueOf(df.format(Double.parseDouble(sum) + (Double.parseDouble(sum) * taxes_percent * .01)));
                    preparedStatement.setDouble(5, (currentTaxes / 100) * Double.parseDouble(txt_billValue.getText()));
                    preparedStatement.setString(6, txt_billDetailsTextArea.getText());
                    preparedStatement.setBoolean(7, chk_activeBill.isSelected());
                    preparedStatement.setString(8, new tafqeetFactory(sum).ArabicNumber);
                    preparedStatement.execute();

                    //**************************


                    TreeModel treeModel = tree_contracts.getModel();
                    tree_contracts.setModel(null);
                    tree_contracts.setModel(treeModel);
                    tree_contracts.removeAll();
                    decideWhichNodesWillAppearOnMyTree(companyName);

                }
            } catch (SQLException | ParseException e1) {
                e1.printStackTrace();
            }
        });

        tree_contracts.addTreeSelectionListener(e -> {
            alreadyExistingContract = false;
            TreePath currentPath = tree_contracts.getSelectionPath();
            String DeptName = "";

            if (currentPath != null) {
                DeptName = currentPath.getPathComponent(1).toString();

                String sql;
                ResultSet rs;

                if (currentPath.getPathCount() > 2) {
                    sql = "SELECT * FROM " + Employee.locationsBills + " Where location_name = ? AND Department = ? LIMIT 1;";
                    try {
//                        mainSettings_contractsConn = dbMangager.checkConnection(mainSettings_contractsConn);
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, currentPath.getPathComponent(2).toString());
                        preparedStatement.setString(2, DeptName);
                        currentLocationName = currentPath.getPathComponent(2).toString();
                        currentDept = DeptName;
                        rs = preparedStatement.executeQuery();
                        boolean isEmpty = !rs.first();
                        if (!isEmpty) {
                            chk_activeBill.setSelected(rs.getBoolean("activeBill"));
                            txt_billValue.setText(String.valueOf(rs.getDouble("bill_value")));
                            txt_billDetailsTextArea.setText(rs.getString("bill_Text"));
                            lbl_taxesValue.setText(String.valueOf(currentTaxes * .01 * rs.getDouble("bill_value")));
                            lbl_totalBillValue.setText(String.valueOf(currentTaxes * .01 * rs.getDouble("bill_value") + rs.getDouble("bill_value")));
                            alreadyExistingContract = true;
                        } else {
                            chk_activeBill.setSelected(false);
                            txt_billValue.setText("");
                            txt_billDetailsTextArea.setText("");
                            lbl_taxesValue.setText("");
                            lbl_totalBillValue.setText("");
                            alreadyExistingContract = false;
                        }
                        sql = "SELECT * FROM " + Employee.locationsTable + " Where location_name = ? AND Department = ? LIMIT 1;";
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, currentPath.getPathComponent(2).toString());
                        preparedStatement.setString(2, DeptName);
                        rs = preparedStatement.executeQuery();
                        isEmpty = !rs.first();
                        if (!isEmpty) {
                            txt_numberLabor.setText(String.valueOf(rs.getInt("NumberOfLabor")));
                            txt_startDate.setText(String.valueOf(df.format(
                                    (Objects.equals(rs.getDate("StartDate").toString(), "0000-00-00")) ? " " : rs.getDate("StartDate"))
                            ));
                            txt_endDate.setText(String.valueOf(df.format(
                                    (Objects.equals(rs.getDate("StartDate").toString(), "0000-00-00")) ? " " : rs.getDate("StartDate"))
                            ));
                            txt_yearlyInc.setText(String.valueOf(rs.getInt("YearlyIncrement")));
                            alreadyExistingContract = true;
                        } else {
                            txt_numberLabor.setText(" ");
                            txt_startDate.setText(" ");
                            txt_endDate.setText(" ");
                            txt_yearlyInc.setText(" ");
                            alreadyExistingContract = false;
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        chk_newContract.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chk_newContract.isSelected()) {
                    drop_sector.setEnabled(true);
                    txt_contractName.setEnabled(true);
                    txt_searchContracts.setEnabled(false);
                    tree_contracts.setEnabled(false);
                    txt_billValue.setText("");
                    txt_billDetailsTextArea.setText("");
                    lbl_taxesValue.setText("");
                    lbl_totalBillValue.setText("");
                } else {
                    drop_sector.setEnabled(false);
                    txt_contractName.setEnabled(false);
                    txt_searchContracts.setEnabled(true);
                    tree_contracts.setEnabled(true);

                }

            }
        });
        panel1.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                drop_sector.removeAllItems();
                if (Objects.equals(companyName, DatabaseManager.COMPANY_NAME_DEV)) {
                    drop_sector.removeAllItems();
                    drop_sector.addItem(DatabaseManager.CLEANING_DEPT);
                } else if (Objects.equals(companyName, DatabaseManager.COMPANY_NAME_Security)) {
                    drop_sector.removeAllItems();
                    drop_sector.addItem(DatabaseManager.SECURITY_DEPT);
                    drop_sector.addItem(DatabaseManager.MONEYTRANSFERE_DEPT);
                }
                if (chk_newContract.isSelected()) {
                    drop_sector.setEnabled(true);
                    txt_contractName.setEnabled(true);
                    txt_searchContracts.setEnabled(false);
                    tree_contracts.setEnabled(false);
                } else {
                    drop_sector.setEnabled(false);
                    txt_contractName.setEnabled(false);
                    txt_searchContracts.setEnabled(true);
                    tree_contracts.setEnabled(true);
                }
                String sql;
                try {
                    DefaultListModel listOfBanksForContractsModel = new DefaultListModel();
//                    mainSettings_contractsConn = dbMangager.checkConnection(mainSettings_contractsConn);
                    sql = "SELECT * FROM " + Employee.company_bank_accountsTbl + " WHERE company_name = ?;";
                    preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                    preparedStatement.setString(1, companyName);
                    ResultSet rs_banksToList = preparedStatement.executeQuery();


                    while (rs_banksToList.next()) {
                        listOfBanksForContractsModel.addElement(rs_banksToList.getString("Bank_name") + " : " + rs_banksToList.getString("bank_bill_sign"));
                    }
                    list_bankAccounts.setModel(listOfBanksForContractsModel);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });
        btn_delete.addActionListener(e -> {
            TreePath currentPath = tree_contracts.getSelectionPath();
            String DeptName = "";
            int deleteOrNot = JOptionPane.showConfirmDialog(panel1,
                    "هل أنت متأكد من انك تريد حذف التعاقد الذي تم تحديده؟");
            if (deleteOrNot == JOptionPane.YES_OPTION) {
                if (currentPath != null) {
                    DeptName = currentPath.getPathComponent(1).toString();
                    try {
//                        mainSettings_contractsConn = dbMangager.checkConnection(mainSettings_contractsConn);

                        String sql = "DELETE FROM " + Employee.locationsBills + " WHERE location_name = ? AND Department = ?";
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, currentPath.getPathComponent(2).toString());
                        preparedStatement.setString(2, DeptName);
                        preparedStatement.execute();
                        sql = "DELETE FROM " + Employee.locationsTable + " WHERE location_name = ? AND Department = ?";
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, currentPath.getPathComponent(2).toString());
                        preparedStatement.setString(2, DeptName);
                        preparedStatement.execute();
                        JOptionPane.showMessageDialog(panel1, "تم الحذف بنجاح!");

                        TreeModel treeModel = tree_contracts.getModel();
                        tree_contracts.setModel(null);
                        tree_contracts.setModel(treeModel);
                        tree_contracts.removeAll();
                        decideWhichNodesWillAppearOnMyTree(companyName);


                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1, "الرجاء تحديد تعاقد للحذف!");
                }
            }
        });
        txt_billValue.addActionListener(e -> {
            lbl_taxesValue.setText(String.valueOf(Double.parseDouble(txt_billValue.getText())
                    * taxes_percent * .01));
            lbl_totalBillValue.setText(String.valueOf(Double.parseDouble(lbl_taxesValue.getText()) + Double.parseDouble(txt_billValue.getText())));
        });

        txt_billValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    lbl_taxesValue.setText(String.valueOf(Double.parseDouble(txt_billValue.getText()) * taxes_percent * .01));
                    lbl_totalBillValue.setText(String.valueOf(Double.parseDouble(lbl_taxesValue.getText()) + Double.parseDouble(txt_billValue.getText())));
                } catch (NumberFormatException ignored) {

                }
            }


        });

        txt_startDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new MyDatePicker(txt_startDate);

            }
        });
        txt_endDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new MyDatePicker(txt_endDate);

            }
        });
    }

    private void decideWhichNodesWillAppearOnMyTree(String companyName) throws SQLException {
        DefaultTreeModel model = (DefaultTreeModel) tree_contracts.getModel();
        TreeNode node = new DefaultMutableTreeNode(companyName);
        model.setRoot(node);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        if (Objects.equals(companyName, DatabaseManager.COMPANY_NAME_DEV)) {
            DefaultMutableTreeNode cleaningNode = new DefaultMutableTreeNode(DatabaseManager.CLEANING_DEPT);
            root.add(cleaningNode);
            populateMyTreeWithContracts(null, cleaningNode, null);
        } else if (Objects.equals(companyName, DatabaseManager.COMPANY_NAME_Security)) {
            DefaultMutableTreeNode securityNode = new DefaultMutableTreeNode(DatabaseManager.SECURITY_DEPT);
            DefaultMutableTreeNode moneyTransfereNode = new DefaultMutableTreeNode(DatabaseManager.MONEYTRANSFERE_DEPT);
            root.add(securityNode);
            root.add(moneyTransfereNode);
            populateMyTreeWithContracts(securityNode, null, moneyTransfereNode);
        }
        model.reload();
    }

    private void populateMyTreeWithContracts(DefaultMutableTreeNode security,
                                             DefaultMutableTreeNode cleaning, DefaultMutableTreeNode MT) throws SQLException {
        ResultSet rs;
//        conn = dbMangager.checkConnection(conn);
        String deptOrganizer = "";
        String deptOrganizerSec = "";
        String deptOrganizerMoneyT = "";
        if (security != null) {
            deptOrganizerSec = "الحراسة";
            deptOrganizer = "نقل الأموال";
        } else if (cleaning != null) {
            deptOrganizer = "النظافة";
        }
        String sql = "SELECT * FROM " + Employee.locationsTable + " WHERE Department=? OR Department = ?;";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1, deptOrganizer);
        preparedStatement.setString(2, deptOrganizerSec);
        rs = preparedStatement.executeQuery();
        while (rs.next()) switch (rs.getString("Department")) {
            case DatabaseManager.SECURITY_DEPT:
                security.add(new DefaultMutableTreeNode(rs.getString("location_name")));
                break;
            case DatabaseManager.CLEANING_DEPT:
                cleaning.add(new DefaultMutableTreeNode(rs.getString("location_name")));
                break;
            case DatabaseManager.MONEYTRANSFERE_DEPT:
                MT.add(new DefaultMutableTreeNode(rs.getString("location_name")));
                break;
        }
    }


    public static void main(String[] args) throws SQLException {
        CompanyBills_MainSettings companyBills_mainSettings = new CompanyBills_MainSettings();
        Employee.setLookAndFeelForMyProgram();
        JFrame frame = new JFrame();
        frame.add(companyBills_mainSettings.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
        frame.setSize(1020, 420);
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:44dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,fill:max(d;4px):noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,left:104dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:61px:noGrow,left:4dlu:noGrow,fill:77px:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,left:4dlu:noGrow,fill:143px:grow,fill:max(d;4px):noGrow,left:4dlu:noGrow,left:124dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "top:4dlu:noGrow,center:max(d;4px):noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:20dlu:grow,top:4dlu:noGrow,center:27px:noGrow,top:4dlu:noGrow,top:4dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:max(d;4px):noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), label1.getFont().getStyle(), 16));
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(0);
        label1.setText("بحث بالأسم");
        CellConstraints cc = new CellConstraints();
        panel1.add(label1, cc.xy(21, 5));
        txt_searchContracts = new JTextField();
        panel1.add(txt_searchContracts, cc.xy(20, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font(label2.getFont().getName(), label2.getFont().getStyle(), 16));
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("تفعيل التعاقد");
        panel1.add(label2, cc.xy(17, 7));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font(label3.getFont().getName(), label3.getFont().getStyle(), 16));
        label3.setHorizontalAlignment(4);
        label3.setText("قيمة الفاتورة بدون ضريبة");
        panel1.add(label3, cc.xy(17, 9));
        chk_activeBill = new JCheckBox();
        chk_activeBill.setHorizontalAlignment(0);
        chk_activeBill.setHorizontalTextPosition(0);
        chk_activeBill.setText("");
        panel1.add(chk_activeBill, cc.xy(14, 7));
        txt_billValue = new JTextField();
        txt_billValue.setColumns(10);
        txt_billValue.setText("");
        panel1.add(txt_billValue, cc.xy(14, 9, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setFont(new Font(label4.getFont().getName(), label4.getFont().getStyle(), 16));
        label4.setHorizontalAlignment(4);
        label4.setText("النص داخل الفاتورة");
        panel1.add(label4, cc.xy(17, 11));
        lbl_taxesValue = new JLabel();
        lbl_taxesValue.setHorizontalAlignment(4);
        lbl_taxesValue.setHorizontalTextPosition(0);
        lbl_taxesValue.setText("");
        panel1.add(lbl_taxesValue, cc.xy(13, 9));
        lbl_totalBillValue = new JLabel();
        lbl_totalBillValue.setHorizontalAlignment(4);
        lbl_totalBillValue.setHorizontalTextPosition(0);
        lbl_totalBillValue.setText("");
        panel1.add(lbl_totalBillValue, cc.xy(11, 9));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font(label5.getFont().getName(), Font.BOLD, label5.getFont().getSize()));
        label5.setHorizontalAlignment(4);
        label5.setText("القيمة المضافة");
        panel1.add(label5, cc.xy(13, 7));
        final JLabel label6 = new JLabel();
        label6.setFont(new Font(label6.getFont().getName(), Font.BOLD, label6.getFont().getSize()));
        label6.setHorizontalAlignment(4);
        label6.setHorizontalTextPosition(4);
        label6.setText("الإجمالى");
        panel1.add(label6, cc.xy(11, 7));
        scrl_tree = new JScrollPane();
        panel1.add(scrl_tree, cc.xywh(20, 7, 3, 8, CellConstraints.FILL, CellConstraints.FILL));
        tree_contracts = new JTree();
        tree_contracts.putClientProperty("JTree.lineStyle", "");
        scrl_tree.setViewportView(tree_contracts);
        chk_newContract = new JCheckBox();
        chk_newContract.setHideActionText(false);
        chk_newContract.setHorizontalAlignment(4);
        chk_newContract.setHorizontalTextPosition(4);
        chk_newContract.setText("إضافة تعاقد جديد");
        panel1.add(chk_newContract, cc.xy(21, 3));
        final JLabel label7 = new JLabel();
        label7.setFont(new Font(label7.getFont().getName(), Font.BOLD, 18));
        label7.setHorizontalAlignment(4);
        label7.setHorizontalTextPosition(0);
        label7.setText("إعدادات التعاقد:");
        panel1.add(label7, cc.xy(17, 5));
        txt_contractName = new JTextField();
        panel1.add(txt_contractName, cc.xy(20, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        drop_sector = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        drop_sector.setModel(defaultComboBoxModel1);
        panel1.add(drop_sector, cc.xy(17, 3));
        list_bankAccounts = new JList();
        list_bankAccounts.setEnabled(true);
        panel1.add(list_bankAccounts, cc.xywh(2, 9, 7, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, cc.xywh(11, 11, 4, 5, CellConstraints.FILL, CellConstraints.FILL));
        txt_billDetailsTextArea = new JTextArea();
        txt_billDetailsTextArea.setLineWrap(true);
        txt_billDetailsTextArea.setText("");
        scrollPane1.setViewportView(txt_billDetailsTextArea);
        button1 = new JButton();
        button1.setFont(new Font(button1.getFont().getName(), Font.BOLD, 12));
        button1.setText("حفظ");
        panel1.add(button1, cc.xy(14, 21));
        btn_delete = new JButton();
        btn_delete.setText("حذف تعاقد");
        panel1.add(btn_delete, cc.xy(20, 21));
        final JLabel label8 = new JLabel();
        label8.setFont(new Font(label8.getFont().getName(), Font.BOLD, 16));
        label8.setText("بداية التعاقد");
        panel1.add(label8, cc.xy(17, 17, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        txt_startDate = new JTextField();
        txt_startDate.setColumns(9);
        txt_startDate.setEditable(false);
        txt_startDate.setToolTipText("dd-MM-yyyy");
        panel1.add(txt_startDate, cc.xyw(13, 17, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label9 = new JLabel();
        label9.setFont(new Font(label9.getFont().getName(), Font.BOLD, 16));
        label9.setText("نهاية التعاقد");
        panel1.add(label9, cc.xyw(11, 17, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        txt_endDate = new JTextField();
        txt_endDate.setColumns(9);
        txt_endDate.setEditable(false);
        txt_endDate.setToolTipText("dd-MM-yyyy");
        panel1.add(txt_endDate, cc.xyw(6, 17, 4, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label10 = new JLabel();
        label10.setFont(new Font(label10.getFont().getName(), Font.BOLD, 16));
        label10.setText("عدد الأفراد");
        panel1.add(label10, cc.xy(17, 19, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        txt_numberLabor = new JTextField();
        txt_numberLabor.setColumns(3);
        panel1.add(txt_numberLabor, cc.xyw(13, 19, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        txt_yearlyInc = new JTextField();
        txt_yearlyInc.setColumns(3);
        panel1.add(txt_yearlyInc, cc.xy(8, 19, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label11 = new JLabel();
        label11.setFont(new Font(label11.getFont().getName(), Font.BOLD, 16));
        label11.setText("زيادة سنوية");
        panel1.add(label11, cc.xyw(11, 19, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label12 = new JLabel();
        label12.setFont(new Font(label12.getFont().getName(), Font.BOLD, 16));
        label12.setText("%");
        panel1.add(label12, cc.xy(6, 19));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
