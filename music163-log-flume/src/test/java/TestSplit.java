import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSplit {
    public static void main(String[] args) {
        String line = "1530875198.859#192.168.23.1#1531105962662#200#{\\\"appChannel\\\":\\\"appstore\\\",\\\"appErrorLogs\\\":[{\\\"createdAtMs\\\":1531105962497,\\\"errorBrief\\\":\\\"at cn.lift.appIn.control.CommandUtil.getInfo(CommandUtil.java:67)\\\",\\\"errorDetail\\\":\\\"at cn.lift.dfdfdf.control.CommandUtil.getInfo(CommandUtil.java:67) at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) at java.lang.reflect.Method.invoke(Method.java:606)\\\"},{\\\"createdAtMs\\\":1531105962497,\\\"errorBrief\\\":\\\"at cn.lift.dfdf.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)\\\",\\\"errorDetail\\\":\\\"java.lang.NullPointerException at cn.lift.appIn.web.AbstractBaseController.validInbound(AbstractBaseController.java:72) at cn.lift.dfdf.web.AbstractBaseController.validInbound\\\"}],\\\"appEventLogs\\\":[{\\\"createdAtMs\\\":1531105962497,\\\"duration\\\":\\\"04:07\\\",\\\"eventId\\\":\\\"skip\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"-1\\\",\\\"musicID\\\":\\\"连续剧\\\",\\\"playTime\\\":\\\"1531105962537\\\",\\\"singer\\\":\\\"Dear Jane\\\",\\\"type\\\":\\\"粤语\\\"},{\\\"createdAtMs\\\":1531105962537,\\\"duration\\\":\\\"03:44\\\",\\\"eventId\\\":\\\"share\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"4\\\",\\\"musicID\\\":\\\"偷走\\\",\\\"playTime\\\":\\\"1531105962574\\\",\\\"singer\\\":\\\"房东的猫\\\",\\\"type\\\":\\\"二次元\\\"},{\\\"createdAtMs\\\":1531105962574,\\\"duration\\\":\\\"04:31\\\",\\\"eventId\\\":\\\"skip\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"-1\\\",\\\"musicID\\\":\\\"Moon River\\\",\\\"playTime\\\":\\\"1531105962622\\\",\\\"singer\\\":\\\"The Piano Guys\\\",\\\"type\\\":\\\"轻音乐\\\"},{\\\"createdAtMs\\\":1531105962622,\\\"duration\\\":\\\"04:44\\\",\\\"eventId\\\":\\\"share\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"4\\\",\\\"musicID\\\":\\\"一番の宝物 (最珍贵的宝物) (Yui final ver)\\\",\\\"playTime\\\":\\\"1531105962661\\\",\\\"singer\\\":\\\"菅野よう子 (菅野洋子)\\\",\\\"type\\\":\\\"二次元|原声\\\"}],\\\"appPageLogs\\\":[{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"test.html\\\",\\\"pageId\\\":\\\"test.html\\\",\\\"pageViewCntInSession\\\":0,\\\"stayDurationSecs\\\":\\\"234\\\",\\\"visitIndex\\\":\\\"2\\\"},{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"test.html\\\",\\\"pageId\\\":\\\"list.html\\\",\\\"pageViewCntInSession\\\":0,\\\"stayDurationSecs\\\":\\\"2\\\",\\\"visitIndex\\\":\\\"0\\\"}],\\\"appPlatform\\\":\\\"ios\\\",\\\"appStartupLogs\\\":[{\\\"brand\\\":\\\"Apple\\\",\\\"carrier\\\":\\\"中国电信\\\",\\\"country\\\":\\\"china\\\",\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"startup\\\",\\\"network\\\":\\\"wifi\\\",\\\"province\\\":\\\"henan\\\",\\\"screenSize\\\":\\\"960 * 640\\\"}],\\\"appUsageLogs\\\":[{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"usage\\\"},{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"usage\\\"}],\\\"appVersion\\\":\\\"1.2.0\\\",\\\"deviceId\\\":\\\"Device000005\\\",\\\"deviceStyle\\\":\\\"iphone 7\\\",\\\"osType\\\":\\\"8.3\\\"}\n";
        String time = line.split("#")[0].replace(".","");
        System.out.println(time);
        
    }

    @Test
    public void testParseYear(){
        String ts = "1530875198859";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date(Long.parseLong(ts)));
        System.out.println(s);
    }

    @Test
    public void testSplit2(){
        String fullPath = "access.log122";
        String[] arr = fullPath.split("access.log");
        int i = Integer.parseInt(arr[1]);
        System.out.println(i);
    }
}
