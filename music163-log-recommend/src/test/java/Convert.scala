import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.{Row, SparkSession}

/**
  *
  */
object Convert {

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


        spark.stop()
    }

}

