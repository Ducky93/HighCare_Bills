package DataBaseManageAndReport;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by mohab 2 on 11/15/2016.
 */
public class Employee {
    private String EMP_NAME;
    private int EMP_AGE;
    private String EMP_NATIONALID;
    private String EMP_PHONE;
    private String EMP_JOINDATE;
    private float EMP_SALARY;
    private float EMP_TOTALSALARY;
    private String EMP_EXTRANOTES;
    private String EMP_CITY;
    private String EMP_LOCATION;
    private String EMP_DEPARTMENT;
    private String sql;
//    public static String EmpDBName="employee";
    public static String EmpDBName="r2gmduck_employee";
    public static String generalInfoTable = "generalempinfo";
    public static String empMonthlyCheck = "empmonthlycheck";
    public static String locationsTable = "locationspercities";
    public static String locationsBills = "locations_bills";
    public static String companyDetails = "company_details";
    public static String locationsBillsMonthly = "monthly_bills_highcare";
    public static String company_bank_accountsTbl = "company_bank_accountstbl";
    public static String usersTbl = "userstbl";
    public static String bill_data = "";
    private String empSalary = "empSalary";
    public static String Cities = "cities";
    private DatabaseManager dbEMPMangager = new DatabaseManager();
//    Connection connection;
//    private Connection EmpConn = dbEMPMangager.checkConnection(connection);
    private PreparedStatement preparedStatement;

    public Employee() throws SQLException {
//        dbEMPMangager = new DataBaseManageAndReport.DatabaseManager(EmpDBName);
//        dbEMPMangager.tablesCreator(generalInfoTable,"(id INT AUTO_INCREMENT PRIMARY KEY ,Name varchar(50) UNIQUE,Age INT , " +
//                "National_ID TEXT,PhoneNumber varchar(50),JoinDate Date,Notes LONGTEXT)");

        createMyTables();
    }

