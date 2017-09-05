package Forms;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import DataBaseManageAndReport.MyMainConnection;
import Utils.MyProgressBar;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by mohab 2 on 11/23/2016.
 */
public class LoginPage {
    private JTextField txt_user;
    private JPanel panel1;
    private JTextField txt_password1;
    private JButton btn_login;
    private JPasswordField txt_password;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
    private String user;
    private String password;
    private String sql;
    public static String deptNameForUser;
    public static JFrame currentFrame;
    SwingWorker sw;

    public LoginPage() throws SQLException {
        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        for (Component c : panel1.getComponents()
                ) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        txt_password.setEchoChar('*');
        sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                new MyProgressBar();
                MyProgressBar.jProgressBar.setIndeterminate(true);
                new Employee();

                return null;
            }

            @Override
            public void done() {
                MyProgressBar.disposeTheBar();
            }
        };
        sw.execute();

        btn_login.addActionListener(e -> {
            user = txt_user.getText();
            password = txt_password.getText();
            SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    new MyProgressBar();
                    MyProgressBar.jProgressBar.setIndeterminate(true);
                    connectToDBTocheckUser_progressBar();

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

    private void connectToDBTocheckUser_progressBar() {
        sql = "Select * FROM " + Employee.usersTbl + " ;";
        boolean loginUser = false;
        try {
            ResultSet rs = MyMainConnection.getInstance().prepareStatement(sql).executeQuery();
            while (rs.next()) {
                if (Objects.equals(rs.getString("user"), user)) {
                    loginUser = true;
                    if (Objects.equals(rs.getString("password"), password)) {
                        doLogin();
                        closeThisFrame(currentFrame);
                    } else {
                        JOptionPane.showMessageDialog(panel1, "كلمة المرور غير صحيحة!");
                    }
                }
            }
            if (!loginUser) {
                JOptionPane.showMessageDialog(panel1, "أسم المستخدم غير صحيح!");
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    private void doLogin() throws SQLException {
        switch (user) {
            case "admindev":
                deptNameForUser = "النظافة";
                ManagementPage.main(null);
                break;
            case "adminsec":
                deptNameForUser = "الحراسة";
                ManagementPage.main(null);
                break;
            default:
                return;
        }

    }

    public static void main(String[] args) throws SQLException {
        LoginPage loginPage = new LoginPage();
        Employee.setLookAndFeelForMyProgram();

        JFrame frame = new JFrame();
        frame.add(loginPage.panel1);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
//        frame.setBounds(screenWidth / 2, screenHeight / 2, 420, 200);
        frame.setSize(new Dimension(420, 200));


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dim.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dim.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

        frame.setTitle("Login");
        frame.setResizable(false);
        setCurrentFrame(frame);
    }

    public void closeThisFrame(JFrame frame1) {
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.dispose();
    }


    public static void setCurrentFrame(JFrame jFrame) {
        currentFrame = jFrame;
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
        panel1.setLayout(new FormLayout("fill:22px:noGrow,left:6dlu:noGrow,fill:248px:grow,left:4dlu:noGrow,fill:28px:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:46px:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        btn_login = new JButton();
        btn_login.setText("دخول");
        CellConstraints cc = new CellConstraints();
        panel1.add(btn_login, cc.xy(3, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 14));
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(4);
        label1.setText("كلمة المرور");
        panel1.add(label1, cc.xy(5, 5));
        final JLabel label2 = new JLabel();
        label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, 14));
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setText("إسم المستخدم");
        panel1.add(label2, cc.xy(5, 3));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font(label3.getFont().getName(), Font.BOLD, 18));
        label3.setHorizontalAlignment(0);
        label3.setHorizontalTextPosition(0);
        label3.setText("صفحة الدخول");
        panel1.add(label3, cc.xyw(1, 1, 7));
        txt_user = new JTextField();
        txt_user.setColumns(10);
        txt_user.setHorizontalAlignment(4);
        panel1.add(txt_user, cc.xy(3, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        txt_password = new JPasswordField();
        txt_password.setColumns(10);
        panel1.add(txt_password, cc.xy(3, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}