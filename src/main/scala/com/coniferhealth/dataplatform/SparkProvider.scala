package com.coniferhealth.dataplatform

import org.apache.spark.sql.SparkSession

trait SparkProvider {
  def spark(): SparkSession
}

object LocalSparkProvider extends SparkProvider {
  override def spark(): SparkSession =
    SparkSession
      .builder
      .master("local[*]")
      //      .config("spark.sql.jsonGenerator.ignoreNullFields", "false")
      //to fix issue of port assignment o"n local
      .config("spark.driver.bindAddress", "localhost")
      .config("spark.scheduler.mode", "FAIR")
      .getOrCreate()
}