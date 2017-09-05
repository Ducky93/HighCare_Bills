package TablesDesign;

import DataBaseManageAndReport.Employee;
import DataBaseManageAndReport.MyMainConnection;
import Forms.PrintBills;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by mohab 2 on 11/25/2016.
 */
public class myTableMode implements TableModel {
    public static Vector<Vector<Object>> tablesVectorData = new Vector<>();

    public static List<Double> generalTotalOfNumbers = new ArrayList<Double>();
    public static List<Double> taxesTotalOfNumbers = new ArrayList<Double>();
    public static List<Double> TotalOfNumbersWithOutTaxes = new ArrayList<Double>();
    public myTableMode() throws SQLException {
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(arabicLocale);

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        Object currentObj;
        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {

                currentObj=rs.getObject(columnIndex);
                if(!isNum(String.valueOf(currentObj))){
                vector.add(currentObj);
                }
                else{
                    vector.add(currentObj);
                }
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
    public static String arabitizeMyNumbers(String word) {
        String arabitizedWord = "";
        Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(arabicLocale);
        int countDecimals = 1;
        boolean countIt = false;
        String floats="";
        int endIndex=0;
        for (int i = 0; i < word.length(); i++) {
            if (myTableMode.isNum(String.valueOf((String.valueOf(word.charAt(i)))))) {
                if(countDecimals!=3) {
                    arabitizedWord += numberFormat.format(Integer.parseInt(String.valueOf(word.charAt(i))));
                    if (Objects.equals(word.charAt(i),'.') || countIt) {
                        countDecimals++;
                        countIt = true;
                        floats+=word.charAt(i);
                    }
                }
            } else {

                if (Objects.equals(word.charAt(i),'.')) {
                    endIndex = i;
                }
                    if (Objects.equals(word.charAt(i),'.')||countIt) {

                        countIt = true;
                        floats+=word.charAt(i);
                    }
                arabitizedWord += word.charAt(i);
            }
        }
        if(floats.length()==2 && Objects.equals(floats,".0")){
            arabitizedWord=arabitizedWord.substring(0,endIndex);
        }
        return arabitizedWord;
    }
    public static DefaultTableModel buildTableModelForBills(ResultSet rs,String name)
            throws SQLException {
        String dbName = Employee.EmpDBName;
//        DatabaseManager dbManager = new DatabaseManager();
//        Connection conn=null;
//        Connection tableModelConn = dbManager.checkConnection(conn);
        Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(arabicLocale);

        ResultSetMetaData metaData = rs.getMetaData();
        taxesTotalOfNumbers.clear();
        TotalOfNumbersWithOutTaxes.clear();
        generalTotalOfNumbers.clear();
        // names of columns

        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount()+1;

        if(Objects.equals(name, Employee.locationsBills)) {
            for (int column = 1; column <= columnCount; column++) {
                if(column != 1) {
                    columnNames.add(metaData.getColumnName(column-1));
                }else{
                    columnNames.add("م");
                }
            }
        }else if(Objects.equals(name, Employee.locationsBillsMonthly)) {
            for (int column = 1; column <= columnCount-1; column++) {
                if(column != 1) {
                    columnNames.add(metaData.getColumnName(column-1));
                }else{
                    columnNames.add("م");
                }

            }
        }

        Vector<Vector<Object>> generalVectorMap;
        Object currentObj;
        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        int counter = 1;

        while (rs.next()) {

            Vector<Object> vector = new Vector<>();

            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                if(columnIndex!=1) {
                    currentObj = rs.getObject(columnIndex-1);
                        vector.add(currentObj);
                }else{
                    vector.add(counter++);
                }
            }
            if(Objects.equals(name, Employee.locationsBills)) {
                Object current = vector.get(5);
                vector.set(5, String.valueOf(Double.parseDouble(String.valueOf(vector.get(3)))
                        + Double.parseDouble(String.valueOf(vector.get(4)))));
                vector.add(current);




                for (int i = 0; i < vector.size(); i++) {

                    if (isNum(String.valueOf(vector.get(i)))) {
                        try {
                            if(i==3) {
                                TotalOfNumbersWithOutTaxes.add((Double) vector.get(3));
                            }if(i==4){
                                taxesTotalOfNumbers.add((Double) vector.get(4));
                            }if(i==5){
                                generalTotalOfNumbers.add(Double.parseDouble(String.valueOf(vector.get(5))));
//                                vector.set(5, arabitizeMyNumbers(String.valueOf(vector.get(5))));
                            }
                            vector.set(i,numberFormat.format(Double.parseDouble(String.valueOf(Math.round(Double.parseDouble(String.valueOf(vector.get(i)))*100)))/100).replace("٬", "").replace("٫", "."));

                        } catch (IllegalArgumentException ignored) {

                        }
                    }
                }
            }

            data.add(vector);
        }
        generalVectorMap=data;

        if(Objects.equals(name, Employee.locationsBillsMonthly)) {
            String getPrintingDate="";
                Vector<Vector<Object>> dataMonthly = new Vector<>();
                    String sql = "Select  a.location_name, a.Department,b.location_name,b.Department,b.DateOfPrinting from "+Employee.locationsBills+" a,"+
                            Employee.locationsBillsMonthly+" b WHERE  " +
                            "a.activeBill =1  AND a.location_name = b.location_name  AND a.Department in(?,?) AND a.Department = b.Department  ORDER BY b.DateOfPrinting DESC; ";
                        PreparedStatement preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
                        preparedStatement.setString(1, PrintBills._dept);
                        preparedStatement.setString(2, PrintBills.sec_dept);
                    ResultSet newRs = preparedStatement.executeQuery();
                    ResultSetMetaData metaDataMain = newRs.getMetaData();

                    if(!newRs.next()){
                        String sqlTemp = "Select location_name,Department,bill_Text,bill_value from "+Employee.locationsBills+" WHERE  " +
                                " activeBill =1 AND Department in(?,?) ;";
                        preparedStatement = MyMainConnection.getInstance().prepareStatement(sqlTemp);
                        preparedStatement.setString(1, PrintBills._dept);
                        preparedStatement.setString(2, PrintBills.sec_dept);
                        newRs = preparedStatement.executeQuery();
                        metaDataMain = newRs.getMetaData();
                    }
                    newRs.beforeFirst();
                    int columnCountMain = metaDataMain.getColumnCount()+1;
                    int counterMain =1;
                    while (newRs.next()) {

                        Vector<Object> vectorMonthly = new Vector<Object>();
                        for (int columnIndex = 1; columnIndex <= columnCountMain; columnIndex++) {
                            if (columnIndex != 1) {
                                if (columnIndex < columnCountMain) {
                                    currentObj = newRs.getObject(columnIndex-1);
                                    if (columnIndex != 3 && columnIndex != 4 ) {
                                        vectorMonthly.add(currentObj);
                                    }
                                } else if (columnIndex == columnCountMain && (!newRs.getObject(3).equals(newRs.getObject(1))
                                        || !newRs.getObject(2).equals(newRs.getObject(4)))) {

                                    currentObj = "1920/1/1";
                                    vectorMonthly.add(currentObj);
                                } else if (columnIndex == columnCountMain && (newRs.getObject(3).equals(newRs.getObject(1))
                                        && newRs.getObject(2).equals(newRs.getObject(4)))) {

                                    currentObj = newRs.getObject(5);
                                    getPrintingDate = newRs.getString(5);
                                    vectorMonthly.add(currentObj);
                                }
                            }else{
                                vectorMonthly.add(counterMain++);
                            }
                        }
                        for (int i = 0; i < vectorMonthly.size(); i++) {

                            if (isNum(String.valueOf(vectorMonthly.get(i)))) {
                                vectorMonthly.set(i, numberFormat.format(Math.round(Double.parseDouble(String.valueOf(vectorMonthly.get(i)))*100)/100).replace("٬", "").replace("٫", "."));

                            }
                        }
                        dataMonthly.add(vectorMonthly);
                    }
            sql = "Select  location_name, Department,activeBill from "+ Employee.locationsBills+
                    " WHERE  EXISTS (SELECT  DISTINCT location_name, Department,activeBill FROM " +Employee.locationsBillsMonthly+"  ) AND "+
                    "activeBill =1 AND Department in(?,?)   ; ";
            preparedStatement = MyMainConnection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, PrintBills._dept);
            preparedStatement.setString(2, PrintBills.sec_dept);
            ResultSet nextRs = preparedStatement.executeQuery();
            metaDataMain = nextRs.getMetaData();


            columnCountMain = metaDataMain.getColumnCount();
            while (nextRs.next()) {
                Vector<Object> vectorMonthly = new Vector<Object>();
                Vector<Object> test = new Vector<Object>();
                    for (int columnIndex = 1; columnIndex <= columnCountMain+1; columnIndex++) {
                        if(columnIndex!=1) {
                            if (columnIndex != 4) {
                                currentObj = nextRs.getObject(columnIndex-1);
                                vectorMonthly.add(currentObj);
                                test.add(currentObj);
                            } else if (columnIndex == 4) {
                                currentObj = "1920/1/1";
                                vectorMonthly.add(currentObj);
                                test.add(currentObj);
                            }
                        }else{
                            vectorMonthly.add(numberFormat.format(counterMain++));
                        }
                    }
                final boolean[] weHaveItAlrdy = {false};

                test.set(2,getPrintingDate);


                dataMonthly.forEach(e->

                    {
                        if(Objects.equals(e.get(1),test.get(0))&&Objects.equals(e.get(2),test.get(1))){
                        weHaveItAlrdy[0] = true;
                    }
                });


                if(!weHaveItAlrdy[0]) {
                    dataMonthly.add(vectorMonthly);
                }
                for (int i = 0; i < dataMonthly.size(); i++) {
                    for (int j = i+1; j < dataMonthly.size(); j++) {
                        if(Objects.equals(dataMonthly.get(j).get(1),dataMonthly.get(i).get(1))
                                &&Objects.equals(dataMonthly.get(j).get(2),dataMonthly.get(i).get(2))){
                            dataMonthly.remove(j);
                        }
//                        if(Objects.equals(dataMonthly.get(j).get(1),dataMonthly.get(i).get(1))
//                                &&dataMonthly.get(j).get(2)==dataMonthly.get(i).get(1)){
//                            dataMonthly.remove(j);
//                        }
                    }
                }
            }
            generalVectorMap=dataMonthly;
            }
        tablesVectorData = generalVectorMap;

        return new DefaultTableModel(generalVectorMap, columnNames);
    }

    public static boolean isNum(String strNum) {
        boolean ret = true;
        try {

            Double.parseDouble(strNum);

        } catch (IllegalArgumentException e1) {
            ret = false;
        }
        return ret;
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
