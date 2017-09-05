package FutureUpdates;

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
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by mohab 2 on 11/25/2016.
 */
public class Management_ManageContracts {
    private JTable table1;
    private JPanel panel1;
    private JScrollPane JScrollPane1;
    private JComboBox drop_chooseCity;
    private JComboBox drop_sortContracts;
    private PreparedStatement preparedStatement;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();

    private String sql;

    private Management_ManageContracts() throws SQLException {
        table1.setAutoCreateRowSorter(true);
        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        for (Component c : JScrollPane1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        table1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);


        panel1.addHierarchyListener(e -> {
            JComponent component = (JComponent) e.getSource();

            if ((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0
                    && component.isShowing()) {
                java.util.List<String> myCitiesList = null;
                try {
                    myCitiesList = dbManager.retrieveCitiesFROM_DB();
                    drop_chooseCity.addItem("كل المحافظات");
                    myCitiesList.forEach(x -> drop_chooseCity.addItem(x));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        drop_chooseCity.addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (Objects.equals(drop_chooseCity.getSelectedItem().toString(), "كل المحافظات")) {
                    try {
                        populateContractsTable(true);
                        optimizeTheLookOfmyTable(table1);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        populateContractsTable(false);
                        optimizeTheLookOfmyTable(table1);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }


    public static void optimizeTheLookOfmyTable(JTable currentTable) {
        JTableHeader th = currentTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc;
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i = 0; i < currentTable.getColumnCount(); i++) {
            currentTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        tc = tcm.getColumn(0);
        tc.setHeaderValue("أسم المحافظة");
        tc = tcm.getColumn(1);
        tc.setHeaderValue("أسم الموقع");
        tc = tcm.getColumn(2);
        tc.setHeaderValue("عدد العاملين");
        tc = tcm.getColumn(3);
        tc.setHeaderValue("قيمة التعاقد");
        tc = tcm.getColumn(4);
        tc.setHeaderValue("بداية مدة التعاقد");
        tc = tcm.getColumn(5);
        tc.setHeaderValue("نهاية المدة");
        tc = tcm.getColumn(6);
        tc.setHeaderValue("الزيادة السنوية (%)");
        tc = tcm.getColumn(7);
        tc.setHeaderValue("القطاع");
        th.repaint();
        TableColumnModel retrMod = currentTable.getColumnModel();
        TableColumn retrCol = retrMod.getColumn(2);

        retrCol.setHeaderRenderer(new TableCellRendererRight());
    }

    private void populateContractsTable(boolean allOrOne) throws SQLException {
//        conn = dbManager.checkConnection(conn);
        if (allOrOne) {
            sql = "select * from " + Employee.locationsTable + ";";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        } else {
            sql = "select * from " + Employee.locationsTable + " WHERE city_name = ?;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, drop_chooseCity.getSelectedItem().toString());
        }

        ResultSet rs = preparedStatement.executeQuery();
        table1.setModel(myTableMode.buildTableModel(rs));
        table1.setShowGrid(true);
    }

    public static void main(String[] args) throws SQLException {
        Management_ManageContracts management_manageContracts = new Management_ManageContracts();
        Employee.setLookAndFeelForMyProgram();
        JFrame frame = new JFrame();
        frame.add(management_manageContracts.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(820, 620);
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
        panel1.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;90dlu):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:38px:noGrow,top:4dlu:noGrow,center:37px:noGrow,top:4dlu:noGrow,center:375px:grow,top:24dlu:noGrow,center:53px:noGrow"));
        JScrollPane1 = new JScrollPane();
        CellConstraints cc = new CellConstraints();
        panel1.add(JScrollPane1, cc.xyw(1, 5, 9, CellConstraints.FILL, CellConstraints.FILL));
        table1 = new JTable();
        JScrollPane1.setViewportView(table1);
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 18));
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("إدارة التعاقدات");
        panel1.add(label1, cc.xyw(1, 1, 9));
        drop_chooseCity = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        drop_chooseCity.setModel(defaultComboBoxModel1);
        panel1.add(drop_chooseCity, cc.xy(5, 3));
        final JLabel label2 = new JLabel();
        label2.setText("المحافظة");
        panel1.add(label2, cc.xy(7, 3));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
