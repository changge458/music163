package com.music163.log.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MysqlUtil {

    //得到数据库行号
    public static int getLineNum(String table) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://192.168.23.1:3306/music163?characterEncoding=utf8&useSSL=false";

            Connection conn = DriverManager.getConnection(url, "root", "root");

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("select * from " + table);

            rs.last();

            int i = rs.getRow();


            rs.close();
            st.close();
            conn.close();

            return i;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //通过行号获取某个id的数据
    public static List<String> getRandomMusic(String table) {

        try {
            Random r = new Random();
            int id = r.nextInt(getLineNum(table)) + 1;

            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://192.168.23.1:3306/music163?characterEncoding=utf8&useSSL=false";

            Connection conn = DriverManager.getConnection(url, "root", "root");

            Statement st = conn.createStatement();
            String sql = "select * from " + table + " where id=" + id;

            ResultSet rs = st.executeQuery(sql);
            rs.next();

            String name = rs.getString(2);
            String duration = rs.getString(3);
            String singer = rs.getString(5);
            String type = rs.getString(6);
            String isFree = rs.getString(7);

            List list = new ArrayList();
            list.add(name);
            list.add(duration);
            list.add(singer);
            list.add(type);
            list.add(isFree);

            st.close();
            rs.close();
            conn.close();

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过表获取关键字
     *
     * @param tableName
     * @return
     */
    public static List<String> getKeywords(String tableName) {

        List<String> list = new ArrayList<String>();

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://192.168.23.1:3306/music163?characterEncoding=utf8&useSSL=false";

            Connection conn = DriverManager.getConnection(url, "root", "root");

            Statement st = conn.createStatement();

            String sql = "select * from table_shadow where tablename=" + "'" + tableName + "'";

            ResultSet rs = st.executeQuery(sql);

            ResultSetMetaData rsm = rs.getMetaData();
            int columnCount = rsm.getColumnCount();

            rs.next();

            for (int i = 2; i <= columnCount; i++) {
                String keyword = rs.getString(i);
                if (keyword != null) {
                    list.add(keyword);
                }
            }

            st.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;


    }


}

















