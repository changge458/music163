package com.music163.log.util;

/**
 * 将类型解析为数据库名称
 */
public class TypeUtil {

    public static String parseTable(int type) {

        switch (type) {
            case 1:
                return "music_mix";
            case 2:
                return "music_folk";
            case 3:
                return "music_custom";
            case 4:
                return "music_old";
            case 5:
                return "music_rock1";
            case 6:
                return "music_rock2";
            case 7:
                return "music_comic";
            case 8:
                return "music_yueyu";
            case 9:
                return "music_light";
            default:
                try {
                    throw new Exception("参数必须为1-9");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
