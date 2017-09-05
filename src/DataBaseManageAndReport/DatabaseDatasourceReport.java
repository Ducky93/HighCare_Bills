package DataBaseManageAndReport; /**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2016 Ricardo Mariaca
 * http://www.dynamicreports.org
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */


import Forms.PrintBills;
import TablesDesign.myTableMode;
import Utils.GeneralCache;
import com.mysql.jdbc.ResultSet;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import tafqeet.tafqeetFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;


public class DatabaseDatasourceReport {
    private Connection connection;
    Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();
    NumberFormat numberFormat = NumberFormat.getNumberInstance(arabicLocale);
    private JasperPrint jasperPrint;
    public String printingDate;
    private String dbName = Employee.EmpDBName;
    String pathForHeaderImageSecurity="";
    static public boolean debuging=true;
    String companyName="";
    PreparedStatement preparedStatement;
    String filePathForReport ="";
    String filePathForReportJasper ="";
    String pathForRightIcon;
    String pathForLeftIcon;
    public DatabaseDatasourceReport(String nameOftheLocation,String DeptName,boolean preview,String dateOfPrinting,String billID) throws SQLException, FileNotFoundException {
        this.printingDate = dateOfPrinting;
        billID =  new BigDecimal(billID).toString(); //converts bill number from arabic to english numbers


        if(Objects.equals(DeptName, DatabaseManager.SECURITY_DEPT) || Objects.equals(DeptName, DatabaseManager.MONEYTRANSFERE_DEPT)){
            filePathForReport ="reports/highcareSecurity_bill.jrxml";
            filePathForReportJasper ="reports/highcareSecurity_bill.jasper";
            companyName = DatabaseManager.COMPANY_NAME_Security;
        }else if(Objects.equals(DeptName, DatabaseManager.CLEANING_DEPT)){
            companyName = DatabaseManager.COMPANY_NAME_DEV;
            filePathForReport = "reports/highcareSecurity_Dev.jrxml";
            filePathForReportJasper = "reports/highcareSecurity_Dev.jasper";
        }

        try {
            ResultSet rsInsertingData;
//            dataReportsGeneratorConn = dbManager.checkConnection(dataReportsGeneratorConn);
            if(GeneralCache.callCache().get("Company Data")==null) {
                String sqlInsertingData;
                sqlInsertingData = "SELECT * FROM " + Employee.companyDetails + " WHERE company_name = ?;";
                preparedStatement = MyMainConnection.getInstance().prepareStatement(sqlInsertingData);
                preparedStatement.setString(1, companyName);

                rsInsertingData = (ResultSet) preparedStatement.executeQuery();
                GeneralCache.callCache().put("Company Data", rsInsertingData);
            }else{
                rsInsertingData = (ResultSet) GeneralCache.callCache().get("Company Data");
            }
            String taxes_file = null;
            String sales_taxes = null;
            String commercialLog = null;
            int taxes_percent = 0;
            String taxes_card = null;
            Map<String, Object> parameters = new HashMap<String, Object>();
            pathForRightIcon = "reports/highcareDev_rightIcon.bmp";
            pathForLeftIcon = "reports/highcareDevIcon.bmp";
            pathForHeaderImageSecurity = "reports/fatora.bmp";

            checkIfDebugingOrNot();

            parameters.put("rightIconPathForReportDev",pathForRightIcon);
            parameters.put("leftIconPathForReportDev",pathForLeftIcon);

            while(rsInsertingData.next()) {
                taxes_file = rsInsertingData.getString("taxes_file");
                sales_taxes = rsInsertingData.getString("sales_tax");
                commercialLog = rsInsertingData.getString("commercial_log");
                taxes_card = rsInsertingData.getString("taxes_card");
                taxes_percent = rsInsertingData.getInt("current_sales_taxes_percent");
            }
            setDepartmentBillData(DeptName, dateOfPrinting, taxes_file, sales_taxes, commercialLog, taxes_percent, taxes_card, parameters);
            JasperReport jasperReport;
            try{
                jasperReport = (JasperReport) JRLoader.loadObject( new FileInputStream(new File(filePathForReportJasper)));
            }catch (FileNotFoundException e){
                JasperCompileManager.compileReportToFile(filePathForReport,filePathForReportJasper);
                jasperReport = (JasperReport) JRLoader.loadObject( new FileInputStream(new File(filePathForReportJasper)));
            }

            String sql = "SELECT * FROM "+ Employee.locationsBillsMonthly +" WHERE location_name =? AND Department in(?,?) AND DateOfPrinting = ? AND id=?;";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1,nameOftheLocation);
            preparedStatement.setString(2, PrintBills.sec_dept);
            preparedStatement.setString(3, PrintBills._dept);
            preparedStatement.setString(4,dateOfPrinting);
            preparedStatement.setString(5,billID);
            ResultSet rs = (ResultSet) preparedStatement.executeQuery();
            Collection<Map<String, ?>> list = new ArrayList<Map<String,?>>();
            Map<String, Object> hashMap = new HashMap<String, Object>();


            if(rs.next()) {
                double total = rs.getDouble("taxes")+rs.getDouble("bill_value");

                setBillReportFieldValues(rs, hashMap, total);
            }

            list.add(hashMap);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRMapCollectionDataSource(list));
            if(preview) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint);
                JasperViewer.viewReport(jasperPrint, false);
            }
