package DataBaseManageAndReport;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import javax.swing.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mohab 2 on 11/15/2016.
 */
public class DatabaseManager {
    private String sql;
    PreparedStatement preparedStatement;
    ResultSet rs;
    public static final String COMPANY_NAME_DEV ="هاى كير للتنمية والإستثمار العقاري";
    public static final String COMPANY_NAME_Security ="هاى كير لخدمات الحراسة ونقل الأموال";
    public static final String SECURITY_DEPT="الحراسة";
    public static final String CLEANING_DEPT="النظافة";
    public static final String MONEYTRANSFERE_DEPT="نقل الأموال";
    private double taxes;
    public double getTaxes() throws SQLException {
        sql = "SELECT * FROM "+ Employee.companyDetails;
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        ResultSet rs;
        rs = preparedStatement.executeQuery();
        rs.next();
        this.taxes = rs.getInt("current_sales_taxes_percent");
        return taxes;
    }


//    private Connection getConnection(String dbName){
//        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            sql = "CREATE DATABASE IF NOT EXISTS "+dbName;
//
//
//            connection = DriverManager.getConnection("jdbc:mysql://localhost", "root", "0mo1230m123");
//            connection.prepareStatement(sql).execute();
//            try {
//                MyMainConnection.getInstance().prepareStatement(sql).execute();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,"root","0mo1230m123");
//
//        return connection;
//    }


//    public Connection getConnection(String dbName) {
//        try {
//            String path = new File(".").getCanonicalPath();
//            Class.forName("org.h2.Driver").newInstance();
//            connection = DriverManager.getConnection("jdbc:h2:"+path+"/Database/"+dbName,"","");
//            connection.setSchema("EMPLOYEE");
//
//            DatabaseMetaData md = connection.getMetaData();
//            ResultSet rs = md.getTables(connection.getCatalog(), connection.getSchema(), "%", null);
//            while (rs.next()) {
//                System.out.println(rs.getString(3));
//            }
//        } catch (InstantiationException |
//                IllegalAccessException |
//                SQLException |
//                ClassNotFoundException |
//                IOException e) {
//            e.printStackTrace();
//        }
//        return connection;
//    }
//    public Connection checkConnection(Connection conn) throws SQLException {
//        if(conn==null) {
//            return getConnection(Employee.EmpDBName);
//        }else if(conn.isClosed()){
//            return getConnection(Employee.EmpDBName);
//        }else{
//            return conn;
//        }
//    }

//    public DatabaseManager(String dbName) throws SQLException {
//        Connection conn=null;
//        checkConnection(conn);
//    }

    public DatabaseManager() {

    }

