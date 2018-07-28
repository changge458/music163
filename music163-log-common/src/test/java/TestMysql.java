import com.music163.log.util.MysqlUtil;
import org.junit.Test;

import java.sql.*;
import java.util.List;

public class TestMysql {


    public static  void testMysql2(String table){

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://192.168.23.1:3306/music163?characterEncoding=utf8&useSSL=false";

            Connection conn = DriverManager.getConnection(url, "root", "root");

            Statement st = conn.createStatement();

            String sql = "select id, mstyle from " + table;

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                int i = rs.getInt(1);
                String s = rs.getString(2);
                if(s != null){
                    String language = s.split("\\|")[0];
                    String style = s.replaceAll(language, "");
                    String style2 = style.substring(1,style.length());
                    String sql2 = "update " + table + " set mlanguage=" + "\'" + language + "\'" + " where id=" + i  ;
                    String sql3 = "update " + table + " set mstyle=" + "\'" + style2 + "\'" + " where id=" + i ;

                    Statement st2 = conn.createStatement();
                    st2.execute(sql2);
                    st2.execute(sql3);

                    st2.close();

                }



            }

            st.close();
            rs.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        testMysql2("music_light");
        testMysql2("music_mix");
    }
}
