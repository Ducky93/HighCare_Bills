//package extraCodeExperiments;
//
//import DataBaseManageAndReport.DatabaseManager;
//import DataBaseManageAndReport.Employee;
//import tafqeet.tafqeetFactory;
//
//import javax.swing.*;
//import java.sql.*;
//import java.text.DateFormatSymbols;
//import java.util.Objects;
//
//import static Forms.PrintBills._dept;
//import static Forms.PrintBills.sec_dept;
//
//
///**
// * Created by mohab 2 on 11/03/2017.
// */
//public class Bill {
//    int id;
//    String dateOfPrinting;
//    String locationName;
//    double pureCost;
//    double taxesAddValue;
//    String billText;
//    String tafqeet;
//    PreparedStatement preparedStatement;
//    private Array departmentNames;
//    public Bill(String dateOfPrinting){
//        this.dateOfPrinting=dateOfPrinting;
//    }
//    public void insertTheBillToCurrentMonth(JTable jTable){
//        if (dateOfPrinting.length() >= 8 & !Objects.equals(dateOfPrinting, "")) {
//                String[] seperatedLetters = dateOfPrinting.split("/");
//                String year = "2013";
//                String monthNumber;
//                String monthName;
//                String sqlMonth;
//
//                boolean modifiedYear = false;
//                if (seperatedLetters[0].length() == 4) {
//                    year = seperatedLetters[0];
//                }
//                if (seperatedLetters[2].length() == 4) {
//                    year = seperatedLetters[2];
//                }
//                monthNumber = seperatedLetters[1];
//                String previousMonthName;
//                try {
//                    previousMonthName = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNumber) - 2];
//                } catch (ArrayIndexOutOfBoundsException e1) {
//                    previousMonthName = new DateFormatSymbols().getMonths()[11];
//                    year = String.valueOf(Integer.parseInt(year) - 1);
//                    modifiedYear = true;
//                }
//                monthName = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNumber) - 1];
//            if (jTable.getSelectedRows().length > 0) {
//                    sqlMonth = "SELECT * from " + Employee.locationsBills + " WHERE location_name = ?"
//                            + " AND Department in (?) ;";
//                    preparedStatement = printBillsConn.prepareStatement(sqlMonth);
//                    preparedStatement.setString(1, (String) tbl_main.getValueAt(tbl_main.getSelectedRow(), 1));
//                departmentNames =
//                try (Connection conn = new DatabaseManager().getConnection(Employee.EmpDBName)){
//                    preparedStatement.setArray(2, (new Department().getDepartmentNames()));
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                preparedStatement.setArray(3, sec_dept);
//                    ResultSet rs = preparedStatement.executeQuery();
//                    sql = "SELECT id FROM " + Employee.locationsBillsMonthly + " WHERE Department in (?,?) ORDER BY id ASC;";
//                    preparedStatement = printBillsConn.prepareStatement(sql);
//                    preparedStatement.setString(1, _dept);
//                    preparedStatement.setString(2, sec_dept);
//                    ResultSet r_s = preparedStatement.executeQuery();
//                    while (r_s.next()) {
//                        r_s.last();
//
//                        lastIDvalue = r_s.getInt("id");
//                    }
//
//                    if (lastIDvalue == 0) {
//                        lastIDvalue = 999;
//                    }
//                    lastIDvalue++;
//                    while (rs.next()) {
////            city_name	DateOfPrinting	location_name	Department	bill_value	taxes	bill_Text	activeBill	id	tafqeet
//
//                        sql = "INSERT INTO " + Employee.locationsBillsMonthly + " VALUES (?,?,?,?,?,?,?,?,?,?) ;";
//                        preparedStatement = printBillsConn.prepareStatement(sql);
//                        preparedStatement.setString(1, rs.getString("city_name"));
//                        preparedStatement.setString(2, dateOfPrinting);
//                        preparedStatement.setString(3, rs.getString("location_name"));
//                        preparedStatement.setString(4, rs.getString("Department"));
//                        preparedStatement.setDouble(5, rs.getDouble("bill_value"));
//                        preparedStatement.setDouble(6, rs.getDouble("taxes"));
//                        String billText = setBillAccountNumberInBillText(rs, printBillsConn, companyName);
//                        if (billText.contains("%Months2") && modifiedYear) {
//                            billText = billText
//                                    .replace("%Year1", String.valueOf(Integer.parseInt(year) + 1));
//                        } else if (!billText.contains("%Months2")) {
//                            billText = billText
//                                    .replace("%Year1", String.valueOf(Integer.parseInt(year)));
//                        }
//                        billText = billText.replace("%Months2", monthName).replace("%Months1", previousMonthName)
//                                .replace("%Year1", year);
//                        preparedStatement.setString(7, billText);
//                        preparedStatement.setBoolean(8, rs.getBoolean("activeBill"));
//
//                        preparedStatement.setInt(9, lastIDvalue);
//
//
//                        sum = String.valueOf((Double.parseDouble(rs.getString("bill_value"))));
//                        sum = String.valueOf(Double.parseDouble(sum) + (Double.parseDouble(sum) * taxes_percent * .01));
//                        preparedStatement.setString(10, new tafqeetFactory(sum).ArabicNumber);
//                        preparedStatement.execute();
//                        tbl_main.removeAll();
//                        tbl_Monthlybills.removeAll();
//                        String requested_date;
//
//                        requested_date = "%";
//                        requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
//                        requestingTheDate = requested_date;
//
//                    }
//            }
//            tbl_main.removeAll();
//            tbl_Monthlybills.removeAll();
//            String requested_date;
//
//            requested_date = "%";
//            requested_date += drop_yearPrint.getSelectedItem().toString() + "/" + String.valueOf(drop_monthPrint.getSelectedIndex() + 1) + "/%";
//            requestingTheDate = requested_date;
//
//
//            populate_updateSum_optimizeLook_JTable();
//
//        }
//    }
//
//    private void populate_updateSum_optimizeLook_JTable() {
//
//    }
//
//    public void addBillToReport(){
//
//    }
//
//}