    public void tablesCreator(String tbl_name, String extraString){
        sql = "CREATE TABLE IF NOT EXISTS "+tbl_name +" "+extraString+";";

        try {
            MyMainConnection.getInstance().prepareStatement(sql).execute();
            if(Objects.equals(tbl_name, Employee.Cities)) {
                fill_cities_table();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void fill_cities_table() throws SQLException {
        sql = "SELECT COUNT(*) FROM "+ Employee.Cities+"";
        ResultSet test = MyMainConnection.getInstance().prepareStatement(sql).executeQuery();
        test.next();
        if (test.getInt("Count(*)")==0) {
            sql = "INSERT INTO "+ Employee.Cities+" VALUES" +
                    "\n('محافظة القاهرة')" +
                    "\n,('محافظة الاسكندرية')" +
                    "\n,('محافظة بورسعيد'),('محافظة السويس'),('محافظة دمياط'),('محافظة الدقهلية'),('محافظة الشرقية'),('محافظة القليوبية')," +
                    "\n('محافظة كفرالشيخ'),('محافظة الغربية'),('محافظة المنوفية'),('محافظة البحيرة'),('محافظة الإسماعلية')," +
                    "\n('محافظة الجيزة'),('محافظة بنى سويف'),('محافظة الفيوم'),('محافظة المنيا'),('محافظة أسيوط')," +
                    "\n('محافظة سوهاج'),('محافظة قنا'),('محافظة أسوان'),('محافظة الأقصر'),('محافظة البحرالأحمر')," +
                    "\n('محافظة الوادى الجديد')," +
                    "\n('محافظة مطروح'),('محافظة شمال سيناء'),('محافظة جنوب سيناء')";
            MyMainConnection.getInstance().prepareStatement(sql).execute();
        }
    }
    public List<String> retrieveCitiesFROM_DB() throws SQLException {
        List<String> CitiesList = new ArrayList<>();
        sql = "SELECT * FROM "+ Employee.Cities+";";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        rs= preparedStatement.executeQuery();
        while(rs.next()){
            CitiesList.add(rs.getString("city_name"));
        }
        return CitiesList;
    }
    public List<String> retrieveLocationsFROM_DB(String cityName) throws SQLException {
        List<String> LocationsList = new ArrayList<>();
        sql = "SELECT * FROM "+ Employee.locationsTable+" WHERE city_name = ?;";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1,cityName);
        rs= preparedStatement.executeQuery();
        while(rs.next()){
            LocationsList.add(rs.getString("location_name"));
        }
        return LocationsList;
    }
    public List<String> retrieve_EmployeesInLocationsFROM_DB(String location_name) throws SQLException {
        List<String> EmployeesList = new ArrayList<>();
        sql = "SELECT * FROM "+ Employee.generalInfoTable+" WHERE CurrentContract= ? ;";
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(1,location_name);
        rs= preparedStatement.executeQuery();
        while(rs.next()){
            EmployeesList.add(rs.getString("Name"));
        }
        return EmployeesList;
    }
    public void insertNewContractDataInDB(String cityName, String locationName, int NumLabor
            , double TotalValueOfContract, String startDate, String endDate, int YearlyIncrement, String Department, JPanel panel
            , String task) throws SQLException, ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(Objects.equals(task, "insert")) {
            sql = "Insert INTO " + Employee.locationsTable + " VALUES (?,?,?,?,?,?,?,?);";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, locationName);
            preparedStatement.setInt(3, NumLabor);
            preparedStatement.setDouble(4, TotalValueOfContract);
            preparedStatement.setDate(5, new java.sql.Date(dateFormat.parse(startDate).getTime()));
            preparedStatement.setDate(6, new java.sql.Date(dateFormat.parse(endDate).getTime()));
            preparedStatement.setInt(7, YearlyIncrement);
            preparedStatement.setString(8, Department);
            try {
                preparedStatement.execute();
                JOptionPane.showMessageDialog(panel, "تم إضافة التعاقد بنجاح !");
            } catch (MySQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(panel, "التعاقد الذي أدخلته موجود بالفعل !");
            }
//        city_name	location_name	NumberOfLabor	TotalContractValue	StartDate	EndDate	YearlyIncrement
        }else if(Objects.equals(task, "update")){
            sql = "UPDATE " + Employee.locationsTable + " SET city_name=?,location_name=?" +
                    ",NumberOfLabor=?,TotalContractValue =?,StartDate=?,EndDate=?,YearlyIncrement=?," +
                    "Department=? where location_name =? AND Department = ? ;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, locationName);
            preparedStatement.setInt(3, NumLabor);
            preparedStatement.setDouble(4, TotalValueOfContract);
            preparedStatement.setDate(5, new java.sql.Date(dateFormat.parse(startDate).getTime()));
            preparedStatement.setDate(6, new java.sql.Date(dateFormat.parse(endDate).getTime()));
            preparedStatement.setInt(7, YearlyIncrement);
            preparedStatement.setString(8, Department);
            preparedStatement.setString(9, locationName);
            preparedStatement.setString(10, Department);
            preparedStatement.execute();
            JOptionPane.showMessageDialog(panel, "تم تعديل التعاقد بنجاح !");
        }
    }
}
