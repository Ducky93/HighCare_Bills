package Utils;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Created by mohab 2 on 05/09/2017.
 */
public class GeneralCache  {
    private static HashMap<String,ResultSet> instance;

    public static HashMap callCache(){
        if (instance == null){
            return instance = new HashMap<>();
        }
        return instance;
    }
}
