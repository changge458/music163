package com.music163.log.util;

import org.apache.commons.collections.bag.SynchronizedSortedBag;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GenTimeUtil {

    public static long genTime(String date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            int i = calendar.get(Calendar.DAY_OF_WEEK);
            Random r = new Random();
            if(i == 7 || i == 1){
                return genWeekendTime(date, r.nextInt(3));
            }
            return genMiddleWeekTime(date,r.nextInt(3));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private static long genWeekendTime(String date, int i) {
        Random r = new Random();

        String hour;
        String minute;

        switch (i){
            case 0:
                hour = intFormat(r.nextInt(24));
                minute = intFormat(r.nextInt(59));
                String newDate = date + " " + hour + ":" + minute ;
                return parseTime(newDate);


            case 1:
                hour = "10";
                minute = intFormat(r.nextInt(59));
                newDate = date + " " + hour + ":" + minute ;
                return parseTime(newDate);



            case 2:
                hour = intFormat(r.nextInt(24));
                minute = intFormat(r.nextInt(59));
                newDate = date + " " + hour + ":" + minute ;
                return parseTime(newDate);

        }
        return 0;

    }



    private static long genMiddleWeekTime(String date, int i) {
        Random r = new Random();

        String hour;
        String minute;

        switch (i){
            case 0:
                hour = intFormat(r.nextInt(24));
                minute = intFormat(r.nextInt(59));
                String newDate = date + " " + hour + ":" + minute ;
                return parseTime(newDate);


            case 1:
                hour = "13";
                minute = intFormat(r.nextInt(59));
                newDate = date + " " + hour + ":" + minute ;
                return parseTime(newDate);



            case 2:
                hour = intFormat(r.nextInt(24));
                minute = intFormat(r.nextInt(59));
                newDate = date + " " + hour + ":" + minute ;
                return parseTime(newDate);

        }
        return 0;
    }

    private static long parseTime(String newDate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d = sdf.parse(newDate);
            return d.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;


    }

    private static String intFormat(int i ){
        DecimalFormat df= new DecimalFormat("00");
        return df.format(i);

    }

    public static void main(String[] args) {
        for (int i = 0; i < 9; i++) {

            System.out.println(genTime("2018-02-29"));
        }
    }



}
