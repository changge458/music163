import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 自定义Sink实现Flume插入数据到分区表
 */
public class TestHDFSSink {

    private String fullPath;
    private List<String> list = new ArrayList<String>();

    String suffix;

    private static final String defaultPath = "/yyy";
    private String Path = "hiveTablePath";

    private static final String defaultUser = "centos";
    private String user = "user";

    private static final long defaultFileSize = 1048576;
    private String fileSize = "fileSize";

    int i;

    public void process() throws Exception {
        String line = "1530875198.859#192.168.23.1#1531105962662#200#{\\\"appChannel\\\":\\\"appstore\\\",\\\"appErrorLogs\\\":[{\\\"createdAtMs\\\":1531105962497,\\\"errorBrief\\\":\\\"at cn.lift.appIn.control.CommandUtil.getInfo(CommandUtil.java:67)\\\",\\\"errorDetail\\\":\\\"at cn.lift.dfdfdf.control.CommandUtil.getInfo(CommandUtil.java:67) at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) at java.lang.reflect.Method.invoke(Method.java:606)\\\"},{\\\"createdAtMs\\\":1531105962497,\\\"errorBrief\\\":\\\"at cn.lift.dfdf.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)\\\",\\\"errorDetail\\\":\\\"java.lang.NullPointerException at cn.lift.appIn.web.AbstractBaseController.validInbound(AbstractBaseController.java:72) at cn.lift.dfdf.web.AbstractBaseController.validInbound\\\"}],\\\"appEventLogs\\\":[{\\\"createdAtMs\\\":1531105962497,\\\"duration\\\":\\\"04:07\\\",\\\"eventId\\\":\\\"skip\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"-1\\\",\\\"musicID\\\":\\\"连续剧\\\",\\\"playTime\\\":\\\"1531105962537\\\",\\\"singer\\\":\\\"Dear Jane\\\",\\\"type\\\":\\\"粤语\\\"},{\\\"createdAtMs\\\":1531105962537,\\\"duration\\\":\\\"03:44\\\",\\\"eventId\\\":\\\"share\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"4\\\",\\\"musicID\\\":\\\"偷走\\\",\\\"playTime\\\":\\\"1531105962574\\\",\\\"singer\\\":\\\"房东的猫\\\",\\\"type\\\":\\\"二次元\\\"},{\\\"createdAtMs\\\":1531105962574,\\\"duration\\\":\\\"04:31\\\",\\\"eventId\\\":\\\"skip\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"-1\\\",\\\"musicID\\\":\\\"Moon River\\\",\\\"playTime\\\":\\\"1531105962622\\\",\\\"singer\\\":\\\"The Piano Guys\\\",\\\"type\\\":\\\"轻音乐\\\"},{\\\"createdAtMs\\\":1531105962622,\\\"duration\\\":\\\"04:44\\\",\\\"eventId\\\":\\\"share\\\",\\\"logType\\\":\\\"event\\\",\\\"mark\\\":\\\"4\\\",\\\"musicID\\\":\\\"一番の宝物 (最珍贵的宝物) (Yui final ver)\\\",\\\"playTime\\\":\\\"1531105962661\\\",\\\"singer\\\":\\\"菅野よう子 (菅野洋子)\\\",\\\"type\\\":\\\"二次元|原声\\\"}],\\\"appPageLogs\\\":[{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"test.html\\\",\\\"pageId\\\":\\\"test.html\\\",\\\"pageViewCntInSession\\\":0,\\\"stayDurationSecs\\\":\\\"234\\\",\\\"visitIndex\\\":\\\"2\\\"},{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"page\\\",\\\"nextPage\\\":\\\"test.html\\\",\\\"pageId\\\":\\\"list.html\\\",\\\"pageViewCntInSession\\\":0,\\\"stayDurationSecs\\\":\\\"2\\\",\\\"visitIndex\\\":\\\"0\\\"}],\\\"appPlatform\\\":\\\"ios\\\",\\\"appStartupLogs\\\":[{\\\"brand\\\":\\\"Apple\\\",\\\"carrier\\\":\\\"中国电信\\\",\\\"country\\\":\\\"china\\\",\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"startup\\\",\\\"network\\\":\\\"wifi\\\",\\\"province\\\":\\\"henan\\\",\\\"screenSize\\\":\\\"960 * 640\\\"}],\\\"appUsageLogs\\\":[{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"usage\\\"},{\\\"createdAtMs\\\":1531105962661,\\\"logType\\\":\\\"usage\\\"}],\\\"appVersion\\\":\\\"1.2.0\\\",\\\"deviceId\\\":\\\"Device000005\\\",\\\"deviceStyle\\\":\\\"iphone 7\\\",\\\"osType\\\":\\\"8.3\\\"}\n";

        String time = line.split("#")[0].replace(".", "");
        String year = parseYear(time);
        String date = parseDate(time);


        System.setProperty("HADOOP_USER_NAME", defaultUser);
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://s101");
        FileSystem fs = FileSystem.get(conf);

        String dirPath = defaultPath + "/" + year + "/" + date + "/";


        Path p1 = new Path(dirPath);

        if (fs.exists(p1)) {
            int bigSuffix = 1;
            FileStatus[] fss = fs.listStatus(p1);
            if (fss.length > 0) {
                FileStatus status = fss[fss.length - 1];
                int thisSuffix = Integer.parseInt(status.getPath().getName().split("access.log")[1]);
                if (thisSuffix > bigSuffix) {
                    bigSuffix = thisSuffix;
                }
            }
            suffix = "access.log" + bigSuffix;
        }

        fullPath = defaultPath + "/" + year + "/" + date + "/" + suffix;

        Path p2 = new Path(fullPath);

        for ( i = 0; i < 4; i++) {
            list.add(line);
            System.out.println(i);

        }

        //如果不存在则创建
        if (!fs.exists(p2)) {
            FSDataOutputStream fos = fs.create(p2);
            fos.write(line.getBytes());
            fos.write("\n".getBytes());
            fos.close();
        }
        //如果大小超过了则新建文件名
        if (fs.getFileStatus(p2).getLen() > defaultFileSize) {
            suffix = renamePath(suffix);
            fullPath = defaultPath + "/" + year + "/" + date + "/" + suffix;
            p2 = new Path(fullPath);
            FSDataOutputStream fos = fs.create(p2);
            fos.write(line.getBytes());
            fos.write("\n".getBytes());
            fos.close();
        }

        //如果存在且大小没超过则追加
        else {
            FSDataOutputStream fos = fs.append(p2);
            fos.write(line.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        }

        System.out.println("成功发送了一个事件");


    }

    private String renamePath(String suffix) {
        String[] arr = suffix.split("access.log");
        int i = Integer.parseInt(arr[1]);
        i = i + 1;
        String newPath = arr[0] + "access.log" + i;

        return newPath;

    }


    public String parseYear(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String s = sdf.format(new Date(Long.parseLong(time)));
        return s;
    }

    public String parseDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String s = sdf.format(new Date(Long.parseLong(time)));
        return s;
    }


    public static void main(String[] args) throws Exception {

        for (; ; ) {
            new TestHDFSSink().process();
        }
    }

}