    public Employee(String emp_name, int emp_age, String emp_nationalid, String emp_phone,
                    String emp_joindate, String emp_extranotes
            , String emp_city, String emp_location, String emp_department
    ) throws SQLException {
        EMP_NAME = emp_name;
        EMP_AGE = emp_age;
        EMP_NATIONALID = emp_nationalid;
        EMP_PHONE = emp_phone;
        EMP_JOINDATE = emp_joindate;
        EMP_JOINDATE = emp_joindate;
        EMP_EXTRANOTES = emp_extranotes;
        EMP_CITY = emp_city;
        EMP_LOCATION = emp_location;
        EMP_DEPARTMENT=emp_department;
        try {
            createMyTables();
            storeEmpInfo();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
    private void createMyTables() throws SQLException {
//        dbEMPMangager = new DatabaseManager(EmpDBName);
        dbEMPMangager.tablesCreator(Cities,"(city_nwame nvarchar(50) Primary Key)");
        dbEMPMangager.tablesCreator(locationsTable,"(city_name nvarchar(50) ,location_name varchar(255) ,NumberOfLabor INT,TotalContractValue DOUBLE,StartDate Date,EndDate Date,YearlyIncrement varchar(50)," +
                "Department varchar(50) , CONSTRAINT locationExist_uniqueIndex UNIQUE (city_name,location_name,Department),FOREIGN KEY (city_name) REFERENCES "+Cities+" (city_name))");
        dbEMPMangager.tablesCreator(generalInfoTable,"(Name varchar(50) UNIQUE NOT NULL PRIMARY KEY,Age INT , " +
                "National_ID Text,PhoneNumber varchar(50),JoinDate Date,Notes LONGTEXT,CurrentWorkingCity nvarchar(50),CurrentContract varchar(255),Department varchar(50)" +
                ",FOREIGN KEY (CurrentWorkingCity,CurrentContract,Department) REFERENCES "+locationsTable+"(city_name,location_name,Department))"
               );
        dbEMPMangager.tablesCreator(empMonthlyCheck,"(Name varchar(50) , " +
                "Unallowed_Absence INT Default 0," +
                "unallowed_Absence_notes Text,"+
                "Allowed_Absence INT Default 0," +
                "allowed_Absence_notes Text,"+
                "Vacation INT Default 0," +
                "vacation_notes Text,"+
                "Ill_absence INT Default 0," +
                "ill_notes Text,"+
                "Punishment INT Default 0 ,"+
                "punishment_notes Text,"+
                "Month INT Default 0,"+
                "Year INT Default 0, FOREIGN KEY (Name) REFERENCES "+generalInfoTable+"(Name), CONSTRAINT empCheck_uniqueIndex UNIQUE (Name,Month,Year));");
        dbEMPMangager.tablesCreator(locationsBills,"(city_name nvarchar(50) ,location_name varchar(255),Department varchar(50), bill_value DOUBLE ," +
                "taxes DOUBLE,bill_Text TEXT,activeBill boolean, tafqeet TEXT" +
                ",FOREIGN KEY (city_name,location_name,Department) REFERENCES "+locationsTable+"(city_name,location_name,Department))");
        dbEMPMangager.tablesCreator(companyDetails,"(company_name nvarchar(50) ,taxes_card Text,commercial_log Text,taxes_file Text,sales_tax Text,current_sales_taxes_percent smallint)");
        dbEMPMangager.tablesCreator(locationsBillsMonthly,"(city_name nvarchar(50) ,DateOfPrinting Text,location_name varchar(255),Department varchar(50), bill_value DOUBLE ," +
                "taxes DOUBLE,bill_Text TEXT,activeBill boolean,id INT , tafqeet TEXT" +
                ",CONSTRAINT no_duplicates_constraint_mine UNIQUE (DateOfPrinting(50) ,location_name, Department))");
        dbEMPMangager.tablesCreator(company_bank_accountsTbl,"(company_name varchar(250) ,Bank_name varchar(250),BankAccount_number varchar(250),bank_bill_sign varchar(50)" +
                "\n,CONSTRAINT no_duplicates UNIQUE (Bank_name(250) ,BankAccount_number(250)))");
        dbEMPMangager.tablesCreator(usersTbl,"(user Text ,password Text,speicality Text)");
    }

    private void storeEmpInfo() throws SQLException, ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        sql = "INSERT INTO "+generalInfoTable+"(NAME,Age,National_ID,PhoneNumber,JoinDate,Notes,CurrentWorkingCity,CurrentContract,Department) VALUES (?,?,?,?,?,?,?,?,?)";
//        EmpConn = dbEMPMangager.checkConnection(EmpConn);
        preparedStatement =MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1,this.EMP_NAME);
        preparedStatement.setInt(2,this.EMP_AGE);
        preparedStatement.setString(3,this.EMP_NATIONALID);
        preparedStatement.setString(4,this.EMP_PHONE);

        preparedStatement.setDate(5, new java.sql.Date(dateFormat.parse(this.EMP_JOINDATE).getTime()));
        preparedStatement.setString(6,this.EMP_EXTRANOTES);
        preparedStatement.setString(7,this.EMP_CITY);
        preparedStatement.setString(8,this.EMP_LOCATION);
        preparedStatement.setString(9,this.EMP_DEPARTMENT);
        preparedStatement.execute();
        preparedStatement.close();
    }
    ResultSet retrieveEmployee(String name) throws SQLException {
        sql = "SELECT * FROM "+generalInfoTable+" WHERE NAME = ?;";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1,name);
        return preparedStatement.executeQuery();
    }

    Object[] retrieveAllEmployees() throws SQLException {
        sql = "SELECT * FROM "+generalInfoTable+";";
        return new Object[]{MyMainConnection.getInstance(), MyMainConnection.getInstance().prepareStatement(sql).executeQuery()};
    }
    public static void setLookAndFeelForMyProgram(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // not worth my time
            }
        }
    }
}
