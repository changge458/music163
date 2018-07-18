import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

    public static void main(String[] args) {
        String date = "1531244379.608";
        long ts = Long.parseLong(date.replace(".", ""));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date(ts));
        System.out.println(s);


    }
}
