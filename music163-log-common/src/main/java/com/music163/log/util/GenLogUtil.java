package com.music163.log.util;

import com.music163.log.common.*;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.music163.log.util.DictionaryUtil.randomValue;
import static com.music163.log.util.DictionaryUtil.randomValue_negative;
import static com.music163.log.util.DictionaryUtil.randomValue_positive;


public class GenLogUtil {

    public static int type;
    public static String deviceID;
    public static String date;


    public GenLogUtil(int type, String deviceID, String date) {
        this.type = type;
        this.deviceID = deviceID;
        this.date = date;

    }

    /**
     * 根据日志类型，返回日志对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T genLog(Class<T> clazz) {
        try {
            T t1 = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class fType = field.getType();
                String fName = field.getName();
                if (fType == String.class) {
                    String value = randomValue(fName.toLowerCase());
                    field.set(t1, value);
                }
            }
            //如果t1是base的实例，则设置创建时间
            if (t1 instanceof AppBaseLog) {
                ((AppBaseLog) t1).setCreatedAtMs(GenTimeUtil.genTime(date));
            }

            //如果t1是event的实例，则设置 \歌曲id\播放时间\播放时长\打分
            //喜欢的歌曲和不喜欢的歌曲通过random函数生成
            if (t1 instanceof AppEventLog) {
                Random r = new Random();
                switch (r.nextInt(2)) {
                    case 0:
                        genPositive((AppEventLog) t1);
                        break;
                    case 1:
                        genNegative((AppEventLog) t1);
                        break;
                }
            }

            //如果是日志聚合体，则设置设备id
            if (t1 instanceof AppLogAggEntity) {
                ((AppLogAggEntity) t1).setDeviceId(deviceID);
            }
            return t1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据日志类型，返回list类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> genLogList(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        Random r = new Random();
        if (clazz.equals(AppStartupLog.class)) {
            list.add(new GenLogUtil(type, deviceID,date).genLog(clazz));
        }
        if (clazz.equals(AppEventLog.class)) {
            for (int i = 0; i <= r.nextInt(10); i++) {
                list.add(new GenLogUtil(type, deviceID,date).genLog(clazz));
            }
        } else {
            for (int i = 0; i <= r.nextInt(3); i++) {
                list.add(new GenLogUtil(type, deviceID,date).genLog(clazz));
            }
        }
        return list;
    }

    public AppLogAggEntity genLogAgg() {
        AppLogAggEntity agg = new GenLogUtil(type, deviceID,date).genLog(AppLogAggEntity.class);
        agg.setAppErrorLogs(genLogList(AppErrorLog.class));
        agg.setAppEventLogs(genLogList(AppEventLog.class));
        agg.setAppStartupLogs(genLogList(AppStartupLog.class));
        agg.setAppUsageLogs(genLogList(AppUsageLog.class));
        agg.setAppPageLogs(genLogList(AppPageLog.class));
        return agg;
    }


    //产生好评的歌曲日志
    private static void genPositive(AppEventLog t1) {
        String table = TypeUtil.parseTable(type);
        String positive = randomValue_positive();

        List<String> list = MysqlUtil.getRandomMusic(table);
        t1.setMusicID(list.get(0));
        if (positive.equals("play") || positive.equals("listen")) {
            t1.setDuration(list.get(1));
            t1.setPlayTime(t1.getCreatedAtMs() + "");
        }


        t1.setEventId(positive);
        t1.setMark(parseMark(positive));
    }

    //产生坏评的歌曲日志
    private static void genNegative(AppEventLog t1) {
        Random r = new Random();
        String table = TypeUtil.parseTable(r.nextInt(9) + 1);
        String negative = randomValue_negative();
        List<String> list = MysqlUtil.getRandomMusic(table);
        t1.setMusicID(list.get(0));
        if (negative.equals("skip")) {
            t1.setDuration("00:20");
            t1.setPlayTime(t1.getCreatedAtMs() + "");
        }


        t1.setEventId(negative);
        t1.setMark(parseMark(negative));
    }

    //将事件解析成打分
    private static String parseMark(String event) {
        if (event.equals("share")) {
            return "4";
        }
        if (event.equals("favourite")) {
            return "3";
        }
        if (event.equals("play")) {
            return "2";
        }
        if (event.equals("listen")) {
            return "1";
        }
        if (event.equals("skip")) {
            return "-1";
        }
        if (event.equals("cancel")) {
            return "-3";
        }
        if (event.equals("black")) {
            return "-5";
        }
        return "0";
    }

}
