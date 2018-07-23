import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.SparkSession

/**
  *
  */
object MusicRecommend {

    case class dataInfo(uid: Int, name: Int, grade: Float)

    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder()
            .appName("MusicRecommend")
            .master("local[4]")
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

        val numRanks = 8 to 20
        val numIters = 10 to 20
        val numLambdas = List(0.01, 1)

        var bestRanks = -1
        var bestIters = 0
        var bestLambdas = -1.0
        var bestRmse = Double.MaxValue
        var bestModel: Option[ALSModel] = None

        // 创建ALS的训练数据模型
        for (rank <- numRanks; iter <- numIters; lambda <- numLambdas) {

            val als = new ALS()
                .setUserCol("uid") // 必须设置，默认值 user
                .setItemCol("name") // 必须设置，默认值 item
                .setRatingCol("grade") // 必须设置，默认值 rating
                .setMaxIter(rank)
                .setRegParam(lambda) //设置正则化参数lambda
                .setRank(rank) //设置参考的特征数

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
            if (rmse < bestRmse) {
                bestRmse = rmse
                bestIters = iter
                bestLambdas = lambda
                bestRanks = rank
                bestModel = Some(model)
            }
        }
        println(s"Rank = $bestRanks , iter = $bestIters , lambda = $bestLambdas , RMSE = $bestRmse")

        val usersSet = ratings.select("uid").distinct().limit(10)
        val itemsSet = ratings.select("name").distinct().limit(10)
        val model = bestModel.get
        val res_to_allUser = model.recommendForAllUsers(3)
        val res_to_allItems = model.recommendForAllItems(3)
        val res_to_users = model.recommendForUserSubset(usersSet, 3) //给指定用户推荐topN个商品
        val res_to_items = model.recommendForItemSubset(itemsSet, 3) //给指定商品推荐topN个用户

        spark.stop()
    }

}

