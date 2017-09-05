package DataBaseManageAndReport;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;

/**
 * Created by mohab 2 on 08/03/2017.
 */
public class BillTextManagement {
    String currentYear;
    String currentlyMonth;
    String previousToCurrentMonth;
    boolean currentModifiedYear;

    public String getCurrentYear() {
        return currentYear;
    }

    public String getCurrentlyMonth() {
        return currentlyMonth;
    }

    public String getPreviousToCurrentMonth() {
        return previousToCurrentMonth;
    }

    public boolean isCurrentModifiedYear() {
        return currentModifiedYear;
    }

    public BillTextManagement(String dateOfPrinting) {
        String[] seperatedLetters = dateOfPrinting.split("/");
        String year = "2013";
        String monthNumber;
        String monthName;
        String sqlMonth;
        String sum = "";
        DecimalFormat df;
        boolean modifiedYear = false;
        if (seperatedLetters[0].length() == 4) {
            year = seperatedLetters[0];
        }
        if (seperatedLetters[2].length() == 4) {
            year = seperatedLetters[2];
        }
        monthNumber = seperatedLetters[1];
        String previousMonthName;
        try {
            previousMonthName = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNumber) - 2];
        } catch (ArrayIndexOutOfBoundsException e1) {
            previousMonthName = new DateFormatSymbols().getMonths()[11];
            year = String.valueOf(Integer.parseInt(year) - 1);
            modifiedYear = true;
        }
        monthName = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNumber) - 1];
        currentYear = year;
        currentlyMonth = monthName;
        previousToCurrentMonth = previousMonthName;
        currentModifiedYear = modifiedYear;
    }

}
