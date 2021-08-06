package com.coniferhealth.dataplatform.source

import org.apache.spark.sql.{DataFrame, DataFrameReader}

trait DatasetSource {
  def read(reader: DataFrameReader): DataFrame
}


case class JDBC(driver: String, url: String, dbtable: String) extends DatasetSource {
  override def read(reader: DataFrameReader): DataFrame =
    reader
      .format("jdbc")
      .option("driver", driver)
      .option("url", url)
      .option("dbtable", dbtable)
      .load()
}

case class FileSource(path: String, format: String) extends DatasetSource {
  override def read(reader: DataFrameReader): DataFrame =
    reader
      .format(format)
      .load(path)
}

case class CSVFileSource(path: String, format: String, header : Boolean = false, separator : String =",") extends DatasetSource {
  override def read(reader: DataFrameReader): DataFrame = {
    reader
      .format(format)
      .option("sep",separator)
      .option("header",header)
      .load(path)
  }
}
