package extraCodeExperiments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by mohab 2 on 05/12/2016.
 */
public class UpdateTheDepartmentNames {
    public UpdateTheDepartmentNames() {
    }
    public static Connection connection;

    public static PreparedStatement preparedStatement;

    public static String tbl_name1 = "locationspercities";
    public static String tbl_name2 = "locations_bills";
    Connection getConnection(String dbName) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            connection = DriverManager.getConnection("jdbc:mysql://localhost/","root","0mo1230m123");

            connection.close();
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,"root","0mo1230m123");
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static void main(String[] args) throws SQLException, ParseException {
//        city_name	location_name	NumberOfLabor	TotalContractValue	StartDate	EndDate	YearlyIncrement	Department
//        new DataBaseManageAndReport.Employee();
//        String sql1 = "SELECT location_name,Department FROM "+tbl_name1+";";
//        String sql2 = "SELECT location_name,Department,bill_Text FROM "+tbl_name2+";";
//        String sql3Update="";
//        String sqltest;
//        DataBaseManageAndReport.DatabaseManager dbManager = new DataBaseManageAndReport.DatabaseManager(DataBaseManageAndReport.Employee.EmpDBName);
//        Connection conn = dbManager.getConnection(DataBaseManageAndReport.Employee.EmpDBName);
////        PreparedStatement preparedStatement = ;
//        ResultSet rs1=conn.prepareStatement(sql2).executeQuery();
//        while (rs1.next()){
////            sql3Update = "UPDATE "+tbl_name2+" SET Department = "+rs1.getString("Department")+" WHERE location_name = "+rs1.getString("Department")+";";
//            if(Objects.equals(rs1.getString("Department"), DataBaseManageAndReport.DatabaseManager.CLEANING_DEPT)) {
//                System.out.println("الموقع : " + rs1.getString("location_name"));
//            }
//        }
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        sqltest = "ALTER TABLE "+tbl_name2+
//                " DROP FOREIGN KEY locations_bills_ibfk_1";
//        conn.prepareStatement(sqltest).execute();
//     Exception in thread "main" com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`employee`.`locations_bills`, CONSTRAINT `locations_bills_ibfk_1` FOREIGN KEY (`city_name`, `location_name`, `Department`) REFERENCES `locationspercities` (`city_name`, `location_name`, `Depar)


    }
}
//while (rs1.next()){
//        sql3Update="Update "+tbl_name2+" SET Department = '" + rs1.getString("Department") + "' WHERE location_name = '"
//        +rs1.getString("location_name")+"';";
//        conn.prepareStatement(sql3Update).execute();
//
//        }
//while (rs1.next()){
//        if(rs1.getString("bill_Text").contains("حراســــة")||rs1.getString("bill_Text").contains("الحــراســـة")
//        ||rs1.getString("bill_Text").contains("حراسة")){
//        sql3Update="UPDATE "+tbl_name1+" SET Department = '"+ DataBaseManageAndReport.DatabaseManager.SECURITY_DEPT +"' WHERE location_name = '"+
//        rs1.getString("location_name")+"';";
//        System.out.println(sql3Update);
//        conn.prepareStatement(sql3Update).execute();
//        sql3Update="UPDATE "+tbl_name2+" SET Department = '"+ DataBaseManageAndReport.DatabaseManager.SECURITY_DEPT +"' WHERE location_name = '"+
//        rs1.getString("location_name")+"';";
//        conn.prepareStatement(sql3Update).execute();
//        System.out.println("success!");
//        }else if(rs1.getString("bill_Text").contains("نظافة")||rs1.getString("bill_Text").contains("النظــــافة")
//        ||rs1.getString("bill_Text").contains("الحشرات")){
//        sql3Update="UPDATE "+tbl_name1+" SET Department = '"+ DataBaseManageAndReport.DatabaseManager.CLEANING_DEPT +"' WHERE location_name = '"+
//        rs1.getString("location_name")+"';";
//        conn.prepareStatement(sql3Update).execute();
//        sql3Update="UPDATE "+tbl_name2+" SET Department = '"+ DataBaseManageAndReport.DatabaseManager.CLEANING_DEPT +"' WHERE location_name = '"+
//        rs1.getString("location_name")+"';";
//        conn.prepareStatement(sql3Update).execute();
//        System.out.println("success!");
//        }else if(rs1.getString("bill_Text").contains("نقل")){
//        sql3Update="UPDATE "+tbl_name1+" SET Department = '"+ DataBaseManageAndReport.DatabaseManager.MONEYTRANSFERE_DEPT +"' WHERE location_name = '"+
//        rs1.getString("location_name")+"';";
//        conn.prepareStatement(sql3Update).execute();
//        sql3Update="UPDATE "+tbl_name2+" SET Department = '"+ DataBaseManageAndReport.DatabaseManager.MONEYTRANSFERE_DEPT +"' WHERE location_name = '"+
//        rs1.getString("location_name")+"';";
//        conn.prepareStatement(sql3Update).execute();
//        System.out.println("success!");
//        }
//
//
//        }