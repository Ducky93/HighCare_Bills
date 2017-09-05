package DataBaseManageAndReport;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by mohab 2 on 20/12/2016.
 */
public class SalesReport {

    Object location_name;
    Object bill_value;
    Object extra_value;
    Object total_value;
    Object bill_number;
    Object counter;



    Object dateOfPrinting;
    Object totalBillValue;
    Object totalExtraValue;
    Object generalTotal;

    public Object getTotalBillValue() {
        return totalBillValue;
    }

    public Object getTotalExtraValue() {
        return totalExtraValue;
    }

    public Object getGeneralTotal() {
        return generalTotal;
    }

    public Object getLocation_name() {
        return location_name;
    }
    public Object getDateOfPrinting() {
        return dateOfPrinting;
    }
    public Object getBill_value() {
        return bill_value;
    }

    public Object getExtra_value() {
        return extra_value;
    }

    public Object getTotal_value() {
        return total_value;
    }

    public Object getBill_number() {
        return bill_number;
    }

    public Object getCounter() {
        return counter;
    }

    static int countIt;
    static double sumBilLValue;
    static double sumExtraValue;
    static double sumTotal;
    Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();
    NumberFormat numberFormat = NumberFormat.getNumberInstance(arabicLocale);
    public SalesReport(Object location_name, Object bill_value, Object extra_value, Object total_value, Object bill_number, Object dateOfPrinting) {
        this.location_name = location_name;
        this.bill_value = bill_value;
        this.extra_value = extra_value;
        this.total_value = total_value;
        this.bill_number = bill_number;
        this.dateOfPrinting = dateOfPrinting;
        this.counter = numberFormat.format(countIt);

        countIt++;
    }
}
