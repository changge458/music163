package com.music163.logudf;

import com.music163.log.util.DateUtil;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 计算某天的零时刻对应的毫秒数
 */
public class ParseWeekUDF extends UDF {
    //
    public int evaluate(String ts) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(Long.parseLong(ts)));

            //得到该时间位于一周中的第几天
            int which = c.get(Calendar.DAY_OF_WEEK);
            if(which == 1 || which == 7){
                return 1;
            }
            else {
                return 2;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }
}
