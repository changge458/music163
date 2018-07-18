import com.alibaba.fastjson.JSON;
import com.music163.log.common.AppLogAggEntity;
import com.music163.log.util.GenLogUtil;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;


public class DataSender {

    public static void main(String[] args) throws Exception {
        genUser(100, "2018-07-16", 1000);

    }

    //产生数据

    public static void genData(String deviceID, int type, String date, int num) {
        for (int i = 0; i < num; i++) {
            AppLogAggEntity aggLog = new GenLogUtil(type, deviceID, date).genLogAgg();
            String json = JSON.toJSONString(aggLog, false);
            //doSend(json);
            System.out.println(json);
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行发送
     *
     * @param json
     */
    private static void doSend(String json) {
        try {
            //String strURL = "http://s202:8080/app-colllog/collector/agg";
            String strURL = "http://192.168.23.101:8089/index.html";
            URL url = new URL(strURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("POST");
            //允许输出到服务器
            conn.setDoOutput(true);
            //设置上传数据的内容类型
            conn.setRequestProperty("content-Type", "application/json");
            conn.setRequestProperty("client_time", System.currentTimeMillis() + "");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
            System.out.println(conn.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //自动生成多个用户及其喜欢类型
    public static void genUser(int userNum, final String date, final int logNum) {

        Random r = new Random();

        //产生
        for (int i = 0; i < userNum; i++) {

            DecimalFormat df = new DecimalFormat("000000");
            final String deviceID = "Device" + df.format(i);

            final int type = r.nextInt(9) + 1;

            Thread t1 = new Thread() {
                @Override
                public void run() {
                    genData(deviceID, type, date, logNum);
                }
            };
            t1.start();
        }
    }
}
