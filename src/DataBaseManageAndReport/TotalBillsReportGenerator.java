package DataBaseManageAndReport;

import Forms.*;
import TablesDesign.myTableMode;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by mohab 2 on 19/12/2016.
 */
public class TotalBillsReportGenerator {
    private Connection connection;
    Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();
    NumberFormat numberFormat = NumberFormat.getNumberInstance(arabicLocale);
    private JasperPrint jasperPrint;
    public String printingDate;
    private String dbName = Employee.EmpDBName;
    private DatabaseManager dbManager = new DatabaseManager();
    PreparedStatement preparedStatement;
    private String filePathForReport;
    private String filePathForReportJasper;
    private String pathForRightIcon = "reports/highcareDev_rightIcon.bmp";
    private String pathForLeftIcon = "reports/highcareDevIcon.bmp";
    private Map<String, Object> parameters = new HashMap<String, Object>();
    private JasperReport jasperReport;
    List<SalesReport> dataSourceListforSales = new ArrayList<>();
    List<HandingBillsReport> dataSourceListforHadnding = new ArrayList<>();
    private JTable theOperatedTable;

    public TotalBillsReportGenerator(JTable myCurrentSelectedTable,boolean handing) throws SQLException, FileNotFoundException, JRException {
        theOperatedTable=myCurrentSelectedTable;

        if(handing){
            fillHandingReport();
        }else {

                SalesReport.countIt = 1;
                SalesReport.sumBilLValue = 0;
                SalesReport.sumExtraValue = 0;
                SalesReport.sumTotal = 0;
                fillSalesReport();
        }
    }
    private void fillHandingReport() throws FileNotFoundException, JRException, SQLException {
        getPathOfJRXMLHandingReport();
        compileJRXML_to_Jasper();
        getDataForHandingReportTable();
        fillInHandingReportParameters();
        viewTheReport();
    }
    private void fillSalesReport() throws FileNotFoundException, JRException, SQLException {
        getPathOfJRXMLSalesReport();
        compileJRXML_to_Jasper();
        getDataForSalesReportTable();
        fillInSalesReportParameters();
        viewTheReport();
    }

    private void fillInSalesReportParameters() {
        parameters.put("HeaderOfReportParam", myTableMode.arabitizeMyNumbers("كشف مبيعات الشركة عن شهر "+ PrintBills.monthOfPrinting
                +" عام "+ PrintBills.YearOfPrinting));
        parameters.put("tableDatasetSource", new JRBeanCollectionDataSource(dataSourceListforSales));
        parameters.put("totalBillValue", myTableMode.arabitizeMyNumbers(String.valueOf(SalesReport.sumBilLValue)));
        parameters.put("totalExtraValue", myTableMode.arabitizeMyNumbers(String.valueOf(SalesReport.sumExtraValue)));
        parameters.put("generalTotal", myTableMode.arabitizeMyNumbers(String.valueOf(SalesReport.sumTotal)));
        parameters.put("rightIconPathForReportDev",pathForRightIcon);
        parameters.put("leftIconPathForReportDev",pathForLeftIcon);
    }

