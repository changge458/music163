import com.music163.log.util.MysqlUtil;

import java.util.List;

public class TestMysql {
    public static void main(String[] args) {
        List<String> appBaseLog = MysqlUtil.getKeywords("appBaseLog");
        for(String key: appBaseLog){
            System.out.println(key);
        }
    }
}
