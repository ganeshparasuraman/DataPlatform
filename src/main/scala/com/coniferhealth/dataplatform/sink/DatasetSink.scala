package com.coniferhealth.dataplatform.sink

import org.apache.spark.sql.{DataFrameWriter, Row}

trait DatasetSink {
  def write(writer: DataFrameWriter[Row]): Unit
}


case class FileTarget(path: String, format: String) extends DatasetSink {
  override def write(writer: DataFrameWriter[Row]): Unit =
    writer
      .format(format)
      .save(path)
}

case class CSVFileTarget(path: String, format: String, header : Boolean, separator: String = ",") extends DatasetSink {
  override def write(writer: DataFrameWriter[Row]): Unit =
    writer
      .format(format)
      .option("header",header)
      .option("sep",separator)
      .save(path)
}
