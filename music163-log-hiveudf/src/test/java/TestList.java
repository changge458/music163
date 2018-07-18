import com.music163.log.util.MysqlUtil;
import com.music163.log.util.ParseLogUtil;

import java.util.ArrayList;
import java.util.List;

public class TestList {
    public static void main(String[] args) {

        String line = "1531324836.857#192.168.23.1#1531376780828#200#{\\\"appChannel\\\":\\\"appstore\\\",\\\"appErrorLogs\\\":[{\\\"createdAtMs\\\":1531376778323,\\\"errorBrief\\\":\\\"at cn.lift.appIn.control.CommandUtil.getInfo(CommandUtil.java:67)\\\",\\\"errorDetail\\\":\\\"at cn.lift.dfdfdf.control.CommandUtil.getInfo(CommandUtil.java:67) at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) at java.lang.reflect.Method.invoke(Method.java:606)\\\"},{\\\"createdAtMs\\\":1531376778324,\\\"errorBrief\\\":\\\"at cn.lift.dfdf.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)\\\",\\\"errorDetail\\\":\\\"java.lang.NullPointerException at cn.lift.appIn.web.AbstractBaseController.validInbound(AbstractBaseController.java:72) at cn.lift.dfdf.web.AbstractBaseController.validInbound\\\"},{\\\"createdAtMs\\\":1531376778324,\\\"errorBrief\\\":\\\"at cn.lift.dfdf.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)\\\",\\\"errorDetail\\\":\\\"at cn.lift.dfdfdf.control.CommandUtil.getInfo(CommandUtil.java:67) at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) at java.lang.reflect.Method.invoke(Method.java:606)\\\"}],\\\"appEventLogs\\\":[{\\\"createdAtMs\\\":1531376778324,\\\"duration\\\":\\\"05:10\\\",\\\"eventId\\\":\\\"share\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"4\\\",\\\"playTime\\\":\\\"1531376780325\\\",\\\"singer\\\":\\\"银临\\\",\\\"type\\\":\\\"古风\\\"},{\\\"createdAtMs\\\":1531376780325,\\\"duration\\\":\\\"03:58\\\",\\\"eventId\\\":\\\"black\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"-5\\\",\\\"musicID\\\":\\\"司宵子\\\",\\\"playTime\\\":\\\"1531376780385\\\",\\\"singer\\\":\\\"少司命\\\",\\\"type\\\":\\\"古风\\\"},{\\\"createdAtMs\\\":1531376780385,\\\"duration\\\":\\\"03:29\\\",\\\"eventId\\\":\\\"black\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"-5\\\",\\\"playTime\\\":\\\"1531376780437\\\",\\\"singer\\\":\\\"Lordi\\\",\\\"type\\\":\\\"欧美流行|摇滚\\\"}],\\\"appPageLogs\\\":[{\\\"createdAtMs\\\":1531376780464,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"test.html\\\",\\\"pageId\\\":\\\"main.html\\\",\\\"pageViewCntInSession\\\":0,\\\"visitIndex\\\":\\\"1\\\"},{\\\"createdAtMs\\\":1531376780464,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"list.html\\\",\\\"pageId\\\":\\\"test.html\\\",\\\"pageViewCntInSession\\\":0,\\\"visitIndex\\\":\\\"0\\\"},{\\\"createdAtMs\\\":1531376780464,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"list.html\\\",\\\"pageId\\\":\\\"test.html\\\",\\\"pageViewCntInSession\\\":0,\\\"visitIndex\\\":\\\"4\\\"}],\\\"appPlatform\\\":\\\"android\\\",\\\"appStartupLogs\\\":[{\\\"brand\\\":\\\"Apple\\\",\\\"carrier\\\":\\\"中国铁通\\\",\\\"country\\\":\\\"china\\\",\\\"createdAtMs\\\":1531376780437,\\\"logType\\\":\\\"startup\\\",\\\"network\\\":\\\"3g\\\",\\\"province\\\":\\\"jiazhou\\\",\\\"screenSize\\\":\\\"960 * 640\\\"}],\\\"appUsageLogs\\\":[{\\\"createdAtMs\\\":1531376780452,\\\"logType\\\":\\\"usage\\\",\\\"singleDownloadTraffic\\\":\\\"3300\\\",\\\"singleUploadTraffic\\\":\\\"128\\\",\\\"singleUseDurationSecs\\\":\\\"45\\\"}],\\\"appVersion\\\":\\\"2.0.0\\\",\\\"deviceId\\\":\\\"Device000009\\\",\\\"deviceStyle\\\":\\\"iphone 6\\\",\\\"osType\\\":\\\"7.1.1\\\"}";
        String[] arr = line.split("#");
        String errorJson = ParseLogUtil.parseToJsonArray("appErrorLogs", arr[4]);
        List<String> list = ParseLogUtil.parseJsonToArray("appErrorLogs", errorJson);


        List<String> fields2 = MysqlUtil.getKeywords("appErrorLogs");

//        for (String json : list) {
//            for (int j = 0; j < fields2.size(); j++) {
//                System.out.print(ParseLogUtil.parseJson(fields2.get((j)), json));
//            }
//            System.out.println("第一个完事了");
//        }

        System.out.println(list.toString());

    }
}
