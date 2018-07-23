package com.music163.logudf;

import static com.music163.log.util.LogUtil.oiCopy;

import com.music163.log.util.GeoliteUtil;
import com.music163.log.util.MysqlUtil;
import com.music163.log.util.ParseLogUtil;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.JavaStringObjectInspector;

import java.util.ArrayList;
import java.util.List;


/**
 * 日志聚合体解析表生成函数
 */
@Description(name = "parsePage",
        value = "select parsePage(line) ===> server_time,remote_ip,country,province,client_time,deviceId,appChannel,appVersion,deviceStyle,osType,appPlatform,logType,createdAtMs,nextPage,pageId,pageViewCntInSession,visitIndex\t")

public class ParsePageUDTF extends GenericUDTF {

    private PrimitiveObjectInspector line_inputOI = null;
    private List<String> fields = MysqlUtil.getKeywords("appBaseLog");
    private List<String> fields2 = MysqlUtil.getKeywords("appPageLogs");
    private List<JavaStringObjectInspector> ois = oiCopy("appBaseLog");
    private List<JavaStringObjectInspector> ois2 = oiCopy("appPageLogs");


    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {


        //检查参数个数
        if (argOIs.getAllStructFieldRefs().size() == 1) {
            //取得日志的ObjectInspector对象
            ObjectInspector line_oi = argOIs.getAllStructFieldRefs().get(0).getFieldObjectInspector();


            //检查字段类型
            line_inputOI = (PrimitiveObjectInspector) line_oi;

            if (line_inputOI.getPrimitiveCategory() != PrimitiveObjectInspector.PrimitiveCategory.STRING) {
                throw new UDFArgumentException("参数1必须是string类型！");
            }


            //添加字段名称
            List<String> fieldNames = new ArrayList<String>();
            fieldNames.addAll(fields);
            fieldNames.addAll(fields2);

            //添加字段类型
            List<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
            fieldOIs.addAll(ois);
            fieldOIs.addAll(ois2);

            return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);


        } else {
            throw new UDFArgumentException("参数个数必须为1 ！");
        }
    }

    /**
     * 一行调用一次此方法，使用forward来输出
     */
    @Override
    public void process(Object[] record) throws HiveException {
        /*
         * 将第一个参数line转换成String类型
         */
        final String line = (String) line_inputOI.getPrimitiveJavaObject(record[0]);

        //获取log数组
        String[] arr = line.split("#");
        String pageJson =  ParseLogUtil.parseToJsonArray("appPageLogs",arr[4]);

        //获取整个日志数组
        List<String> list = ParseLogUtil.parseJsonToArray("appPageLogs", pageJson);

        //14
        int baseSize = fields.size();
        int size = baseSize + fields2.size();

        for (String json : list) {
            //server_time	remote_ip	country		province	client_time  	deviceId	appChannel	appVersion	deviceStyle	osType	appPlatform		logType	createdAtMs	nextPage	pageId	pageViewCntInSession	visitIndex
            //0		        1		    2		    3		    4		        5		    6		    7		    8		    9		10		        11		12	        13          14      15                      16

            Object[] obj = new Object[size];
            //server_time
            obj[0] = arr[0];
            //remote_ip
            obj[1] = arr[1];
            //country
            obj[2] = GeoliteUtil.getCountry(arr[1]);
            //province
            obj[3] = GeoliteUtil.getProvince(arr[1]);
            //client_time
            obj[4] = arr[2];

            //
            //obj[5] ---> obj[10] : LogAgg
            for (int i = 5; i < baseSize; i++) {
                obj[i] = ParseLogUtil.parseJson(fields.get(i), arr[4]);
            }

            //obj[11] ---> obj[13] : LogError
            for (int j = baseSize; j < size; j++) {
                obj[j] = ParseLogUtil.parseJson(fields2.get((j-baseSize)), json);
            }
            forward(obj);
        }
    }

    @Override
    public void close() throws HiveException {

    }
}