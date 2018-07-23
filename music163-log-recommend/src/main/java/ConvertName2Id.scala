object ConvertName2Id {

    import java.sql.DriverManager;

    def convert(name: String): Int = {
        // Change to Your Database Config
        val url = "jdbc:mysql://localhost:3306/music163?characterEncoding=utf8&useSSL=false";
        // Load the driver
        classOf[com.mysql.jdbc.Driver]
        // Setup the connection
        val conn = DriverManager.getConnection(url, "root", "root")
        try {
            // Configure to be Read Only
            val statement = conn.createStatement()
            // Execute Query
            val sql = "select id from music where mname=\"" + name + "\"";

            val rs = statement.executeQuery(sql);
            // Iterate Over ResultSet
            rs.next;
            return rs.getInt(1);

        } catch {
            case e: Exception => return 0;
        }
        finally {
            conn.close
        }
    }


}
