package com.coniferhealth.dataplatform


import com.coniferhealth.dataplatform.transform.DatasetTransform
import org.apache.spark.sql.SparkSession
import org.json4s.jackson.JsonMethods

import scala.io.Source
import scala.io.Source.fromInputStream

object Main {
  /**
   *
   * @param args
   * This is the main method.
   * This method runs commandline program and creates a Spark Session at the Local Host.
   * This method Invokes the orchestrate method with spark session, filepath , outpath for results, and Config map
   */
  def main(args: Array[String]): Unit = {
//    val file = "classpath:01_transform.json"
//    val jsonConfig = Source.fromURL(file).mkString
    val content = fromInputStream(getClass.getResourceAsStream("01_transform.xml")).mkString

    runFromConfig(content)



  }

  def runFromConfig(jsonConfig: String) = {

    println(jsonConfig)

    import com.coniferhealth.dataplatform.map.TransformConfigJsonImplicits._
    val transformConfig = JsonMethods.parse(jsonConfig).extract[DatasetTransform]

    run(transformConfig)
  }

  def run(transformConfig: DatasetTransform) = {
    val spark = LocalSparkProvider.spark()
    DatasetTransform.execute(spark)(transformConfig)
  }


}
