package Forms;

import DataBaseManageAndReport.*;
import ExcelManagement.ExcelFile;
import TablesDesign.TableCellRendererCenter;
import TablesDesign.myTableMode;
import Utils.JOptionDateInputWithValidation;
import Utils.MyProgressBar;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import tafqeet.tafqeetFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static Utils.NumberManagementAR_EN.convertArabicNumbertoEnglishNumber;

/**
 * Created by mohab 2 on 30/11/2016.
 */
public class PrintBills {
    private JPanel panel1;
    private JTable tbl_Monthlybills;
    private JButton btn_print;
    private JButton btn_preview;
    private JScrollPane JScrollPane1;
    private JTable tbl_main;
    private JScrollPane jscrol__main;
    private JButton btn_addOne;
    private JButton btn_retrieveOne;
    private JComboBox drop_monthPrint;
    private JComboBox drop_yearPrint;
    private JLabel lbl_billValue;
    private JLabel lbl_salesTaxes;
    private JLabel lbl_generalTotal;
    private JButton طباعةكشفتسليمButton;
    private JButton طباعةكشفمبيعاتButton;
    private JButton btn_saveToFile;
    private PreparedStatement preparedStatement;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
    private String sql;
    private JTextField hiddenTextFieldForPrintingDate = new JTextField("");
    String dateOfPrinting = "";
    public int lastIDvalue;
    public static String monthOfPrinting = "";
    public static String YearOfPrinting = "";
    public int taxes_percent = 0;
    String[] monthsOftheYear = new String[]{"يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "اكتوبر", "نوفمبر", "ديسمبر"};
    NumberFormat nf_us = NumberFormat.getInstance(Locale.US);
    public String bill_table_filter = "SELECT id,location_name,bill_value,taxes,bill_Text,Department FROM " + Employee.locationsBillsMonthly + " WHERE DateOfPrinting LIKE ? AND Department in(?,?) ORDER BY id ASC ;";
    public static String requestingTheDate = "";
    public String companyName = "";
    public static String _dept = "";
    public static String sec_dept = "";
    public String currentYear = "";
    public boolean currentModifiedYear = false;
    public String currentlyMonth = "";
    public String previousToCurrentMonth = "";
    SwingWorker sw;

    public PrintBills() throws SQLException {

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem refreshBills = new JMenuItem("تحديث الفواتير");
        JMenuItem editBill_ID = new JMenuItem("تعديل رقم الفاتورة");
        popupMenu.add(refreshBills);
        popupMenu.add(editBill_ID);
        tbl_Monthlybills.setComponentPopupMenu(popupMenu);

        drop_monthPrint.removeAllItems();
        drop_yearPrint.removeAllItems();
        for (int i = 0; i < monthsOftheYear.length; i++) {
            drop_monthPrint.addItem(monthsOftheYear[i]);
        }
        for (int i = 2000; i <= 2050; i++) {
            drop_yearPrint.addItem(i);
        }
        drop_monthPrint.setSelectedIndex(0);
        drop_yearPrint.setSelectedItem(2017);
//        printBillsConn = dbManager.checkConnection(printBillsConn);
        String sqlInsertingData;

        sqlInsertingData = "SELECT current_sales_taxes_percent FROM " + Employee.companyDetails + "" +
                " WHERE company_name = ?;";
        if (Objects.equals(LoginPage.deptNameForUser, "الحراسة")) {
            companyName = DatabaseManager.COMPANY_NAME_Security;
            sec_dept = "الحراسة";
            _dept = "نقل الأموال";
        } else if (Objects.equals(LoginPage.deptNameForUser, "النظافة")) {
            companyName = DatabaseManager.COMPANY_NAME_DEV;
            _dept = "النظافة";
        }
        ResultSet rsInsertingData;
//        if (printBillsConn == null) {
//            printBillsConn = dbManager.checkConnection(printBillsConn);
//        }
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
        for (Component c : JScrollPane1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        for (Component c : jscrol__main.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        tbl_main.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int col = tbl_main.columnAtPoint(e.getPoint());
                if (col == 0) {
                    try {
                        populateMainTable(tbl_main);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    optimizeTheLookOfmyMain(tbl_main);
                }
            }
        });
        tbl_Monthlybills.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int col = tbl_Monthlybills.columnAtPoint(e.getPoint());
                if (col == 0) {
                    String requested_date;
                    requested_date = "%";
                    requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
                    requestingTheDate = requested_date;
                    try {
                        populateBillANDOptimizeTheLookOfTable_progressBar();

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        tbl_Monthlybills.setAutoCreateRowSorter(true);
        tbl_Monthlybills.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        tbl_main.setAutoCreateRowSorter(true);
        tbl_main.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        panel1.addHierarchyListener(e -> {
            JComponent component = (JComponent) e.getSource();

            if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                    && component.isShowing()) {
                try {
                    tbl_Monthlybills.setDragEnabled(false);
                    tbl_main.removeAll();
                    tbl_Monthlybills.removeAll();
                    String requested_date;

                    requested_date = "%";
                    requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
                    requestingTheDate = requested_date;

                    populate_updateSum_optimizeLook_JTable_progressBar(false);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        btn_preview.addActionListener(e -> {
            JasperPrint masterJP = null;
            sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    new MyProgressBar();
                    reviewABill_progressBar(masterJP);
                    return null;
                }

                @Override
                public void done() {
                    MyProgressBar.disposeTheBar();
                }
            };
            sw.execute();
        });
        btn_print.addActionListener(e -> {
            JasperPrint masterJP = null;
            sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    new MyProgressBar();
                    long startTime = System.nanoTime();
                    printBills_progressBar(masterJP);
                    long endTime = System.nanoTime();
                    System.out.println("Printing bills took " + (endTime - startTime) + "ms");


                    return null;
                }

                @Override
                public void done() {
                    MyProgressBar.disposeTheBar();
                }
            };
            sw.execute();
        });
        btn_addOne.addActionListener(e -> {
            if (tbl_main.getSelectedRows().length > 0) {
                int confirm = JOptionPane.showConfirmDialog(panel1, "هل أنت متأكد من إضافة هذه الفاتورة؟", "تأكيد", JOptionPane.OK_CANCEL_OPTION);
                if (confirm == JOptionPane.OK_OPTION) {

//            dateOfPrinting = JOptionPane.showInputDialog(panel1, "ادخل تاريخ تحرير الفاتورة / الفواتير :", "طباعة الفواتير", 2);
                    dateOfPrinting = new JOptionDateInputWithValidation().gimmeTheCorrectDate();
                    showProgressBarOnUpdatingTables(false);
                } else {
                    dateOfPrinting = "";
                    JOptionPane.showMessageDialog(panel1, "تم إلغاء عملية الإضافة!");
                }
            } else {
                JOptionPane.showMessageDialog(panel1, "قم بتحديد فاتورة واحدة علي الأقل لإضافتها!");
            }
        });
        btn_retrieveOne.addActionListener(e -> {
            if (tbl_Monthlybills.getSelectedRows().length > 0) {
                int confirmDelete = JOptionPane.showConfirmDialog(panel1, "سيتم الحذف الفاتورة / الفواتير التي تم تحديدها من جدول الشهر الحالي, لا يمكن التراجع بعد اتمام العملية!", "تحذير", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmDelete == JOptionPane.OK_OPTION) {
                    String requested_date;
                    requested_date = "%";
                    requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
                    requestingTheDate = requested_date;
                    sql = "DELETE FROM " + Employee.locationsBillsMonthly + " WHERE location_name= ? AND Department = ? AND id = ? AND DateOfPrinting LIKE '" + requested_date + "'";
                    try {
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        for (int i = 0; i < tbl_Monthlybills.getSelectedRows().length; i++) {
                            preparedStatement.setString(1, String.valueOf(tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 2)));
                            preparedStatement.setString(2, String.valueOf(tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 6)));
                            preparedStatement.setString(3, String.valueOf(convertArabicNumbertoEnglishNumber(String.valueOf(
                                    tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 1))
                                    ))
                            );
                            preparedStatement.execute();
                        }
                        showProgressBarOnUpdatingTables(true);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1, "تم إلغاء العملية بنجاح!");
                }
            } else {
                JOptionPane.showMessageDialog(panel1, "قم بتحديد فاتورة واحدة علي الأقل لحذفها!");
            }
        });

