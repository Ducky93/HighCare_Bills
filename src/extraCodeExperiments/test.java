import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class test extends JPanel {
    public static Connection connection;
    public static String sql;
    public static PreparedStatement preparedStatement;


    public test() throws SQLException {

    }


    public static void main(String[] args) throws SQLException, ParseException {
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        System.out.println(new Date(dateFormat.parse("2017-10-20").getTime()));
//        Calendar currentDate = Calendar.getInstance();
//        currentDate.clear();
//        currentDate.set(2010, Calendar.JANUARY, 1);
//        Calendar lastDate = Calendar.getInstance();
//        lastDate.clear();
//        lastDate.set(2020, Calendar.DECEMBER, 31);

        System.out.println(correctMyDate().get("0009-07-06"));
        System.out.println(correctMyDate().get("0007-07-07"));
    }
    public static Map<String,Date> correctMyDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Map<String, Date> corrections = new HashMap<>(5500); // capacity for 11 years

        Calendar currentDate = Calendar.getInstance();
        currentDate.clear();
        currentDate.set(2010, Calendar.JANUARY, 1);

        Calendar lastDate = Calendar.getInstance();
        lastDate.clear();
        lastDate.set(2020, Calendar.DECEMBER, 31);

        do {
            try {
                Date correctDate = new java.sql.Date(currentDate.getTimeInMillis());
                String correctString = correctDate.toString();
                String wrongString = new java.sql.Date(dateFormat.parse(correctString).getTime()).toString();

                if (corrections.containsKey(wrongString)) {
//                    System.out.println("Duplicate wrong string " + wrongString);
                }

                corrections.put(wrongString, correctDate);

                currentDate.add(Calendar.DATE, 1);
            } catch (ParseException ignore) {

            }
        } while (! currentDate.after(lastDate));
return corrections;
    }
}
