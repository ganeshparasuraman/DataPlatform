package com.coniferhealth.dataplatform.map

import com.coniferhealth.dataplatform.parser.CustomTypeFormats
import com.coniferhealth.dataplatform.sink.{CSVFileTarget, FileTarget}
import com.coniferhealth.dataplatform.source.{CSVFileSource, FileSource, JDBC}
import com.coniferhealth.dataplatform.transform.{Alias, Col, DatasetAlias, DatasetColumnOps, Drop, GroupBy, Joined, Read, ReadCSV, Select, Sql, Struct, WithColumn, WithColumnRenamed, Write}

object TransformConfigJsonImplicits {
  implicit val formats = new CustomTypeFormats(Map(

    "col" -> classOf[Col],
    "alias" -> classOf[Alias],
    "sql" -> classOf[Sql],
    "struct" -> classOf[Struct],

    "ds/read" -> classOf[Read],
    "ds/read/csv" -> classOf[ReadCSV],

    "ds/write" -> classOf[Write],

    "ds/select" -> classOf[Select],
    "ds/alias" -> classOf[DatasetAlias],
    "ds/join" -> classOf[Joined],
    "ds/groupby" -> classOf[GroupBy],

    "ds/cols" -> classOf[DatasetColumnOps],
    "ds/col/add" -> classOf[WithColumn],
    "ds/col/rename" -> classOf[WithColumnRenamed],
    "ds/col/drop" -> classOf[Drop],

    "src/jdbc" -> classOf[JDBC],
    "src/file" -> classOf[FileSource],
    "src/csv" -> classOf[CSVFileSource],
    "dst/file" -> classOf[FileTarget],
    "dst/csv" -> classOf[CSVFileTarget]
  ))
}