package DataBaseManageAndReport;

/**
 * Created by mohab 2 on 21/12/2016.
 */
public class HandingBillsReport {
    public Object getLocation_name() {
        return location_name;
    }

    public Object getBill_number() {
        return bill_number;
    }

    public Object getDateOfPrinting() {
        return dateOfPrinting;
    }

    Object location_name;
    Object bill_number;
    Object dateOfPrinting;

    public HandingBillsReport(Object location_name, Object bill_number, Object dateOfPrinting) {
        this.location_name = location_name;
        this.bill_number = bill_number;
        this.dateOfPrinting = dateOfPrinting;
    }
}
