//package extraCodeExperiments;
//
//import DataBaseManageAndReport.DatabaseManager;
//import Forms.LoginPage;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
///**
// * Created by mohab 2 on 11/03/2017.
// */
//public class Department {
//    private String[] departmentNames;
//    private String companyName;
//
//    public String[] getDepartmentNames() {
//        return departmentNames;
//    }
//    public String getCompanyName() {
//        return companyName;
//    }
//
//    public Department(){
//        /* ToDo * Make Department names Dynamicly added instead of using manually written values
//           TODO create Enum for department names
//        */
//        List<String> deptNameList= new ArrayList<>();
//        if (Objects.equals(LoginPage.deptNameForUser,"الحراسة")) {
//            this.companyName = DatabaseManager.COMPANY_NAME_Security;
//            deptNameList.add("الحراسة");
//            deptNameList.add("نقل الأموال");
//        } else if (Objects.equals(LoginPage.deptNameForUser, "النظافة")) {
//            this.companyName = DatabaseManager.COMPANY_NAME_DEV;
//            deptNameList.add("النظافة");
//        }
//        departmentNames = new String[deptNameList.size()];
//        for (int i = 0; i < deptNameList.size(); i++) {
//            departmentNames[i]=deptNameList.get(i);
//        }
//    }
//
//}
