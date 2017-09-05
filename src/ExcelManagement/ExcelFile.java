package ExcelManagement;

import DataBaseManageAndReport.DatabaseManager;
import DataBaseManageAndReport.Employee;
import DataBaseManageAndReport.MyMainConnection;
import Forms.PrintBills;
import TablesDesign.myTableMode;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ExcelFile {
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
    Connection connection;
    PreparedStatement preparedStatement;
    public ExcelFile(JTable theOperatedTable) throws SQLException, IOException {
        JFileChooser fileChooser = new JFileChooser();

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Excel Sheet");
            HSSFRow rowhead = sheet.createRow((short) 0);
            sheet.getWorkbook().getSheetAt(0).setRightToLeft(true);

            rowhead.createCell((short) 6).setCellValue("الإجمالي");
            rowhead.createCell((short) 5).setCellValue("القيمة المضافة");
            rowhead.createCell((short) 4).setCellValue("قيمة الفاتورة");
            rowhead.createCell((short) 3).setCellValue("إسم العميل");
            rowhead.createCell((short) 2).setCellValue("التاريخ");
            rowhead.createCell((short) 1).setCellValue("رقم الفاتورة");
            rowhead.createCell((short) 0).setCellValue("م");
            String sql = "SELECT DateOfPrinting FROM " + Employee.locationsBillsMonthly + " WHERE "
                    + "location_name = ? AND Department in(?,?) AND id= ? AND DateOfPrinting LIKE '" + PrintBills.requestingTheDate + "';";
            ResultSet rs;
            String date = "";
//            totalBillsReportGeneratorConn = dbManager.checkConnection(totalBillsReportGeneratorConn);
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(2, PrintBills._dept);
            preparedStatement.setString(3, PrintBills.sec_dept);

            int index = 1;
            for (int i = 0; i < theOperatedTable.getSelectedRows().length; i++) {
                preparedStatement.setString(1, String.valueOf(theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 2)));
                preparedStatement.setInt(4, Integer.parseInt(String.valueOf(theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 1))));
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    date = rs.getString("DateOfPrinting");
                }
                HSSFRow row = sheet.createRow((short) index);
                row.createCell((short) 6).setCellValue((String) theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 5));
                row.createCell((short) 5).setCellValue((String) theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 4));
                row.createCell((short) 4).setCellValue((String) theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 3));
                row.createCell((short) 3).setCellValue((String) theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 2));
                row.createCell((short) 2).setCellValue(myTableMode.arabitizeMyNumbers(date));
                row.createCell((short) 1).setCellValue((String) theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 1));
                row.createCell((short) 0).setCellValue(myTableMode.arabitizeMyNumbers(String.valueOf(index)));
                index++;
            }
            String savingPath;
            if(!selectedFile.getPath().contains(".xls")) {
                savingPath=selectedFile.getPath()+".xls";
            }else{
                savingPath=selectedFile.getPath();
            }
            FileOutputStream fileOut = new FileOutputStream(savingPath);
            wb.write(fileOut);
            fileOut.close();
            System.out.println("Data is saved in excel file.");
            System.out.println("Regards .. E n g.M o h a b M o r s y A b d E l R a h m a n 2 0 1 7");
        }

    }

}