        drop_monthPrint.addItemListener(e -> {
            String requested_date;
            if (e.getStateChange() == ItemEvent.SELECTED) {
                requested_date = "%";
                requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
                requestingTheDate = requested_date;
                try {
                    populateBillANDOptimizeTheLookOfTable_progressBar();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            lblSumsSetters();
        });
        drop_yearPrint.addItemListener(e -> {

            String requested_date;

            if (e.getStateChange() == ItemEvent.SELECTED) {
                requested_date = "%";
                requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
                requestingTheDate = requested_date;

                try {
                    populateBillANDOptimizeTheLookOfTable_progressBar();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        طباعةكشفتسليمButton.addActionListener(e -> {
            try {
                new TotalBillsReportGenerator(tbl_Monthlybills, true);
            } catch (SQLException | FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (JRException e1) {
                e1.printStackTrace();
            }
        });
        طباعةكشفمبيعاتButton.addActionListener(e -> {
            try {
                new TotalBillsReportGenerator(tbl_Monthlybills, false);
            } catch (SQLException | FileNotFoundException | JRException e1) {
                e1.printStackTrace();
            }
        });

        btn_saveToFile.addActionListener(e -> {
            try {
                new ExcelFile(tbl_Monthlybills);
            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            }
        });
        //Menu buttons actions
        refreshBills.addActionListener(e -> {
            if (tbl_Monthlybills.getSelectedRows().length > 0) {
                int confirmUpdate = JOptionPane.showConfirmDialog(panel1, "سيتم تحديث بيانات الفاتورة / الفواتير التي تم تحديدها مباشرة من إدارة التعاقدات , لا يمكن التراجع بعد اتمام العملية!", "تحذير", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmUpdate == JOptionPane.OK_OPTION) {
                    sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            new MyProgressBar();
                            updateBills_MENU_OPTION_progressBar();
                            populateBillANDOptimizeTheLookOfTable_withOUTprogressBar();
                            return null;
                        }

                        private void populateBillANDOptimizeTheLookOfTable_withOUTprogressBar() {
                            try {
                                populateBillsTable(tbl_Monthlybills, bill_table_filter);
                                optimizeTheLookOfmyTable(tbl_Monthlybills);
                                lblSumsSetters();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                        }

                        @Override
                        public void done() {
                            MyProgressBar.disposeTheBar();
                        }
                    };
                    sw.execute();

                }
            } else {
                JOptionPane.showMessageDialog(panel1, "برجاء إختيار فاتورة علي الأقل", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //Edit Bill id menu option
        editBill_ID.addActionListener(e -> {
            sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    new MyProgressBar();
                    MyProgressBar.jProgressBar.setIndeterminate(true);
                    editBillID_MENU_OPTION_updateTheTables_progressBar();
                    return null;
                }

                @Override
                public void done() {
                    MyProgressBar.disposeTheBar();
                }
            };
            sw.execute();

        });
    }

    private void updateBills_MENU_OPTION_progressBar() {
        String updateBillsTableSQL = "Update " + Employee.locationsBillsMonthly + " SET bill_value =?,taxes=?,tafqeet=? " +
                ",bill_Text=? WHERE location_name =? AND id = ? AND Department in(?,?)";
        String getCurrentLatestBillInfoSQL = "SELECT * FROM " + Employee.locationsBills + " WHERE location_name=? and Department in(?,?) LIMIT 1;";
        try {
            BillTextManagement billTextManagement;
            String dof;
            String dof_builderLike;
            PreparedStatement latestInfoPreparedStatment = MyMainConnection.getInstance().prepareStatement(getCurrentLatestBillInfoSQL);
            PreparedStatement pS = MyMainConnection.getInstance().prepareStatement(updateBillsTableSQL);
            for (int i = 0; i < tbl_Monthlybills.getSelectedRows().length; i++) {
                try {
                    latestInfoPreparedStatment.setString(1, String.valueOf(tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 2)));
                    latestInfoPreparedStatment.setString(2, _dept);
                    latestInfoPreparedStatment.setString(3, sec_dept);
                    ResultSet currentLatestInfoRS = latestInfoPreparedStatment.executeQuery();
                    while (currentLatestInfoRS.next()) {
                        pS.setDouble(1, currentLatestInfoRS.getDouble("bill_value"));
                        pS.setDouble(2, currentLatestInfoRS.getDouble("bill_value") * taxes_percent * .01);
                        double sum = currentLatestInfoRS.getDouble("bill_value");
                        sum = sum + sum * taxes_percent * .01;
                        pS.setString(3, new tafqeetFactory(String.valueOf(sum)).ArabicNumber);
                        dof_builderLike = drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/";
                        dof = dateOfPrintingBuilderForUpdateButton(
                                String.valueOf(tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 2)),
                                _dept,
                                sec_dept,
                                dof_builderLike
                        );
                        billTextManagement = new BillTextManagement(dof);
                        String billText = getBillTextOptimized(
                                billTextManagement.getCurrentYear(),
                                billTextManagement.getCurrentlyMonth(),
                                billTextManagement.isCurrentModifiedYear(),
                                billTextManagement.getPreviousToCurrentMonth(),
                                currentLatestInfoRS
                        );
                        billText = setBillAccountNumberInBillText(companyName, billText);
                        pS.setString(4, billText);

                        pS.setString(5, String.valueOf(tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 2)));
                        pS.setInt(6, Integer.parseInt(String.valueOf(tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 1))));
                        pS.setString(7, _dept);
                        pS.setString(8, sec_dept);
                        int rowsAffected = pS.executeUpdate();
                        System.out.println("Number of rows affected is : " + rowsAffected);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void printBills_progressBar(JasperPrint masterJP) {
        try {
            dateOfPrinting = decicdeOnThatBillDateOfPrinting(tbl_Monthlybills, 0);
            masterJP = new DatabaseDatasourceReport((String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[0], 2),
                    (String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[0], 6), false, dateOfPrinting,
                    (String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[0], 1)).getJasperPrint();
        } catch (SQLException | FileNotFoundException e1) {
            e1.printStackTrace();
        }
        JasperPrint jasperPrint;
        addBillsToReport(masterJP);
        try {
            JasperPrintManager.printReport(masterJP, true);
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }

    private void addBillsToReport(JasperPrint masterJP) {
        JasperPrint jasperPrint;
        for (int i = 1; i < tbl_Monthlybills.getSelectedRows().length; i++) {
            try {
                dateOfPrinting = decicdeOnThatBillDateOfPrinting(tbl_Monthlybills, i);
                jasperPrint = new DatabaseDatasourceReport((String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 2),
                        (String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 6), false, dateOfPrinting,
                        (String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[i], 1)).getJasperPrint();

                List page = jasperPrint.getPages();
                JRPrintPage object = (JRPrintPage) page.get(0);
                masterJP.addPage(object);
            } catch (SQLException | FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void reviewABill_progressBar(JasperPrint masterJP) {
        try {
            dateOfPrinting = decicdeOnThatBillDateOfPrinting(tbl_Monthlybills, 0);
            masterJP = new DatabaseDatasourceReport((String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[0], 2),
                    (String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[0], 6), false, dateOfPrinting,
                    (String) tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRows()[0], 1)).getJasperPrint();
        } catch (SQLException | FileNotFoundException e1) {
            e1.printStackTrace();
        }
        addBillsToReport(masterJP);
        JasperViewer.viewReport(masterJP, false);
    }

    private void editBillID_MENU_OPTION_updateTheTables_progressBar() {
        if (tbl_Monthlybills.getSelectedRows().length == 1) {
            try {
                String billID = (String) JOptionPane.showInputDialog(
                        panel1,
                        "أدخل رقم الفاتورة الجديدة",
                        "تعديل رقم الفاتورة",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "9999");

                //If a string was returned, say so.
                if ((billID != null) && (billID.length() > 0)) {
                    Integer.parseInt(billID);
                    String sqlBillID = "Update " + Employee.locationsBillsMonthly + " SET id = ? where id =?";
                    try {
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sqlBillID);
                        preparedStatement.setInt(1, Integer.parseInt(billID));
                        preparedStatement.setInt(2,
                                Integer.parseInt(
                                        convertArabicNumbertoEnglishNumber(
                                                tbl_Monthlybills.getValueAt(tbl_Monthlybills.getSelectedRow(), 1).toString()
                                        )));
                        int rowsAffected = preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(panel1, "تم التعديل بنجاح لعدد " + rowsAffected + " فاتورة ", "تعديل رقم الفاتورة", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        populate_updateSum_optimizeLook_JTable_progressBar(true);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (NumberFormatException exc) {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(
                        panel1,
                        "من فضلك أدخل رقم صحيح",
                        "خطأ",
                        JOptionPane.ERROR_MESSAGE,
                        null
                );
            }
        } else {
            JOptionPane.showMessageDialog(panel1, "الرجاء تحديد فاتورة واحدة فقط في حالة الرغبة في تعديل رقم الفاتورة", "لا يمكن تعديل رقم أكثر من فاتورة واحدة", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateBillANDOptimizeTheLookOfTable_progressBar() throws SQLException {
        sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                new MyProgressBar();
                MyProgressBar.jProgressBar.setIndeterminate(true);
                populateBillsTable(tbl_Monthlybills, bill_table_filter);
                optimizeTheLookOfmyTable(tbl_Monthlybills);
                lblSumsSetters();
                return null;
            }

            @Override
            public void done() {
                MyProgressBar.disposeTheBar();
            }
        };
        sw.execute();

    }

    private void populate_updateSum_optimizeLook_JTable_progressBar(boolean alreadyInSwingWorker) throws SQLException {
        if (!alreadyInSwingWorker) {
            sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    new MyProgressBar();
                    MyProgressBar.jProgressBar.setIndeterminate(true);
                    populateBillsTable(tbl_Monthlybills, bill_table_filter);
                    lblSumsSetters();
                    populateMainTable(tbl_main);
                    optimizeTheLookOfmyTable(tbl_Monthlybills);
                    optimizeTheLookOfmyMain(tbl_main);
                    return null;
                }

                @Override
                public void done() {
                    MyProgressBar.disposeTheBar();
                }
            };
            sw.execute();
        } else {
            populateBillsTable(tbl_Monthlybills, bill_table_filter);
            lblSumsSetters();
            populateMainTable(tbl_main);
            optimizeTheLookOfmyTable(tbl_Monthlybills);
            optimizeTheLookOfmyMain(tbl_main);
        }
    }

    public String decicdeOnThatBillDateOfPrinting(JTable myMonthlyBillsTable, int i) throws SQLException {
        String requested_date;

        String date = "";
        requested_date = "%";
        requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
        requestingTheDate = requested_date;
        String bill_table_filter = "SELECT id,DateOfPrinting,location_name,bill_value,taxes,bill_Text,Department FROM " + Employee.locationsBillsMonthly + " WHERE location_name = ? AND DateOfPrinting LIKE ? AND Department in(?,?) AND id=? ;";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(bill_table_filter);
        preparedStatement.setString(1, String.valueOf(myMonthlyBillsTable.getValueAt(myMonthlyBillsTable.getSelectedRows()[i], 2)));
        preparedStatement.setString(2, requestingTheDate);
        preparedStatement.setString(3, PrintBills.sec_dept);
        preparedStatement.setString(4, PrintBills._dept);
        String billID = new BigDecimal(String.valueOf(myMonthlyBillsTable.getValueAt(myMonthlyBillsTable.getSelectedRows()[i], 1))).toString(); //converts bill number from arabic to english numbers
        preparedStatement.setString(5, billID);
        ResultSet dateOfPrintingRS = preparedStatement.executeQuery();
        while (dateOfPrintingRS.next()) {
            date = dateOfPrintingRS.getString("DateOfPrinting");
        }
        return date;
    }

    private void lblSumsSetters() {
        double sum = 0;
        for (int i = 0; i < myTableMode.generalTotalOfNumbers.size(); i++) {
            sum += Double.parseDouble(String.valueOf(myTableMode.generalTotalOfNumbers.get(i)));
        }
        lbl_generalTotal.setText(myTableMode.arabitizeMyNumbers(String.valueOf((double) Math.round(sum * 100) / 100)));
        sum = 0;
        for (int i = 0; i < myTableMode.taxesTotalOfNumbers.size(); i++) {
            sum += Double.parseDouble(String.valueOf(myTableMode.taxesTotalOfNumbers.get(i)));
        }
        lbl_salesTaxes.setText(myTableMode.arabitizeMyNumbers(String.valueOf((double) Math.round(sum * 100) / 100)));
        sum = 0;
        for (int i = 0; i < myTableMode.TotalOfNumbersWithOutTaxes.size(); i++) {
            sum += Double.parseDouble(String.valueOf(myTableMode.TotalOfNumbersWithOutTaxes.get(i)));
        }
        lbl_billValue.setText(myTableMode.arabitizeMyNumbers(String.valueOf((double) Math.round(sum * 100) / 100)));
        sum = 0;
    }

    private void updateWorksOfMonthData(boolean isRetreiving) throws SQLException {

        if (dateOfPrinting.length() >= 8 & !Objects.equals(dateOfPrinting, "") || isRetreiving) {
            if (!isRetreiving) {
                String[] seperatedLetters = dateOfPrinting.split("/");
                String year = "2013";
                String monthNumber;
                String monthName;
                String sqlMonth;
                String sum = "";
                DecimalFormat df;
                boolean modifiedYear = false;
                if (seperatedLetters[0].length() == 4) {
                    year = seperatedLetters[0];
                }
                if (seperatedLetters[2].length() == 4) {
                    year = seperatedLetters[2];
                }
                monthNumber = seperatedLetters[1];
                String previousMonthName;
                try {
                    previousMonthName = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNumber) - 2];
                } catch (ArrayIndexOutOfBoundsException e1) {
                    previousMonthName = new DateFormatSymbols().getMonths()[11];
                    year = String.valueOf(Integer.parseInt(year) - 1);
                    modifiedYear = true;
                }
                monthName = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNumber) - 1];
                currentYear = year;
                currentlyMonth = monthName;
                previousToCurrentMonth = previousMonthName;
                currentModifiedYear = modifiedYear;

                if (tbl_main.getSelectedRows().length == 1) {
                    insertOneNewBill_inTheCurrentMonth(year, monthName, modifiedYear, previousMonthName);
                } else if (tbl_main.getSelectedRows().length > 1) {
                    insertMultiple_Bills_inTheCurrentMonth(year, monthName, modifiedYear, previousMonthName);
                }
            }
            tbl_main.removeAll();
            tbl_Monthlybills.removeAll();
            String requested_date;

            requested_date = "%";
            requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
            requestingTheDate = requested_date;
            populate_updateSum_optimizeLook_JTable_progressBar(true);
        }
    }

    private void showProgressBarOnUpdatingTables(boolean isRetreiving) {
        sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                new MyProgressBar();
                MyProgressBar.jProgressBar.setIndeterminate(true);
                updateWorksOfMonthData(isRetreiving);
                return null;
            }

            @Override
            public void done() {
                MyProgressBar.disposeTheBar();
            }
        };
        sw.execute();
    }

    private void insertMultiple_Bills_inTheCurrentMonth(String year, String monthName, boolean modifiedYear, String previousMonthName) throws SQLException {
        String sqlMonth;
        String sum;
//        printBillsConn = dbManager.checkConnection(printBillsConn);
        for (int i = 0; i < tbl_main.getSelectedRows().length; i++) {

            sql = "SELECT id FROM " + Employee.locationsBillsMonthly + " WHERE Department in (?,?) ORDER BY id ASC;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, _dept);
            preparedStatement.setString(2, sec_dept);
            ResultSet r_s = preparedStatement.executeQuery();
            while (r_s.next()) {
                r_s.last();
                lastIDvalue = r_s.getInt("id");
            }

            if (lastIDvalue == 0) {
                lastIDvalue = 999;
            }
            lastIDvalue++;
            sqlMonth = "SELECT * from " + Employee.locationsBills + " WHERE location_name = '" + tbl_main.getValueAt(tbl_main.getSelectedRows()[i], 1)
                    + "' AND Department in (?,?) LIMIT 1;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sqlMonth);
            preparedStatement.setString(1, _dept);
            preparedStatement.setString(2, sec_dept);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
//            city_name	DateOfPrinting	location_name	Department	bill_value	taxes	bill_Text	activeBill	id	tafqeet

                sql = "INSERT INTO " + Employee.locationsBillsMonthly + " VALUES (?,?,?,?,?,?,?,?,?,?) ;";
                preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                preparedStatement.setString(1, rs.getString("city_name"));
                preparedStatement.setString(2, dateOfPrinting);
                preparedStatement.setString(3, rs.getString("location_name"));
                preparedStatement.setString(4, rs.getString("Department"));
                preparedStatement.setDouble(5, rs.getDouble("bill_value"));
                preparedStatement.setDouble(6, (rs.getDouble("bill_value")) * taxes_percent * .01);

                String billText = getBillTextOptimized(year, monthName, modifiedYear, previousMonthName, rs);
                preparedStatement.setString(7, billText);
                preparedStatement.setBoolean(8, rs.getBoolean("activeBill"));
                preparedStatement.setInt(9, lastIDvalue);
                sum = String.valueOf((Double.parseDouble(rs.getString("bill_value"))));
                sum = String.valueOf(Double.parseDouble(sum) + (Double.parseDouble(sum) * taxes_percent * .01));
                preparedStatement.setString(10, new tafqeetFactory(sum).ArabicNumber);
                preparedStatement.execute();
//                JOptionPane.showMessageDialog(panel1, "تم إضافة الفواتير بنجاح!");

            }
        }
        tbl_main.removeAll();
        tbl_Monthlybills.removeAll();

    }

    private void insertOneNewBill_inTheCurrentMonth(String year, String monthName, boolean modifiedYear, String previousMonthName) throws SQLException {
        String sqlMonth;
        String sum;
        sqlMonth = "SELECT * from " + Employee.locationsBills + " WHERE location_name = ?"
                + " AND Department in (?,?) LIMIT 1 ;";
//        printBillsConn = dbManager.checkConnection(printBillsConn);
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sqlMonth);
        preparedStatement.setString(1, (String) tbl_main.getValueAt(tbl_main.getSelectedRow(), 1));
        preparedStatement.setString(2, _dept);
        preparedStatement.setString(3, sec_dept);
        ResultSet rs = preparedStatement.executeQuery();
        sql = "SELECT id FROM " + Employee.locationsBillsMonthly + " WHERE Department in (?,?) ORDER BY id ASC;";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1, _dept);
        preparedStatement.setString(2, sec_dept);
        ResultSet r_s = preparedStatement.executeQuery();
        while (r_s.next()) {
            r_s.last();
            lastIDvalue = r_s.getInt("id");
        }

        if (lastIDvalue == 0) {
            lastIDvalue = 999;
        }
        lastIDvalue++;
        while (rs.next()) {
//            city_name	DateOfPrinting	location_name	Department	bill_value	taxes	bill_Text	activeBill	id	tafqeet

            sql = "INSERT INTO " + Employee.locationsBillsMonthly + " VALUES (?,?,?,?,?,?,?,?,?,?) ;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, rs.getString("city_name"));
            preparedStatement.setString(2, dateOfPrinting);
            preparedStatement.setString(3, rs.getString("location_name"));
            preparedStatement.setString(4, rs.getString("Department"));
            preparedStatement.setDouble(5, rs.getDouble("bill_value"));
            preparedStatement.setDouble(6, (rs.getDouble("bill_value")) * taxes_percent * .01);
            String billText = setBillAccountNumberInBillText(rs, companyName);
            if (billText.contains("%Months2") && modifiedYear) {
                billText = billText
                        .replace("%Year1", String.valueOf(Integer.parseInt(year) + 1));
            } else if (!billText.contains("%Months2")) {
                billText = billText
                        .replace("%Year1", String.valueOf(Integer.parseInt(year)));
            }
            billText = billText.replace("%Months2", monthName).replace("%Months1", previousMonthName)
                    .replace("%Year1", year);
            preparedStatement.setString(7, billText);
            preparedStatement.setBoolean(8, rs.getBoolean("activeBill"));

            preparedStatement.setInt(9, lastIDvalue);


            sum = String.valueOf((Double.parseDouble(rs.getString("bill_value"))));
            sum = String.valueOf(Double.parseDouble(sum) + (Double.parseDouble(sum) * taxes_percent * .01));
            preparedStatement.setString(10, new tafqeetFactory(sum).ArabicNumber);
            preparedStatement.execute();
//            JOptionPane.showMessageDialog(panel1, "تم إضافة الفاتورة بنجاح!");
            tbl_main.removeAll();
            tbl_Monthlybills.removeAll();
            String requested_date;

            requested_date = "%";
            requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
            requestingTheDate = requested_date;


            populate_updateSum_optimizeLook_JTable_progressBar(true);
        }

    }

    private String getBillTextOptimized(String year, String monthName, boolean modifiedYear, String previousMonthName, ResultSet rs) throws SQLException {
        sql = "SELECT * FROM " + Employee.company_bank_accountsTbl + ";";
        ResultSet resultSet = MyMainConnection.getInstance().prepareStatement(sql).executeQuery();
        String billText = "";
        while (resultSet.next()) {
            billText = rs.getString("bill_Text").replace(resultSet.getString("bank_bill_sign"),
                    resultSet.getString("BankAccount_number") + " ب" + resultSet.getString("bank_name"));
        }
        if (billText.contains("%Months2") && modifiedYear) {
            billText = billText
                    .replace("%Year1", String.valueOf(Integer.parseInt(year) + 1));
        } else if (!billText.contains("%Months2")) {
            billText = billText
                    .replace("%Year1", String.valueOf(Integer.parseInt(year)));
        }
        billText = billText.replace("%Months2", monthName).replace("%Months1", previousMonthName)
                .replace("%Year1", year);
        return billText;
    }

    public static String setBillAccountNumberInBillText(ResultSet rs, String companyName) throws SQLException {
        String sqlQuery;
        sqlQuery = "SELECT * FROM " + Employee.company_bank_accountsTbl + " WHERE company_name=?;";
        PreparedStatement prbsttmnt = MyMainConnection.getInstance().prepareStatement(sqlQuery);
        prbsttmnt.setString(1, companyName);
        ResultSet resultSet = prbsttmnt.executeQuery();

        String billText = rs.getString("bill_Text");
        while (resultSet.next()) {
            if (billText.contains(resultSet.getString("bank_bill_sign"))) {
                billText = billText.replace(resultSet.getString("bank_bill_sign"),
                        resultSet.getString("BankAccount_number") + " ب" + resultSet.getString("bank_name"));
            }
        }
        return billText;
    }

    private static String setBillAccountNumberInBillText(String companyName, String billText) throws SQLException {
        String sqlQuery;
        sqlQuery = "SELECT * FROM " + Employee.company_bank_accountsTbl + " WHERE company_name=?;";
        PreparedStatement prbsttmnt = MyMainConnection.getInstance().prepareStatement(sqlQuery);
        prbsttmnt.setString(1, companyName);
        ResultSet resultSet = prbsttmnt.executeQuery();


        while (resultSet.next()) {
            if (billText.contains(resultSet.getString("bank_bill_sign"))) {
                billText = billText.replace(resultSet.getString("bank_bill_sign"),
                        resultSet.getString("BankAccount_number") + " ب" + resultSet.getString("bank_name"));
            }
        }
        return billText;
    }

    private void populateBillsTable(JTable myTable, String sql) throws SQLException {
        monthOfPrinting = String.valueOf(drop_monthPrint.getSelectedItem());
        YearOfPrinting = String.valueOf(drop_yearPrint.getSelectedItem());
//        conn = dbManager.checkConnection(conn);

        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1, requestingTheDate);
        preparedStatement.setString(2, _dept);
        preparedStatement.setString(3, sec_dept);

        ResultSet rs = preparedStatement.executeQuery();

        myTable.setModel(myTableMode.buildTableModelForBills(rs, Employee.locationsBills));

        myTable.setShowGrid(true);

    }

    private void populateMainTable(JTable myTable) throws SQLException {
        monthOfPrinting = String.valueOf(drop_monthPrint.getSelectedItem());
        YearOfPrinting = String.valueOf(drop_yearPrint.getSelectedItem());
//        conn = dbManager.checkConnection(conn);

        sql = "select location_name,DateOfPrinting,Department ,max(id) from " + Employee.locationsBillsMonthly + " WHERE Department in(?,?) AND activeBIll = 1" +
                ";";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1, _dept);
        preparedStatement.setString(2, sec_dept);
        ResultSet rs = preparedStatement.executeQuery();

        myTable.setModel(myTableMode.buildTableModelForBills(rs, Employee.locationsBillsMonthly));

        myTable.setShowGrid(true);

    }

    public String dateOfPrintingBuilderForUpdateButton(String locationName, String dep1, String dep2, String dateOfPrintingLike) throws SQLException {
        String dof = "";
        String sql = "Select * From " + Employee.locationsBillsMonthly + " where location_name =? and Department in(?,?) and DateOfPrinting LIKE ? LIMIT 1;";
        PreparedStatement prb;
        prb = MyMainConnection.getInstance().prepareStatement(sql);
        prb.setString(1, locationName);
        prb.setString(2, dep1);
        prb.setString(3, dep2);
        prb.setString(4, "%" + dateOfPrintingLike + "%");

        ResultSet rs = prb.executeQuery();

        while (rs.next()) {
            dof = rs.getString("DateOfPrinting");
        }
        return dof;
    }

    private void optimizeTheLookOfmyTable(JTable currentTable) {
        JTableHeader th = currentTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc;
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i = 0; i < currentTable.getColumnCount(); i++) {
            currentTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }


        tc = tcm.getColumn(0);
        tc.setHeaderValue("م");
        tc.setMaxWidth(30);
        tc.setMinWidth(30);
        tc = tcm.getColumn(1);
        tc.setHeaderValue("رقم الفاتورة");
        tc.setMaxWidth(70);
        tc.setMinWidth(70);
        tc = tcm.getColumn(2);
        tc.setHeaderValue("أسم الموقع");
        tc = tcm.getColumn(3);
        tc.setHeaderValue("قيمة الفاتورة");
        tc = tcm.getColumn(4);
        tc.setHeaderValue("الضريبة");
        tc = tcm.getColumn(5);
        tc.setHeaderValue("الإجمالى");
        tc = tcm.getColumn(6);
        tc.setHeaderValue("نوع الخدمة");
        th.repaint();
        TableColumnModel retrMod = currentTable.getColumnModel();
        TableColumn retrCol = retrMod.getColumn(2);
        retrCol.setHeaderRenderer(new TableCellRendererCenter());
    }

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
        tc.setHeaderValue("م");
        tc.setMaxWidth(30);
        tc.setMinWidth(30);
        tc = tcm.getColumn(1);
        tc.setHeaderValue("أسم الموقع");
        tc = tcm.getColumn(2);
        tc.setHeaderValue("نوع الخدمة");
        tc = tcm.getColumn(3);
        tc.setHeaderValue("تاريخ آخر طباعة");


        th.repaint();
        TableColumnModel retrMod = currentTable.getColumnModel();
        TableColumn retrCol = retrMod.getColumn(2);

        retrCol.setHeaderRenderer(new TableCellRendererCenter());
    }


