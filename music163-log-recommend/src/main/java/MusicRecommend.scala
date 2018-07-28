import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.{Row, SparkSession}

/**
  *
  */
object MusicRecommend {

    case class dataInfo(uid: Int, name: Int, grade: Float)

    System.setProperty("HADOOP_USER_NAME", "centos")

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .appName("MusicRecommend")
            .master("local[32]")
            .config("spark.sql.warehouse.dir", "hdfs://mycluster/user/hive/warehouse")
            .enableHiveSupport()
            .getOrCreate()
        import spark.implicits._
        val ratings = spark.read.textFile("file:///C:\\Users\\chang\\OneDrive\\桌面\\xxx/*")
            .filter(f => {
                val res = f.split("\t").length == 3
                res
            })
            .map(f => {
                val data = f.split("\t")
                dataInfo(data(0).split("Device")(1).toInt, ConvertName2Id.convert(data(1)), data(2).toFloat)
            }).toDF()
        val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))


        // 创建ALS的训练数据模型

        val als = new ALS()
            .setUserCol("uid") // 必须设置，默认值 user
            .setItemCol("name") // 必须设置，默认值 item
            .setRatingCol("grade") // 必须设置，默认值 rating
            .setMaxIter(20)
            .setRegParam(0.01) //设置正则化参数lambda
            .setRank(20) //设置参考的特征数

        val model = als.fit(training)


        //关闭冷启动
        model.setColdStartStrategy("drop")

        //计算RMSE，评估数据模型
        val predictions = model.transform(test)

        val evaluator = new RegressionEvaluator()
            .setMetricName("rmse")
            .setLabelCol("grade")
            .setPredictionCol("prediction")

        val rmse = evaluator.evaluate(predictions)

        println(s"RMSE = $rmse")

        val res_to_users = model.recommendForAllUsers(10) //给所有用户推荐topN个商品

        res_to_users.write.saveAsTable("music163.xxx");

        // res_to_users.createOrReplaceTempView("recommend2")

        //spark.sql("create table if not exists music163.music_recommend(deviceId string, mname string, mark string)")

        //spark.sql("truncate table  music163.music_recommend")


        //spark.sql("create table music163.xxx as select uid,tags from recommend2 lateral view explode(recommendations) xxx as tags ")


        //res_to_users.


        spark.stop()
    }

}

