package com.music163.log.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ParseLogUtil {

    public static String parseToJsonArray(String key, String data) {
        String newData = null;
        if (data.contains("\\")) {
            newData = data.replaceAll("\\\\", "");
            JSONObject jo = JSON.parseObject(newData);
            JSONObject obj = new JSONObject();

            if (jo != null) {
                //通过json对象和key返回jsonArray

                JSONArray array = jo.getJSONArray(key);
                obj.put(key, array);
            }
            return obj.toJSONString();
        } else {
            JSONObject jo = JSON.parseObject(data);
            JSONObject obj = new JSONObject();

            if (jo != null) {
                //通过json对象和key返回jsonArray
                JSONArray array = jo.getJSONArray(key);
                obj.put(key, array);
            }
            return obj.toJSONString();
        }
    }

    public static String parseJson(String key, String data) {
        String newData = null;
        if (data.contains("\\")) {
            newData = data.replaceAll("\\\\", "");
            JSONObject jo = JSON.parseObject(newData);

            if (jo != null) {
                String val = (String) jo.get(key);
                return val;
            }
        } else {
            JSONObject jo = JSON.parseObject(data);
            JSONObject obj = new JSONObject();

            if (jo != null) {
                try {
                    String val = jo.get(key).toString();
                    return val;
                } catch (Exception e) {
                    return null;
                }


            }
        }
        return null;
    }

    public static List<String> parseJsonToArray(String key, String json) {
        List<String> list = new ArrayList<String>();

        try {
            JSONObject jo = JSON.parseObject(json);
            JSONArray jArray = jo.getJSONArray(key);
            if (jArray != null && jArray.size() > 0) {
                for (int i = 0; i < jArray.size(); i++) {
                    String o = jArray.get(i).toString();
                    list.add(o);
                }
            }
        } catch (Exception e) {

        }

        return list;

    }
}
