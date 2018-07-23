import org.junit.Test;

import java.sql.*;

public class TestMusicConvert {
    public static void main(String[] args) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/music163";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String pass = "root";

        Connection connection = DriverManager.getConnection(url, user, pass);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select * from music_yueyu");
        int i = 582;
        while(rs.next()){
            String mname = "\"" + rs.getString(2) + "\"";
            String mtime = "\"" + rs.getString(3) + "\"";
            String malbum = "\"" + rs.getString(4) + "\"";
            String msinger = "\"" + rs.getString(5) + "\"";
            String mstyle = "\"" + rs.getString(6) + "\"";
            String misfree ="\"" + rs.getString(7) + "\"";
            i++;

            if(mname != null && misfree != null){
                Statement st2 = connection.createStatement();
                String sql = "insert into music values( "+  i + "," + mname + ","+ mtime + "," + malbum + ","+ msinger + ","+ mstyle + ","+ misfree + " )";
                st2.execute(sql);

            }


        }

    }

    @Test
    public void convert2() throws Exception{
        int id = ConvertName2Id.convert("星の奏でる歌 (星奏歌)");
        System.out.println(id);

    }
}
