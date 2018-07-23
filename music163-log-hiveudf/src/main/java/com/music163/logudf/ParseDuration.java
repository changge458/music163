package com.music163.logudf;


import org.apache.hadoop.hive.ql.exec.UDF;

import java.math.BigDecimal;

public class ParseDuration extends UDF {

    public Float evaluate(String s) {

        try {
            String[] arr = s.split(":");
            int min = Integer.parseInt(arr[0]);
            int sec = Integer.parseInt(arr[1]);

            float total = (float) min + (float) sec / 60;
            BigDecimal b = new BigDecimal(total);
            float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

            return f1;


        } catch (Exception e) {

        }
        return Float.valueOf(0);
    }
}