    public static void main(String[] args) throws SQLException {
        PrintBills printBills = new PrintBills();
        Employee.setLookAndFeelForMyProgram();
        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        JFrame frame = new JFrame();
        frame.add(printBills.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setTitle("طباعة الفواتير");
        frame.setVisible(true);
        frame.setSize(new Dimension(1220, 620));

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dim.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dim.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
//        frame.setLocation((dim.width / 2 - frame.getSize().width / 2) - 1220 / 2, (dim.height / 2 - frame.getSize().height / 2) - 620 / 2);


//        frame.setBounds(screenWidth / 3, screenHeight / 5, 420, 500);
//        frame.setSize(1220, 620);
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
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:54dlu:noGrow,fill:120px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:57dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:8dlu:noGrow,fill:83px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:317px:grow,fill:max(d;4px):noGrow,left:5dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:70px:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:91px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        jscrol__main = new JScrollPane();
        CellConstraints cc = new CellConstraints();
        panel1.add(jscrol__main, cc.xywh(17, 5, 8, 11, CellConstraints.CENTER, CellConstraints.FILL));
        tbl_main = new JTable();
        jscrol__main.setViewportView(tbl_main);
        btn_preview = new JButton();
        btn_preview.setText("معاينة");
        panel1.add(btn_preview, cc.xy(13, 21));
        JScrollPane1 = new JScrollPane();
        panel1.add(JScrollPane1, cc.xywh(4, 5, 10, 11, CellConstraints.FILL, CellConstraints.FILL));
        tbl_Monthlybills = new JTable();
        JScrollPane1.setViewportView(tbl_Monthlybills);
        btn_print = new JButton();
        btn_print.setText("طباعة الفواتير");
        panel1.add(btn_print, cc.xy(11, 21));
        btn_addOne = new JButton();
        btn_addOne.setText("<");
        panel1.add(btn_addOne, cc.xy(15, 11));
        btn_retrieveOne = new JButton();
        btn_retrieveOne.setText(">");
        panel1.add(btn_retrieveOne, cc.xy(15, 13));
        drop_monthPrint = new JComboBox();
        panel1.add(drop_monthPrint, cc.xy(11, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 18));
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(4);
        label1.setText("شهر");
        panel1.add(label1, cc.xy(13, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        drop_yearPrint = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        drop_yearPrint.setModel(defaultComboBoxModel1);
        panel1.add(drop_yearPrint, cc.xy(5, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, 18));
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("سنة");
        panel1.add(label2, cc.xy(8, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font(label3.getFont().getName(), Font.BOLD, 14));
        label3.setHorizontalAlignment(4);
        label3.setText("إجمالى صافي");
        panel1.add(label3, cc.xy(11, 17, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        lbl_billValue = new JLabel();
        lbl_billValue.setFont(new Font(lbl_billValue.getFont().getName(), Font.BOLD, 16));
        lbl_billValue.setHorizontalAlignment(4);
        lbl_billValue.setText("Label");
        panel1.add(lbl_billValue, cc.xy(8, 17, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setFont(new Font(label4.getFont().getName(), Font.BOLD, 14));
        label4.setHorizontalAlignment(4);
        label4.setText("قيمة مضافة");
        panel1.add(label4, cc.xy(5, 17, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        lbl_salesTaxes = new JLabel();
        lbl_salesTaxes.setFont(new Font(lbl_salesTaxes.getFont().getName(), Font.BOLD, 16));
        lbl_salesTaxes.setHorizontalAlignment(4);
        lbl_salesTaxes.setText("Label");
        panel1.add(lbl_salesTaxes, cc.xyw(2, 17, 3, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        lbl_generalTotal = new JLabel();
        lbl_generalTotal.setFont(new Font(lbl_generalTotal.getFont().getName(), Font.BOLD, 16));
        lbl_generalTotal.setHorizontalAlignment(4);
        lbl_generalTotal.setText("Label");
        panel1.add(lbl_generalTotal, cc.xy(8, 19, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font(label5.getFont().getName(), Font.BOLD, 14));
        label5.setHorizontalAlignment(4);
        label5.setText("إجمالي بالقيمة المضافة");
        panel1.add(label5, cc.xy(11, 19, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        طباعةكشفتسليمButton = new JButton();
        طباعةكشفتسليمButton.setHorizontalTextPosition(0);
        طباعةكشفتسليمButton.setText("طباعة كشف تسليم");
        panel1.add(طباعةكشفتسليمButton, cc.xy(8, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
        طباعةكشفمبيعاتButton = new JButton();
        طباعةكشفمبيعاتButton.setHorizontalTextPosition(0);
        طباعةكشفمبيعاتButton.setText("طباعة كشف مبيعات");
        panel1.add(طباعةكشفمبيعاتButton, cc.xy(5, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
        btn_saveToFile = new JButton();
        btn_saveToFile.setText("حفظ التحديد إلى ملف");
        panel1.add(btn_saveToFile, cc.xy(4, 21, CellConstraints.FILL, CellConstraints.CENTER));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
