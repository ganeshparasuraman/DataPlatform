package com.coniferhealth.dataplatform.transform

import org.apache.spark.sql.{Column, DataFrame, functions}
import org.apache.spark.sql.functions.{col, struct}

trait ColumnSpec

case class Col(colName: String) extends ColumnSpec
case class Alias(alias: String, value: ColumnSpec) extends ColumnSpec
case class Sql(expr: String) extends ColumnSpec
case class Struct(fields: Seq[ColumnSpec]) extends ColumnSpec


trait ColumnOp {
  def transform(dataframe: DataFrame): DataFrame
}

case class WithColumn(name: String, value: ColumnSpec) extends ColumnOp {
  override def transform(dataframe: DataFrame): DataFrame = dataframe.withColumn(name, ColumnSpec.get(value))
}
case class WithColumnRenamed(from: String, to: String) extends ColumnOp {
  override def transform(dataframe: DataFrame): DataFrame = dataframe.withColumnRenamed(from, to)
}
case class Drop(columns: Seq[String]) extends ColumnOp {
  override def transform(dataframe: DataFrame): DataFrame = dataframe.drop(columns:_*)
}


object ColumnSpec {
  def get(config: ColumnSpec): Column = {
    config match {
      case Col(colName) => col(colName)
      case Alias(alias, value) => ColumnSpec.get(value).as(alias)
      case Sql(expr) => functions.expr(expr)
      case Struct(fields) => struct(fields.map(f => ColumnSpec.get(f)):_*)
      case unhandled => throw new Exception(s"${unhandled.getClass.toString} is not handled by ColumnSpec.`get`")
    }
  }
}