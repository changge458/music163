import com.music163.log.util.GeoliteUtil;
import com.music163.log.util.MysqlUtil;
import com.music163.log.util.ParseLogUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class TestParseList {
    public static void main(String[] args) throws Exception {
        List<String> fields = MysqlUtil.getKeywords("appBaseLog");
        List<String> fields2 = MysqlUtil.getKeywords("appEventLogs");
        BufferedReader br = new BufferedReader(new FileReader("D:/access.log1"));

        String line = null;

        while ((line = br.readLine()) != null) {

            //获取log数组
            String[] arr = line.split("#");
            String eventJson = ParseLogUtil.parseToJsonArray("appEventLogs", arr[4]);

            //获取整个日志数组
            List<String> list = ParseLogUtil.parseJsonToArray("appEventLogs", eventJson);

            //14
            int baseSize = 11;
            int size = 20;

            for (String json : list) {
                //server_time	remote_ip	country		province	client_time  	deviceId	appChannel	appVersion	deviceStyle	osType	appPlatform		musicID createdAtMs eventId	logType	mark	playTime	duration	singer	type
                //0		        1		    2		    3		    4		        5		    6		    7		    8		    9		10		        11		    12	        13  14      15      16          17          18      19

                Object[] obj = new Object[size];
                //server_time
                obj[0] = arr[0];
                //remote_ip
                obj[1] = arr[1];
                //country
                obj[2] = GeoliteUtil.getCountry(arr[1]);
                //province
                obj[3] = GeoliteUtil.getProvince(arr[1]);
                //client_time
                obj[4] = arr[2];

                //
                //obj[5] ---> obj[10] : LogAgg
                for (int i = 5; i < baseSize; i++) {
                    obj[i] = ParseLogUtil.parseJson(fields.get(i), arr[4]);
                }

                //obj[11] ---> obj[13] : LogError
                for (int j = baseSize; j < size; j++) {
                    obj[j] = ParseLogUtil.parseJson(fields2.get((j - baseSize)), json);
                }

                for (int i = 0; i < size; i++) {
                    System.out.print(obj[i] + ",");

                }
                System.out.println();

            }
        }
    }


    @Test
    public void xxx() throws Exception {
        String line = null;
        File f = new File("C:/Users/chang/Desktop/1.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        while ((line = br.readLine()) != null) {
            String[] arr = line.split("#");
            String eventJson = ParseLogUtil.parseToJsonArray("appEventLogs", arr[4]);

            try {
                List<String> list = ParseLogUtil.parseJsonToArray("appEventLogs", eventJson);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //获取整个日志数组

        }
    }
}