    private void getDataForSalesReportTable() throws SQLException {
        String sql = "SELECT DateOfPrinting,bill_value,taxes FROM " + Employee.locationsBillsMonthly + " WHERE "
                + "location_name = ? AND Department in(?,?) AND id = ? AND DateOfPrinting LIKE '" + PrintBills.requestingTheDate + "';";
        ResultSet rs;
        String date = "";
//        totalBillsReportGeneratorConn = dbManager.checkConnection(totalBillsReportGeneratorConn);
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(2, PrintBills._dept);
        preparedStatement.setString(3, PrintBills.sec_dept);

        for (int i = 0; i < theOperatedTable.getSelectedRows().length; i++) {
            preparedStatement.setString(1, String.valueOf(theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 2)));
            preparedStatement.setInt(4, Integer.parseInt(String.valueOf(theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 1))));
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SalesReport.sumBilLValue += rs.getDouble("bill_value");
                SalesReport.sumExtraValue += rs.getDouble("taxes");
                date = rs.getString("DateOfPrinting");
            }
            SalesReport.sumBilLValue=(double)Math.round(SalesReport.sumBilLValue*100)/100;
            SalesReport.sumExtraValue=(double)Math.round(SalesReport.sumExtraValue*100)/100;
            dataSourceListforSales.add(new SalesReport(
                    theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 2),
                    theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 3),
                    theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 4),
                    theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 5),
                    theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i], 1), myTableMode.arabitizeMyNumbers(date)
            ));
        }
        SalesReport.sumTotal = SalesReport.sumBilLValue + SalesReport.sumExtraValue;
    }

    private void getPathOfJRXMLSalesReport() {
        if(Objects.equals(LoginPage.deptNameForUser, "النظافة")) {
            filePathForReport = "reports/SalesReportDev_HighCare.jrxml";
            filePathForReportJasper = "reports/SalesReportDev_HighCare.jasper";
        }else if(Objects.equals(LoginPage.deptNameForUser, "الحراسة")) {
            filePathForReport = "reports/SalesReportSecurity_HighCare.jrxml";
            filePathForReportJasper = "reports/SalesReportSecurity_HighCare.jasper";
        }
        checkIfDebugingForHandingAndTotalSalesReports();
    }

    private void checkIfDebugingForHandingAndTotalSalesReports() {
        if(!DatabaseDatasourceReport.debuging){
            filePathForReport=filePathForReport.replace("reports/","");
            filePathForReportJasper=filePathForReportJasper.replace("reports/","");
            pathForLeftIcon=pathForLeftIcon.replace("reports/","");
            pathForRightIcon=pathForRightIcon.replace("reports/","");
        }
    }

    private void viewTheReport() throws JRException{
        jasperPrint = net.sf.jasperreports.engine.JasperFillManager.
                fillReport(jasperReport, parameters,new net.sf.jasperreports.engine.JREmptyDataSource(1)
                );
        JasperViewer.viewReport(jasperPrint, false);
    }

    private void getDataForHandingReportTable() throws SQLException {
        String sql = "SELECT DateOfPrinting FROM "+ Employee.locationsBillsMonthly + " WHERE "
                +"location_name = ? AND Department in( ? ,?)AND DateOfPrinting LIKE '"+ PrintBills.requestingTheDate +"';";
        ResultSet rs ;
        String date="";
//        totalBillsReportGeneratorConn=dbManager.checkConnection(totalBillsReportGeneratorConn);
        preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
        preparedStatement.setString(2, PrintBills._dept);
        preparedStatement.setString(3, PrintBills.sec_dept);
        for (int i = 0; i <theOperatedTable.getSelectedRows().length ; i++) {
            preparedStatement.setString(1, String.valueOf(theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i],2)));
            rs=preparedStatement.executeQuery();
            while (rs.next()){
                date = rs.getString("DateOfPrinting");
            }
            dataSourceListforHadnding.add(new HandingBillsReport(
                    theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i],2),
                    theOperatedTable.getValueAt(theOperatedTable.getSelectedRows()[i],1),
                    myTableMode.arabitizeMyNumbers(date)
                    ));
        }
    }

    private void fillInHandingReportParameters() {
        parameters.put("tableDatasetSource",new JRBeanCollectionDataSource(dataSourceListforHadnding));
        parameters.put("HeaderOfReportParam","بيان تسليم فواتير");
        parameters.put("rightIconPathForReportDev",pathForRightIcon);
        parameters.put("leftIconPathForReportDev",pathForLeftIcon);
    }

    private void getPathOfJRXMLHandingReport() {

        if(Objects.equals(LoginPage.deptNameForUser, "النظافة")) {
            filePathForReport = "reports/HandingDev_HighCare.jrxml";
            filePathForReportJasper = "reports/HandingDev_HighCare.jasper";
        }else if(Objects.equals(LoginPage.deptNameForUser, "الحراسة")) {
            filePathForReport = "reports/HandingSecurity_HighCare.jrxml";
            filePathForReportJasper = "reports/HandingSecurity_HighCare.jasper";
        }
        checkIfDebugingForHandingAndTotalSalesReports();
    }
    private void compileJRXML_to_Jasper() throws JRException, FileNotFoundException {
        try{
            jasperReport = (JasperReport) JRLoader.loadObject( new FileInputStream(new File(filePathForReportJasper)));
        }catch (FileNotFoundException e){
            net.sf.jasperreports.engine.JasperCompileManager.compileReportToFile(filePathForReport,filePathForReportJasper);
            jasperReport = (JasperReport) JRLoader.loadObject( new FileInputStream(new File(filePathForReportJasper)));
        }
    }
}
