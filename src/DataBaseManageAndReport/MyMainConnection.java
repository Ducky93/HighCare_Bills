package DataBaseManageAndReport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by mohab 2 on 19/07/2017.
 */
public class MyMainConnection {
    private static Connection instance = null;

    protected MyMainConnection() {
        // Exists only to defeat instantiation.

    }

    private Connection instaMyConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            instance = DriverManager.getConnection("jdbc:mysql://johnny.heliohost.org:3306", "r2gmduck_empDB", "0mo1230m123");
            instance = DriverManager.getConnection("jdbc:mysql://johnny.heliohost.org:3306/"+Employee.EmpDBName+"?characterEncoding=UTF-8","r2gmduck_empDB","0mo1230m123");
            String sql = "CREATE DATABASE IF NOT EXISTS " + Employee.EmpDBName;
            instance.prepareStatement(sql).execute();
        } catch (InstantiationException
                | IllegalAccessException
                | ClassNotFoundException
                | SQLException e) {
            e.printStackTrace();
        }
        return instance;
     }


    public static Connection getInstance() {
        if(instance == null) {
            instance = new MyMainConnection().instaMyConnection();
        }
        return instance;
    }
}
