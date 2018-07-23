package com.music163;

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
public class MyHDFSSink extends AbstractSink implements Configurable {

    private String fullPath;
    private List<String> list = new ArrayList<String>();

    String suffix = "access.log1";

    private static final String defaultPath = "/1.txt";
    private String Path = "path";

    private static final String defaultUser = "centos";
    private String user = "user";

    private static final long defaultFileSize = 134217728;
    private String fileSize = "fileSize";

    private static final int defaultBatch = 1;
    private String batch = "batch";


    private String p;
    private String u;
    private long s;
    private int b;

    int i = 0;


    public void configure(Context context) {

        p = context.getString(Path, defaultPath);
        u = context.getString(user,defaultUser);
        s = context.getLong(fileSize,defaultFileSize);
        b = context.getInteger(batch,defaultBatch);

    }

    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event = null;

        try {
            transaction.begin();
            event = channel.take();
            String line = new String(event.getBody());
            String time = line.split("#")[0].replace(".","");
            String year = parseYear(time);
            String date = parseDate(time);


            if (event != null) {
                System.setProperty("HADOOP_USER_NAME",u);
                Configuration conf = new Configuration();
                FileSystem fs = FileSystem.get(conf);

                // xxx/year=2017/date=07-08/access.log1

                String dirPath = p + "/" + year + "/" + date + "/";


                Path p1 = new Path(dirPath);

                if(fs.exists(p1)){
                    int bigSuffix = 1;
                    FileStatus[] fss = fs.listStatus(p1);
                    if(fss.length > 0){
                        FileStatus status = fss[fss.length - 1];
                        int thisSuffix = Integer.parseInt(status.getPath().getName().split("access.log")[1]);
                        if( thisSuffix > bigSuffix){
                            bigSuffix = thisSuffix;
                        }
                    }
                    suffix = "access.log" + bigSuffix;
                }

                fullPath = p + "/" + year + "/" + date + "/" + suffix ;

                Path p2= new Path(fullPath);

                for ( ; i < b ; i++) {

                }

                //如果不存在则创建
                if(!fs.exists(p2)) {
                    FSDataOutputStream fos = fs.create(p2);
                    fos.write(event.getBody());
                    fos.write("\n".getBytes());
                    fos.close();
                }
                //如果大小超过了则新建文件名
                if (fs.getFileStatus(p2).getLen() > s){
                    suffix = renamePath(suffix);
                    fullPath = p + "/" + year + "/" + date + "/" + suffix;
                    p2 = new Path(fullPath);
                    FSDataOutputStream fos = fs.create(p2);
                    fos.write(event.getBody());
                    fos.write("\n".getBytes());
                    fos.close();
                }

                //如果存在且大小没超过则追加
                else {
                    FSDataOutputStream fos = fs.append(p2);
                    fos.write(event.getBody());
                    fos.write("\n".getBytes());
                    fos.close();

                }

                System.out.println("成功发送了一个事件");

            } else {
                // No event found, request back-off semantics from the sink runner
                result = Status.BACKOFF;
            }
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
            System.out.println("暂时没有事件待发送");
        } finally {
            transaction.close();
        }
        return result;
    }

    private String renamePath(String suffix) {
        String[] arr = suffix.split("access.log");
        int i = Integer.parseInt(arr[1]);
        i = i + 1;
        String newPath = arr[0]+ "access.log" + i;

        return newPath;

    }


    public String parseYear(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String s = sdf.format(new Date(Long.parseLong(time)));
        return s;
    }

    public String parseDate(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String s = sdf.format(new Date(Long.parseLong(time)));
        return s;
    }






}