//            jasperViewer.setVisible(true);


        } catch (SQLException | JRException e) {
            e.printStackTrace();
        }
    }

    private void setBillReportFieldValues(ResultSet rs, Map<String, Object> hashMap, double total) throws SQLException {
        String replacedValue;
        hashMap.put("id", numberFormat.format(rs.getInt("id")).replace("٫",".").replace("٬",""));
        hashMap.put("location_name", rs.getString("location_name"));

        replacedValue=processAMultiDecimalPlacesNumber(String.valueOf(((double)Math.round(rs.getDouble("bill_value")*100))/100)).replace("٫",".").replace("٬","");
        hashMap.put("bill_value", replacedValue);
        replacedValue=processAMultiDecimalPlacesNumber(String.valueOf(((double)Math.round(rs.getDouble("taxes")*100))/100)).replace("٫",".").replace("٬","");
        hashMap.put("taxes",replacedValue);
        total=((double)Math.round(total*100))/100;

        replacedValue=processAMultiDecimalPlacesNumber(String.valueOf(total)).replace("٫",".").replace("٬","");
        hashMap.put("total_billVal", replacedValue);
//        hashMap.put("bill_Text", arabitizeMyNumbers(setBillAccountNumberInBillText(rs.getString("bill_Text"))));
        hashMap.put("bill_Text", arabitizeMyNumbers(PrintBills.setBillAccountNumberInBillText(rs,companyName)));
        hashMap.put("tafqeet", new tafqeetFactory(String.valueOf(total)).ArabicNumber);
    }

    private void checkIfDebugingOrNot() {
        if(!debuging){
            filePathForReport=filePathForReport.replace("reports/","");
            filePathForReportJasper=filePathForReportJasper.replace("reports/","");
            pathForLeftIcon=pathForLeftIcon.replace("reports/","");
            pathForRightIcon=pathForRightIcon.replace("reports/","");
            pathForHeaderImageSecurity=pathForHeaderImageSecurity.replace("reports/","");
        }
    }

    private void setDepartmentBillData(String DeptName, String dateOfPrinting, String taxes_file, String sales_taxes, String commercialLog, int taxes_percent, String taxes_card, Map<String, Object> parameters) {
        if (Objects.equals(DeptName, DatabaseManager.SECURITY_DEPT) || Objects.equals(DeptName, DatabaseManager.MONEYTRANSFERE_DEPT)) {
            setParametersForSecurityDept(dateOfPrinting, parameters);
        } else if (Objects.equals(DeptName, DatabaseManager.CLEANING_DEPT)) {
            setParametersForDevDept(dateOfPrinting, taxes_file, sales_taxes, commercialLog, taxes_percent, taxes_card, parameters);
        }
    }

    private void setParametersForDevDept(String dateOfPrinting, String taxes_file, String sales_taxes, String commercialLog, int taxes_percent, String taxes_card, Map<String, Object> parameters) {
        parameters.put("company_addressP", arabitizeMyNumbers("أسيوط : شارع الجمهورية - برج المروة ت:2291402 - 2291403 - 2291404 /088"));
        parameters.put("billPrinting_dateP", arabitizeMyNumbers("تحريراً فى: " + dateOfPrinting));
        parameters.put("taxes_file", arabitizeMyNumbers("م.ض" +taxes_file));
        parameters.put("sales_taxes", arabitizeMyNumbers("ض.م."+sales_taxes));
        parameters.put("commercialLog", arabitizeMyNumbers("س.ت"+commercialLog + " ب.ض "+taxes_card));
        parameters.put("taxes_percent", arabitizeMyNumbers("القيمة المضافة "+taxes_percent + "%"));
    }

    private void setParametersForSecurityDept(String dateOfPrinting, Map<String, Object> parameters) {
        parameters.put("company_addressP", arabitizeMyNumbers("أسيوط : شارع الجمهورية - برج المروة ت:2291402 - 2291403 - 2291404 /088"));
        parameters.put("billPrinting_dateP", arabitizeMyNumbers("تحريراً فى: " + dateOfPrinting));
        parameters.put("taxes_file", arabitizeMyNumbers("م.ض(306/420) الشركات المساهمة بالقاهرة"));
        parameters.put("sales_taxes", arabitizeMyNumbers("ض.م.(200/176/633)"));
        parameters.put("commercialLog", arabitizeMyNumbers("س.ت(54142) ب.ض(12113)"));
        parameters.put("taxes_percent", arabitizeMyNumbers("القيمة المضافة 13%"));
        parameters.put("headerForSecurityBill",pathForHeaderImageSecurity);
    }

    public String arabitizeMyNumbers(String word) {
        String arabitizedWord = "";
        for (int i = 0; i < word.length(); i++) {
            if (myTableMode.isNum(String.valueOf((String.valueOf(word.charAt(i)))))) {

                arabitizedWord += numberFormat.format(Integer.parseInt(String.valueOf(word.charAt(i))));
            } else {
                arabitizedWord += word.charAt(i);
            }
        }
        return arabitizedWord;
    }
    public String processAMultiDecimalPlacesNumber(String inputNumber){
        String returnedValue="";
        String floats="";
        String[] currentNum = inputNumber.split("\\.");
        if(currentNum.length>1) {
            floats = currentNum[1];
        }
        returnedValue=currentNum[0];
        if(floats.length()==1){
            floats+="0";
        }
        returnedValue = addDecimalPlacesValues(returnedValue, floats);
        returnedValue = arabitizeMyNumbers(returnedValue);
        return returnedValue;
    }

    private String addDecimalPlacesValues(String returnedValue, String floats) {
        if(floats.length()>=2&&!floats.startsWith("00")){
            returnedValue+=".";
            for (int i = 0; i <2 ; i++) {
                returnedValue+=floats.charAt(i);
            }
        }
        return returnedValue;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

}