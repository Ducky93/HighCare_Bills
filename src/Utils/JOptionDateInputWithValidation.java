package Utils;

import javax.swing.*;

/**
 * Created by mohab 2 on 21/07/2017.
 */
public class JOptionDateInputWithValidation {
    public JOptionDateInputWithValidation(){}
    public String gimmeTheCorrectDate(){
        // TODO Auto-generated method stub
        int month, day, year;

        String userInput = JOptionPane
                .showInputDialog("أدخل تاريخ لإضافة الفاتورة على الصيغة yyyy/mm/dd :", JOptionPane.RIGHT_ALIGNMENT);
        String[] date = userInput.split("/");
        String dd = date[2];
        String mm = date[1];
        String yyyy = date[0];
        month = Integer.parseInt(mm);
        day = Integer.parseInt(dd);
        year = Integer.parseInt(yyyy);
        boolean validLeapYear = false;
        boolean validDate = false;

        if (month >= 1 && month <= 12 && day >= 1 && day <= 31) {

            if (month == 4 || month == 9 || month == 6 || month == 11
                    && day <= 30) {

                validDate = true;
            }

            if( (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 11 || month == 12)
                    && (day <= 31))
            {
                validDate=true;
            }

            if ((year % 400 == 0) || (year % 4 == 0) && (year % 100 != 0)) {

                validLeapYear = true;

            }
            if ((month == 2 && day == 29) && (!validLeapYear)) {

                validDate = false;
            }
            if ((month == 2 && day <= 29) && (validLeapYear)) {

                validDate = true;
            }

//            if (validDate) {
//
//                JOptionPane.showMessageDialog(null, month + "/" + day + "/"
//                        + year + " is a valid date");
//            }

//            else if (!validDate) {
//                JOptionPane.showMessageDialog(null, "Your date is invalid");
//            }
        }
        if(validDate){
            return userInput;
        }else{
            JOptionPane.showMessageDialog(null, "التاريخ لا يتبع الصيغة المعطاة , تم إلغاء العملية");
            return "";
        }
    }
}
