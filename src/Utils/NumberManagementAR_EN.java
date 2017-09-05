package Utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by mohab 2 on 13/07/2017.
 */
public class NumberManagementAR_EN {

    public static String convertArabicNumbertoEnglishNumber(String arabicNumber){
        String[] strings = arabicNumber.split(",\\s*");
        NumberFormat fmt = NumberFormat.getInstance(Locale.forLanguageTag("ar"));
        StringBuilder sb = new StringBuilder();
        try {

            for (String s : strings) {
                Number n = fmt.parse(s);
                if (sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(n);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return String.valueOf(sb);
    }
}
