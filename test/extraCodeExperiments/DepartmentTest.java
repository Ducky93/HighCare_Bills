//package extraCodeExperiments;
//
//import DataBaseManageAndReport.DatabaseManager;
//import Forms.LoginPage;
//import org.junit.Test;
//
//import java.sql.SQLException;
//import java.util.Objects;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Created by mohab 2 on 12/03/2017.
// */
//public class DepartmentTest {
//@Test (expected = SQLException.class)
//    public void testDepartmentNamesSetting() throws SQLException {
//    new LoginPage();
//    Department dept = new Department();
////    assertNotNull(dept.getDepartmentNames());
//    String loginUser="النظافة";
//        if (Objects.equals(loginUser, "الحراسة")) {
//            assertEquals("Company's sec name property is set",new Department().getCompanyName(),DatabaseManager.COMPANY_NAME_Security);
//            assertEquals(dept.getDepartmentNames().size(),2);
//            assertEquals(dept.getDepartmentNames().get(0),"الحراسة");
//            assertEquals(dept.getDepartmentNames().get(1),"نقل الأموال");
//        } else if (Objects.equals(loginUser, "النظافة")) {
//            assertEquals("Company's dev name property is set","هاى كير للتنمية والإستثمار العقاري",DatabaseManager.COMPANY_NAME_DEV);
//            assertEquals(dept.getDepartmentNames().size(),1);
//            assertEquals(dept.getDepartmentNames().get(0),"النظافة");
//        }
//    }
//}