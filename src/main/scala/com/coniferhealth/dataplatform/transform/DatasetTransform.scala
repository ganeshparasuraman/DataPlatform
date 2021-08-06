package com.coniferhealth.dataplatform.transform

import com.coniferhealth.dataplatform.sink.DatasetSink
import com.coniferhealth.dataplatform.source.DatasetSource
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.{DataType, StructType}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JObject

trait DatasetTransform



case class Read(from: DatasetSource, schema: Option[JObject] = None) extends DatasetTransform
case class ReadCSV(from :DatasetSource,schema: Option[JObject] = None) extends DatasetTransform
case class Write(src: DatasetTransform, to: DatasetSink, saveMode: String = "append") extends DatasetTransform
case class DatasetAlias(alias: String, value: DatasetTransform) extends DatasetTransform
case class DatasetColumnOps(src: DatasetTransform, ops: Seq[ColumnOp]) extends DatasetTransform
case class Select(src: DatasetTransform, fields: Seq[ColumnSpec]) extends DatasetTransform
case class Joined(left: DatasetTransform, right: DatasetTransform, condition: ColumnSpec, joinType: String = "inner") extends DatasetTransform
case class GroupBy(src: DatasetTransform, groupBy: Seq[ColumnSpec], agg: Seq[ColumnSpec]) extends DatasetTransform

object DatasetTransform {

  def execute(spark: SparkSession)(config: DatasetTransform): DataFrame = {
    config match {
      case Read(from, schema) => {
        var reader = spark.read
        reader =
          if (schema.isEmpty) reader
          else {
            import org.json4s.jackson.Serialization
            implicit val formats = DefaultFormats

            reader.schema(DataType.fromJson(Serialization.write(schema)).asInstanceOf[StructType])
          }
        val df = from.read(reader)
        df.show()
        df
      }


      case Write(src, to, saveMode) => {
        val dataframe = execute(spark)(src)
        val writer =
          dataframe.write
            .mode(saveMode)
        to.write(writer)
        dataframe
      }
      case DatasetAlias(alias, value) => {
        execute(spark)(value).as(alias)
      }
      case Select(src, fields) => {
        val cols = fields.map(ColumnSpec.get)
        execute(spark)(src)
          .select(cols: _*)
      }
      case DatasetColumnOps(src, ops) => {
        val df = execute(spark)(src)
        ops.foldLeft(df) { case (df, op) => {
          println(op)
          op.transform(df)
        }
        }
      }

      case Joined(left, right, condition, joinType) => {
        execute(spark)(left)
          .join(execute(spark)(right), ColumnSpec.get(condition), joinType)
      }
      case GroupBy(src, groupBy, agg) => {
        val aggregates = agg.map(ColumnSpec.get)
        execute(spark)(src)
          .groupBy(groupBy.map(ColumnSpec.get): _*)
          .agg(aggregates.head, aggregates.tail: _*)
      }
      case unhandled => {
        throw new Exception(s"${unhandled.getClass.toString} is not handled by DatasetTransform.`execute`")
      }
    }
  }
}